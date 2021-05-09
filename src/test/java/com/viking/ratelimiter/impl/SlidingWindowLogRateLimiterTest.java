package com.viking.ratelimiter.impl;

import com.viking.ratelimiter.Duration;
import com.viking.ratelimiter.EventPublisher;
import com.viking.ratelimiter.RateLimiter;
import com.viking.ratelimiter.RateLimiterConfig;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.concurrent.atomic.AtomicInteger;

public class SlidingWindowLogRateLimiterTest extends TestCase {

    private RateLimiterConfig rateLimiterConfig = RateLimiterConfig.builder()
            .duration(Duration.MILLISECONDS)
            .maxRequests(2)
            .build();
    private EventPublisher eventPublisher = new ConsoleEventPublisher();
    private RateLimiter rateLimiter;

    public void setUp() throws Exception {
        super.setUp();
        rateLimiter = new SlidingWindowLogRateLimiter(rateLimiterConfig, eventPublisher);
    }

    public void tearDown() throws Exception {
        rateLimiter = null;
    }

    public void testAllow() throws InterruptedException {
        int requests = 4;
        int request_time_interval = 1000/requests;
        AtomicInteger accepted = new AtomicInteger(0);
        AtomicInteger rejected = new AtomicInteger(0);
        for(int i=0;i<requests;i++){
            Thread t = new Thread(() -> {
                if(rateLimiter.allow()){
                    accepted.incrementAndGet();
                }else{
                    rejected.incrementAndGet();
                }
            });
            t.start();
            Thread.sleep(request_time_interval);
        }
        Assert.assertEquals(2, accepted.get());
        Assert.assertEquals(2, rejected.get());
    }

    public void testAllowSuccess() throws InterruptedException {
        rateLimiterConfig.setMaxRequests(4);
        int requests = 4;
        int request_time_interval = 1000/requests;
        AtomicInteger accepted = new AtomicInteger(0);
        AtomicInteger rejected = new AtomicInteger(0);
        for(int i=0;i<requests;i++){
            Thread t = new Thread(() -> {
                if(rateLimiter.allow()){
                    accepted.incrementAndGet();
                }else{
                    rejected.incrementAndGet();
                }
            });
            t.start();
            Thread.sleep(request_time_interval);
        }
        Assert.assertEquals(4, accepted.get());
        Assert.assertEquals(0, rejected.get());
    }
}