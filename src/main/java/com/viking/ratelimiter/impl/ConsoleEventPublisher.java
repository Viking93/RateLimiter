package com.viking.ratelimiter.impl;

import com.viking.ratelimiter.EventPublisher;

public class ConsoleEventPublisher<T> implements EventPublisher<T> {
    @Override
    public void onSuccess(T item) {
        //System.out.println("Allowed request at " + item);
    }

    @Override
    public void onFailure(T item) {
        //System.out.println("Rejected request at " + item);
    }
}
