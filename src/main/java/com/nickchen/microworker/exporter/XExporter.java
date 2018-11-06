package com.nickchen.microworker.exporter;

import com.nickchen.microworker.producer.XProducer;
import com.nickchen.microworker.queue.XQueue;

import java.util.List;

/**
 * 数据传输，将 {@link XProducer} 生成的数据传输到 {@link XQueue}
 *
 * @author nickChen
 * @date 2018年10月12日
 */
public interface XExporter<T> {
    /**
     * 将 {@link XProducer} 生成的数据传输到 {@link XQueue}
     */
    void export(T data);

    void exportBatch(List<T> batch);

    void exportOver();
}
