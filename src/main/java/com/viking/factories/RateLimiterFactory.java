package com.viking.factories;

import com.viking.ratelimiter.EventPublisher;
import com.viking.ratelimiter.RateLimiter;
import com.viking.ratelimiter.RateLimiterConfig;
import com.viking.ratelimiter.RateLimiterType;
import com.viking.visitors.RateLimiterVisitor;
import lombok.NonNull;

public class RateLimiterFactory {
    public static RateLimiter createRateLimiter(@NonNull RateLimiterVisitor visitor,
                                                @NonNull final RateLimiterConfig rateLimiterConfig,
                                                @NonNull final RateLimiterType rateLimiterType,
                                                final EventPublisher eventPublisher) {
        return  rateLimiterType.visit(visitor, rateLimiterConfig, eventPublisher);
    }
}
