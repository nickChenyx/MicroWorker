package com.nickchen.microworker.consumer;

import com.nickchen.event.EventService;
import com.nickchen.microworker.event.XEvent;
import com.nickchen.microworker.handler.ConsumeOverEventHandler;
import com.nickchen.microworker.handler.ConsumeStartEventHandler;
import com.nickchen.microworker.queue.MsgQueueWorker;
import com.nickchen.microworker.queue.XQueue;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public abstract class AbstractXConsumer<T> implements XConsumer<T> {

    private ThreadPoolExecutor executor;
    private ThreadPoolExecutor executorBatch;

    private XQueue queue;

    private final String taskName;

    private AtomicBoolean consumeStart = new AtomicBoolean(false);
    private AtomicBoolean consumeOver = new AtomicBoolean(false);

    public AbstractXConsumer(String taskName) {
        this.taskName = taskName;
        init(taskName);
    }

    public AbstractXConsumer(String taskName, int corePoolSize, int maxPoolSize) {
        this(taskName);
        executorBatch = new ThreadPoolExecutor(corePoolSize, maxPoolSize
                , 1, TimeUnit.MINUTES, new LinkedBlockingDeque<>()
                , new NamedThreadFactory(taskName + "-%d-pool"));
    }

    private void init(String taskName) {
        queue = MsgQueueWorker.msgQueue.getQueue(taskName);
        EventService.registerEventHandler(XEvent.CONSUME_START.toString(), new ConsumeStartEventHandler(taskName));
        EventService.registerEventHandler(XEvent.CONSUME_OVER.toString(), new ConsumeOverEventHandler(taskName));
    }

    @Override
    public void consumer(Consumer<T> func) {
        if (!consumeStart.compareAndSet(false, true)) {
            throw new IllegalStateException(getClass().getSimpleName() + " is already start.");
        }
        System.out.println("consumer starter");
        EventService.emitEvent(XEvent.CONSUME_START.toString());
        executor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
        executor.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    T pop = (T) queue.pop();
                    if (pop != null) {
//                        System.out.println(pop);
                        func.accept(pop);
                        continue;
                    } else {
//                        System.out.println("pop null");
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (MsgQueueWorker.msgQueue.isTaskOver(taskName) && queue.size() == 0) {
                        executor.shutdown();
                        if (consumeOver.compareAndSet(false, true)) {
                            System.out.println(taskName + " is over");
                            EventService.emitEvent(XEvent.CONSUME_OVER.toString());
                        }
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void consumerBatch(Consumer<T> func) {
        if (executorBatch == null) {
            System.out.println("consumer batch executor is null");
            return;
        }
        if (!consumeStart.compareAndSet(false, true)) {
            throw new IllegalStateException(getClass().getSimpleName() + " is already start.");
        }
        System.out.println("consumer batch");
        EventService.emitEvent(XEvent.CONSUME_START.toString());
        int maximumPoolSize = executorBatch.getMaximumPoolSize();
        for (int i = 0; i < maximumPoolSize; i++) {
            System.out.println(i);
            executorBatch.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        T pop = (T) queue.pop();
                        if (pop != null) {
//                            System.out.println(Thread.currentThread().getName() + pop);
                            func.accept(pop);
                        } else {
                            System.out.println(Thread.currentThread().getName() + "pop null");
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (MsgQueueWorker.msgQueue.isTaskOver(taskName) && queue.size() == 0) {
                            executorBatch.shutdown();
                            if (consumeOver.compareAndSet(false, true)) {
                                System.out.println(Thread.currentThread().getName() + taskName + " is over");
                                EventService.emitEvent(XEvent.CONSUME_OVER.toString());
                            }
                            break;
                        }
                    }
                }
            });
        }
    }
}
