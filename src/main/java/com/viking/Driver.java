package com.viking;

import com.viking.factories.RateLimiterFactory;
import com.viking.ratelimiter.Duration;
import com.viking.ratelimiter.RateLimiter;
import com.viking.ratelimiter.RateLimiterConfig;
import com.viking.ratelimiter.RateLimiterType;
import com.viking.ratelimiter.impl.ConsoleEventPublisher;
import com.viking.visitors.impl.RateLimiterVisitorImpl;

import java.util.concurrent.CountDownLatch;

import static com.viking.constants.Constants.MILLISECONDS;

public class Driver {
    public static void main(String[] args) throws InterruptedException {
        RateLimiterConfig rateLimiterConfig = RateLimiterConfig.builder()
                .duration(Duration.MILLISECONDS)
                .maxRequests(10)
                .build();
        RateLimiter rateLimiter = RateLimiterFactory.createRateLimiter(new RateLimiterVisitorImpl(),
                rateLimiterConfig,
                RateLimiterType.TOKEN_BUCKET,
                new ConsoleEventPublisher());

        Thread requestThread = new Thread(() -> {
            sendRequest(rateLimiter, 10, 1);
            sendRequest(rateLimiter, 20, 20);
            sendRequest(rateLimiter,50, 5);
            sendRequest(rateLimiter,100, 10);
            sendRequest(rateLimiter,200, 20);
            sendRequest(rateLimiter,250, 25);
            sendRequest(rateLimiter,500, 50);
            sendRequest(rateLimiter,1000, 100);
        });

        requestThread.start();
        requestThread.join();
    }

    private static void sendRequest(RateLimiter rateLimiter, int totalCnt, int requestPerSecond) {
        long startTime = System.currentTimeMillis();
        CountDownLatch doneSignal = new CountDownLatch(totalCnt);
        for(int i=0;i<totalCnt;i++){
            try{
                new Thread(() -> {
                    while (!rateLimiter.allow()){
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    doneSignal.countDown();
                }).start();
                Thread.sleep(MILLISECONDS / requestPerSecond);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        try {
            doneSignal.await();
            double duration = (System.currentTimeMillis() - startTime) / 1000.0;
            System.out.println(totalCnt + " requests processed in " + duration + " seconds. "
                    + "Rate: " + (double) totalCnt / duration + " per second");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
