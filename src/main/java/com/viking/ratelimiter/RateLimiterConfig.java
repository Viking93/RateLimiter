package com.viking.ratelimiter;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RateLimiterConfig {
    @NonNull
    private Integer maxRequests;

    @NonNull
    private Duration duration;
}
