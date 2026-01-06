package com.smit.uber.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class RideEventConsumer {

    @KafkaListener( topics = "ride-completed-events", groupId = "uber-consumer-group")
    public void consumeRideCompletedEvent(String message) {
        System.out.println("Consumed pinged ");
        System.out.println("Received ride completed event: " + message);
    }
}
