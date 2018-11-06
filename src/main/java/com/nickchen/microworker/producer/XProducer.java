package com.nickchen.microworker.producer;

import java.util.List;

/**
 * 数据生产者
 *
 * @author nickChen
 * @date 2018年10月12日
 */
public interface XProducer<T> {

    T produce();

    List<T> produceBatch();
}
