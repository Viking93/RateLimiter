package com.viking.utilities;

import com.viking.ratelimiter.Duration;

import static com.viking.constants.Constants.MICROSECONDS;
import static com.viking.constants.Constants.MILLISECONDS;

public class TimeUtils {
    public static Integer getTimeRequestInterval(Duration duration){
        if(duration == Duration.MICROSECONDS){
            return MICROSECONDS;
        }else if (duration == Duration.MILLISECONDS){
            return MILLISECONDS;
        }

        return MILLISECONDS;
    }
}
