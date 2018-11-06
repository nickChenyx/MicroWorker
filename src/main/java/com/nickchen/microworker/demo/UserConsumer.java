package com.nickchen.microworker.demo;

import com.nickchen.microworker.consumer.AbstractXConsumer;

public class UserConsumer extends AbstractXConsumer<User> {
    public UserConsumer() {
        super("user-task");
    }

    public UserConsumer(int corePoolSize, int maxPoolSize) {
        super("user-task", corePoolSize, maxPoolSize);
    }
}
