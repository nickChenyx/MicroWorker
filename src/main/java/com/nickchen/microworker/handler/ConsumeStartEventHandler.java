package com.nickchen.microworker.handler;

import com.nickchen.event.EventHandler;
import com.nickchen.microworker.event.XEvent;

/**
 * @see XEvent#CONSUME_START
 */
public class ConsumeStartEventHandler implements EventHandler {

    private final String taskName;

    public ConsumeStartEventHandler(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public void resolve() {
        System.out.println(taskName + "'s consumer is start.");
    }
}
