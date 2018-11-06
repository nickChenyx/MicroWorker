package com.nickchen.microworker.queue;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Queue 的上下文信息，可以存和 taskName 相关的信息
 */
public class QueueContext {
    private ConcurrentHashMap<String, Object> context = new ConcurrentHashMap<>();

    public Object get(String key) {
        return context.get(key);
    }

    public void set(String key, Object value) {
        context.put(key, value);
    }
}
