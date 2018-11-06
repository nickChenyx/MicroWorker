package com.nickchen.microworker.demo;

import com.nickchen.microworker.exporter.AbstractXExporter;

public class UserExporter extends AbstractXExporter<User> {

    public UserExporter() {
        super("user-task");
    }

}
