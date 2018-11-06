package com.nickchen.microworker.demo;

import com.nickchen.microworker.producer.XProducer;

import java.util.ArrayList;
import java.util.List;

public class UserProducer implements XProducer<User> {

    @Override
    public User produce() {
        User lili = new User("lili", 23);
        return lili;
    }

    @Override
    public List<User> produceBatch() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User("for" + i, 20 + i);
            users.add(user);
            System.out.println("produce " + user);
        }
        return users;
    }
}
