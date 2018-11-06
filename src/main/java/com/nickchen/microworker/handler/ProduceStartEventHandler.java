package com.nickchen.microworker.handler;

import com.nickchen.event.EventHandler;
import com.nickchen.microworker.event.XEvent;

/**
 * @see XEvent#PRODUCE_START
 */
public class ProduceStartEventHandler implements EventHandler {

    private final String taskName;

    public ProduceStartEventHandler(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public void resolve() {

    }
}
