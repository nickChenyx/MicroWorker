package com.nickchen.microworker.consumer;

import java.util.function.Consumer;

/**
 * 消费者模型
 *
 * @author nickChen
 * @date 2018年10月13日
 */
public interface XConsumer<T> {

    /**
     * 单线程消费
     */
    void consumer(Consumer<T> func);

    /**
     * 多线程消费
     */
    void consumerBatch(Consumer<T> func);
}
