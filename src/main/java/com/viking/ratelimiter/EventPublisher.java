package com.viking.ratelimiter;

public interface EventPublisher<E> {
    void onSuccess(E item);
    void onFailure(E item);
}
