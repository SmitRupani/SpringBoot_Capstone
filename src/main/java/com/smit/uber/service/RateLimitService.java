package com.smit.uber.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    private static final int LIMIT = 5; // rate limit per time window
    private static final int WINDOW_SECONDS = 60; // time in seconds

    private static class Counter{
        int count;
        long windowStart;
    }

    private final Map<String,Counter> store = new ConcurrentHashMap<>();

    //Check if a request is allowed for a given user
    /*
    key user_id or username get JWT
    if request > LIMIT in WINDOW_SECONDS return 429 Too Many Requests
     */

    public boolean allowRequest(String key){
        long now = Instant.now().getEpochSecond();
        Counter counter = store.get(key);

        //user request is coming for the first time
        if(counter == null) {
            counter = new Counter();
            counter.count = 1;
            counter.windowStart = now;
            store.put(key, counter);
            return true;
        }

        // check if user is coming after window time
        if(now - counter.windowStart >= WINDOW_SECONDS){
            counter.count = 1;
            counter.windowStart = now;
            return true;
        }

        // check if the limit is exceeded
        if(counter.count <= LIMIT) {
            counter.count++;
            return true;
        }

        return false; // limit exceeded
    }

}
