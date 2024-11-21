package me.vifez.core.util.mongo.async;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class AsyncResult<T> {

    private Set<Consumer<T>> consumers = new HashSet<>();
    private T result;

    public void addCallback(Consumer<T> consumer) {
        consumers.add(consumer);

        if (result != null) {
            consumer.accept(result);
        }
    }

    public AsyncResult<T> complete(T result) {
        for (Consumer<T> consumer : consumers) {
            consumer.accept(result);
        }
        return this;
    }

}