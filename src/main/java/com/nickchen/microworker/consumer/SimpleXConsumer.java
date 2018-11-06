package com.nickchen.microworker.consumer;

public class SimpleXConsumer<T> extends AbstractXConsumer<T> {
    public SimpleXConsumer(String taskName) {
        super(taskName);
    }

    public SimpleXConsumer(String taskName, int corePoolSize, int maxPoolSize) {
        super(taskName, corePoolSize, maxPoolSize);
    }
}
