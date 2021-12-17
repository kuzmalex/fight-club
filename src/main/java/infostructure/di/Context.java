package infostructure.di;

import infostructure.Properties;
import infostructure.di.annotations.*;
import org.flywaydb.core.Flyway;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.*;

public class Context {

    private final static String PROPERTIES_PATH = "properties.txt";

    volatile boolean started = false;
    final Object startLock = new Object();

    private final Map<Class<?>, Object> managedEntities = new HashMap<>();
    private final Map<String, Object> managedNamedEntities = new HashMap<>();
    private final Map<Object, Integer> elementsOrder = new HashMap<>();
    private Set<Class<?>> configsToProcess = new LinkedHashSet<>();

    private final Set<Method> processedMethods = new HashSet<>();

    private final List<PostProcessor> postProcessors;

    public Context() {
        this.postProcessors = List.of(new HttpHandlerPostProcessor());
    }

    public void start() {
        if (!started){
            synchronized (startLock){
                if (!started){
                    try {
                        initContextEntities();
                        postProcessEntities();
                        migrateDb();
                        invokeStartMethods();
                        started = true;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void initContextEntities() {

        var properties = new Properties(Paths.get(PROPERTIES_PATH));
        managedEntities.put(Properties.class, properties);

        var packageScanner = new PackageScanner();
        configsToProcess = packageScanner.findClasses("config");

        boolean newEntityCreated;
        do {
            newEntityCreated = false;

            for (Class<?> configuration : configsToProcess.toArray(new Class[0])) {

                configsToProcess.remove(configuration);

                if (!isConfiguration(configuration)){
                    continue;
                }

                for (Method method : configuration.getMethods()) {

                    method.setAccessible(true);

                    if (processedMethods.contains(method)){
                        continue;
                    }

                    for (Annotation methodAnnotation : method.getDeclaredAnnotations()) {

                        boolean isManagedObject = methodAnnotation.annotationType()
                                .isAssignableFrom(ManagedObject.class);

                        boolean isManagedCollectionElement = methodAnnotation.annotationType()
                                .isAssignableFrom(ManagedCollectionElement.class);

                        if (isManagedObject || isManagedCollectionElement) {

                            Optional<Object[]> args =
                                    matchConfigMethodArgsWithContextEntities(method, configuration, properties);

                            if (args.isPresent()) {

                                Class<?> returnType = null;
                                if (isManagedObject) {
                                    returnType = method.getReturnType();
                                }

                                if (isManagedCollectionElement) {
                                    Class<?> elementType = method.getReturnType();
                                    returnType = Array.newInstance(elementType, 0).getClass();
                                }

                                Object config;
                                Object newObject;
                                try {
                                    config = configuration.getConstructor().newInstance();
                                    newObject = method.invoke(config, args.get());
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }


                                if (isManagedObject){
                                    addManagedObjectToContext(newObject, returnType, methodAnnotation);
                                }

                                if (isManagedCollectionElement){
                                    addCollectionElementToContext(newObject, returnType, methodAnnotation);
                                }

                                processedMethods.add(method);
                                newEntityCreated = true;
                            }
                        }
                    }
                }
            }
        }while (!configsToProcess.isEmpty() && newEntityCreated);

        if (!configsToProcess.isEmpty()){
            throw new RuntimeException("Managed objects was not initialized");
        }
    }

    private boolean isConfiguration(Class<?> configuration){
        for (Annotation classAnnotation : configuration.getDeclaredAnnotations()) {
            if (classAnnotation.annotationType().isAssignableFrom(Configuration.class)){
                return true;
            }
        }
        return false;
    }

    private Optional<Object[]> matchConfigMethodArgsWithContextEntities(
            Method method, Class<?> configuration,
            Properties properties
    ){
        Class<?>[] paramTypes = method.getParameterTypes();
        Annotation[][] paramsAnnotations = method.getParameterAnnotations();

        var args = new Object[paramsAnnotations.length];

        for (int paramNumber = 0; paramNumber < paramsAnnotations.length; paramNumber++) {
            Class<?> paramType = paramTypes[paramNumber];

            Object managedEntity = null;

            for (Annotation paramAnnotation : paramsAnnotations[paramNumber]) {
                try {
                    if (paramAnnotation.annotationType().isAssignableFrom(Named.class)) {

                        Class<?> type = paramAnnotation.annotationType();
                        Method nameGetter = type.getMethod("value");
                        String name = (String)nameGetter.invoke(paramAnnotation);

                        managedEntity = managedNamedEntities.get(name);
                    }
                    else if (paramAnnotation.annotationType().isAssignableFrom(Value.class)){
                        Class<?> type = paramAnnotation.annotationType();
                        Method nameGetter = type.getMethod("value");
                        String name = (String)nameGetter.invoke(paramAnnotation);
                        managedEntity = properties.getValue(name);
                    }
                } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            if (managedEntity == null)
                managedEntity = managedEntities.get(paramType);

            if (managedEntity == null) {
                configsToProcess.add(configuration);
                return Optional.empty();
            }

            args[paramNumber] = managedEntity;
        }

        return Optional.of(args);
    }

    private void addCollectionElementToContext(Object newObject, Class<?> returnType, Annotation methodAnnotation){
        List<Object> collection;

        String name = null;
        Integer order = null;

        Class<?> type = methodAnnotation.annotationType();
        for (Method annotationMethod : type.getDeclaredMethods()){
            if (annotationMethod.getName().equals("name")){
                try {
                    name = (String)annotationMethod.invoke(methodAnnotation);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            else if (annotationMethod.getName().equals("order")){
                try {
                    order = (Integer) annotationMethod.invoke(methodAnnotation);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (!managedNamedEntities.containsKey(name)) {
            collection = new ArrayList<>();
            collection.add(newObject);
        }
        else {
            collection = (List) managedNamedEntities.get(name);
            collection.add(newObject);
        }

        elementsOrder.put(newObject, order);
        collection.sort(Comparator.comparingInt(elementsOrder::get));

        managedNamedEntities.put(name, collection);
    }

    private void addManagedObjectToContext(Object newObject, Class<?> returnType, Annotation methodAnnotation){
        try {
            String name = "";

            Class<?> type = methodAnnotation.annotationType();
            for (Method annotationMethod : type.getDeclaredMethods()){
                if (annotationMethod.getName().equals("name")){
                    name = (String)annotationMethod.invoke(methodAnnotation);
                }
            }

            if (name.length() > 0){
                if (!managedNamedEntities.containsKey(name)) {
                    managedNamedEntities.put(name, newObject);
                }
                else throw new RuntimeException("Two managed object with same name: " + name);
            }
            else if (!managedEntities.containsKey(returnType)) {
                managedEntities.put(returnType, newObject);
            }
            else throw new RuntimeException("Two managed object with same type: " + returnType);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void postProcessEntities(){
        postProcessors.forEach(
                postProcessor -> {
                    var processedEntities = new HashMap<Class<?>, Object>();
                    managedEntities.forEach((clazz, entity) -> {
                        processedEntities.put(clazz, postProcessor.process(entity, null));
                    });
                    managedEntities.clear();
                    managedEntities.putAll(processedEntities);

                    var processedNamedEntities = new HashMap<String, Object>();
                    managedNamedEntities.forEach((name, entity) -> {
                        processedNamedEntities.put(name, postProcessor.process(entity, name));
                    });
                    managedNamedEntities.clear();
                    managedNamedEntities.putAll(processedNamedEntities);
                }
        );
    }

    private void migrateDb() {
        var flyway = managedEntities.get(Flyway.class);
        if (flyway != null){
            ((Flyway)flyway).migrate();
        }
    }

    private void invokeStartMethods() {
        var startPoints = lookUpForStartMethods();
        startPoints.forEach(startMethod -> {
            Object managedObjectWithStartMethod = startMethod.managedObject();
            Method method = startMethod.method();
            method.setAccessible(true);
            try {
                method.invoke(managedObjectWithStartMethod);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private List<StartMethod> lookUpForStartMethods() {
        var startMethods = new ArrayList<StartMethod>();
        for (Object managedObject : managedEntities.values()){
            for (Method method : managedObject.getClass().getDeclaredMethods()){
                for (Annotation methodAnnotation : method.getDeclaredAnnotations()) {
                    if (methodAnnotation.annotationType().isAssignableFrom(AfterContextInitialization.class)) {
                        startMethods.add(new StartMethod(managedObject, method));
                    }
                }
            }
        }
        return startMethods;
    }

    private static record StartMethod(Object managedObject, Method method){}
}
