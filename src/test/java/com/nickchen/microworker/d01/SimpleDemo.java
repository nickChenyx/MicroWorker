package com.nickchen.microworker.d01;

import com.nickchen.event.EventService;
import com.nickchen.microworker.consumer.SimpleXConsumer;
import com.nickchen.microworker.event.XEvent;
import com.nickchen.microworker.exporter.SimpleXExporter;
import com.nickchen.microworker.queue.MsgQueueWorker;
import com.nickchen.microworker.queue.QueueContext;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * MicroWorker 框架的简单使用
 *
 *<p>场景设定：</p>
 *  <p>
 *      学生喊 "到", 班长记录班级同学出勤情况
 *  </p>
 *
 * @author nickChen
 */
public class SimpleDemo {

    private SimpleXExporter<Integer> exporter = new SimpleXExporter<>("stu");
    private SimpleXConsumer<Integer> consumer = new SimpleXConsumer<>("stu");
    // 模拟异步处理
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2
            , 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
    public static void main(String[] args) throws InterruptedException {
        SimpleDemo sd = new SimpleDemo();

        AtomicInteger counter = new AtomicInteger(0);

        // 任务上下文
        QueueContext context = MsgQueueWorker.msgQueue.getQueueContext("stu");

        EventService.registerEventHandler(XEvent.CONSUME_OVER.toString(), () -> System.out.println(counter.get()));

        sd.executor.execute(()-> {
            System.out.println("班长统计人数");
            sd.consumer.consumer(counter::addAndGet);
        });

        sd.executor.execute(()-> {
            // 假定有 10 个学生
            System.out.println("学生喊 到");
            for (int i = 0; i < 10; i++) {
                Stu stu = new Stu();
                sd.exporter.export(stu.call());
            }
            sd.exporter.exportOver();
        });

        Thread.sleep(5000);
        sd.executor.shutdown();
        EventService.destroy();
    }
}
