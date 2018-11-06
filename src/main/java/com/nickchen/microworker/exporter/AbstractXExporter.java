package com.nickchen.microworker.exporter;

import com.nickchen.microworker.queue.MsgQueue;
import com.nickchen.microworker.queue.MsgQueueWorker;
import com.nickchen.microworker.queue.SimpleXQueue;

import java.util.List;

public abstract class AbstractXExporter<T> implements XExporter<T> {

    private MsgQueue msgQueue = MsgQueueWorker.msgQueue;

    private final String taskName;

    public AbstractXExporter(String taskName) {
        this.taskName = taskName;
        init(taskName);
    }

    /**
     * 注册任务队列
     * @param taskName
     */
    private void init(String taskName) {
        boolean b = msgQueue.registerTask(taskName, new SimpleXQueue<T>());
    }

    @Override
    public void export(T data) {
        msgQueue.getQueueOptional(taskName).ifPresent(q -> {
            q.push(data);
        });
    }

    @Override
    public void exportBatch(List<T> batch) {
        msgQueue.getQueueOptional(taskName).ifPresent(q -> {
            batch.forEach(item -> {
                System.out.println("export " + item);
                q.push(item);
            });
        });
    }

    @Override
    public void exportOver() {
        msgQueue.setTaskOver(taskName);
    }
}
