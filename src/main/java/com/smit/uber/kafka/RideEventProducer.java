package com.smit.uber.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class RideEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public RideEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishRideCompletedEvent(String rideId, String userId) {
        String topic = "ride-completed-events";
        String message = String.format("{\"rideId\":\"%s\", \"userId\":\"%s\"}", rideId, userId);
        kafkaTemplate.send(topic, message);
    }
}
