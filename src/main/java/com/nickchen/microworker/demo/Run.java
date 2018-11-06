package com.nickchen.microworker.demo;

import com.nickchen.event.EventService;
import com.nickchen.microworker.queue.MsgQueueWorker;

import java.util.List;

public class Run {
    public void runProducer() {
        UserProducer producer = new UserProducer();
        List<User> users = producer.produceBatch();
        UserExporter exporter = new UserExporter();
        exporter.exportBatch(users);
    }

    public void runConsumer() {
        UserConsumer consumer = new UserConsumer(2, 4);
//        consumer.consumerBatch();
    }

    public static void main(String[] args) throws InterruptedException {
        Run run = new Run();
        run.runProducer();
        run.runConsumer();
        while (true) {
            if (MsgQueueWorker.msgQueue.isTaskOver("user-task")) {
                Thread.sleep(3000);
                EventService.destroy();
                break;
            }
        }
    }
}
