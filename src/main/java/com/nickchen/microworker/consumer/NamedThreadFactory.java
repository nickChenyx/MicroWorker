package com.nickchen.microworker.consumer;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用构造函数，创建有名称的线程
 * 构造函数中传入 "user-%d-pool" 形式的字符串，用一个 "%d" 占位
 */
public class NamedThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final String namePrefix;
    private final boolean daemon;

    public NamedThreadFactory(String namePrefix) {
        this(namePrefix, false);
    }

    public NamedThreadFactory(String namePrefix, boolean daemon) {
        this.namePrefix = String.format(namePrefix, poolNumber.getAndIncrement());
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(null, r,
                namePrefix + poolNumber.getAndIncrement(),
                0);
        t.setDaemon(daemon);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
