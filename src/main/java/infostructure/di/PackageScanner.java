package infostructure.di;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PackageScanner {

    public Set<Class<?>> findClasses(String packageName) {
        try {
            Enumeration<URL> resources = ClassLoader.getSystemClassLoader()
                    .getResources(packageName);
            URL packageUrl = resources.nextElement();

            if (packageUrl.getProtocol().equals("jar")){
                return scanJar(packageUrl, packageName);
            }
            else return scan(packageUrl, packageName);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<Class<?>> scanJar(URL packageURL, String packageName){
        try {
            var classes = new HashSet<Class<?>>();

            if (packageURL.getProtocol().equals("jar")){

                String jarPath = PackageScanner.class.getProtectionDomain()
                        .getCodeSource().getLocation().toURI().getPath();
                JarFile jarFile = new JarFile(jarPath);

                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (isClassFileFromRequiredPackage(packageName, entry)){
                        classes.add(getClass(entry.getRealName()));
                    }
                }
            }
            return classes;
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isClassFileFromRequiredPackage(String packageName, JarEntry entry){
        String entryName = entry.getRealName();
        return !entry.isDirectory() && entry.getRealName().startsWith(packageName)
                && entryName.endsWith(".class") && !entryName.contains("$");
    }

    private Set<Class<?>> scan(URL packageURL, String packageName){
        try (var reader = new BufferedReader(new InputStreamReader(packageURL.openStream()))){
            String line;
            var classes = new HashSet<Class<?>>();
            while ((line = reader.readLine()) != null){
                if (line.endsWith(".class")){
                    classes.add(getClass(line, packageName));
                }
                else {
                    try {
                        classes.addAll(findClasses(packageName+"/"+line));
                    }catch (Exception ignored){}
                }
            }
            return classes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Class<?> getClass(String className, String packageName) {
        return getClass(packageName + "." + className);
    }

    private Class<?> getClass(String classFullName) {
        try {
            classFullName = classFullName.replaceAll("/", ".");
            return Class.forName(classFullName.substring(0, classFullName.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
