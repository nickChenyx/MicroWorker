package com.nickchen.microworker.handler;

import com.nickchen.event.EventHandler;
import com.nickchen.microworker.event.XEvent;

/**
 * @see XEvent#PRODUCE_OVER
 */
public class ProduceOverEventHandler implements EventHandler {
    private final String taskName;

    public ProduceOverEventHandler(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public void resolve() {

    }
}
