package infostructure.di;

public interface PostProcessor {
    Object process(Object entity, String name);
}
