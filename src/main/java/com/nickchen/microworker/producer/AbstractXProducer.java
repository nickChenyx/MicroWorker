package com.nickchen.microworker.producer;

import com.nickchen.microworker.queue.MsgQueueWorker;

import java.util.List;

public abstract class AbstractXProducer<T> implements XProducer<T> {

    public final String taskName;
    public static final int DEFAULT_MAXIMUM_QUEUE_SIZE = 1000;

    public AbstractXProducer(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public T produce() {
        if (MsgQueueWorker.msgQueue.getQueue(taskName).size() > DEFAULT_MAXIMUM_QUEUE_SIZE) {

        }
        return produceX();
    }

    @Override
    public List<T> produceBatch() {
        return produceBatchX();
    }

    public abstract T produceX();
    public abstract List<T> produceBatchX();
}
