package com.nickchen.microworker.queue;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 基于 {@link XQueue} 的基础实现
 *
 * @param <T>
 * @author nickChen
 * @date 2018年10月11日
 */
public class SimpleXQueue<T> implements XQueue<T> {

    /**
     * 真正存储 T 的位置
     */
    private final ConcurrentLinkedQueue<T> queue = new ConcurrentLinkedQueue<>();

    private AtomicLong size = new AtomicLong();

    @Override
    public boolean push(T t) {
        size.incrementAndGet();
        return queue.offer(t);
    }

    @Override
    public T pop() {
        T polled = queue.poll();
        if (polled == null) {
            return null;
        }
        size.decrementAndGet();
        return polled;
    }

    /**
     * @return
     */
    @Override
    public long size() {
        return size.get();
    }
}
