package com.smit.uber.service;

import com.smit.uber.model.Ride;
import com.smit.uber.model.RideStatus;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * V2 Analytics Service - Empty implementations for live coding demonstration
 * This service will be implemented live during the lecture
 */
@Service
public class RideAnalyticsService {

    // Will be used during live coding implementation
    @SuppressWarnings("unused")
    private final MongoTemplate mongoTemplate;

    public RideAnalyticsService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * API 10: Rides per day (for charts, trends etc.)
     * TODO: Implement using aggregation pipeline
     * Expected: GROUP by createdDate, COUNT, SORT, PROJECT
     */
    public List<Document> ridesPerDay() {
        // TODO: Implement this method
        // Hint: Use aggregation with group, sort, and project operations
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("createdAt").count().as("count"),
                Aggregation.project("count").and("_id").as("date"),
                Aggregation.sort(Sort.by(Sort.Direction.ASC, "date"))
        );
        return mongoTemplate.aggregate(aggregation, Ride.class, Document.class)
                .getMappedResults();
    }

    /**
     * API 11: Driver summary - total rides, completed, cancelled, avg distance, total fare
     * TODO: Implement using aggregation pipeline
     * Expected: MATCH by driverId, GROUP with conditional counts, PROJECT
     */
    public Map<String, Object> getDriverStats(String driverId) {
        // TODO: Implement this method
        // Hint: Use MATCH → GROUP → PROJECT pattern
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("driverId").is(driverId)),
                Aggregation.group("driverId")
                        .count().as("totalRides")
                        .sum(ConditionalOperators
                                .when(Criteria.where("status").is(RideStatus.COMPLETED))
                                .then(1).otherwise(0))
                        .as("completedRides")
                        .sum(ConditionalOperators
                                .when(Criteria.where("status").is(RideStatus.CANCELLED))
                                .then(1).otherwise(0))
                        .as("cancelledRides")
                        .sum("distanceKm").as("totalDistance")
                        .sum("fare").as("totalFare"),
                Aggregation.project("totalRides", "completedRides",
                        "cancelledRides", "totalDistance", "totalFare")
        );
        List<Document> results = mongoTemplate
                .aggregate(aggregation, "ride", Document.class)
                .getMappedResults();

        if (results.isEmpty()) {
            return Map.of(
                    "totalRides", 0,
                    "completedRides", 0,
                    "cancelledRides", 0,
                    "totalDistance", 0.0,
                    "totalFare", 0.0
            );
        }

        return results.get(0);
        // Hint: Use ConditionalOperators.when() for conditional counts
    }

    /**
     * API 12: User spending summary - total completed rides & total fare
     * TODO: Implement using aggregation pipeline
     * Expected: MATCH by userId AND status=COMPLETED, GROUP, PROJECT
     */
    public Map<String, Object> getUserSpending(String userId) {
        // TODO: Implement this method
        // Hint: Use MATCH with AND condition, GROUP with count and sum, PROJECT
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("userId").is(userId)),
                Aggregation.group("userId")
                        .sum(ConditionalOperators
                                .when(Criteria.where("status").is(RideStatus.COMPLETED))
                                .then(1).otherwise(0))
                        .as("completedRides")
                        .sum("distanceKm").as("totalDistance")
                        .sum("fare").as("totalSpending"),
                Aggregation.project("totalCompletedRides",
                        "totalDistance", "totalSpending")
        );
        List<Document> results = mongoTemplate.aggregate(aggregation, Ride.class, Document.class).getMappedResults();

        if (results.isEmpty()) {
            return Map.of(
                    "totalCompletedRides", 0,
                    "totalDistance", 0,
                    "totalSpending", 0
            );
        }
        return results.get(0);
    }

    /**
     * API 13: Status summary - count rides grouped by status
     * TODO: Implement using aggregation pipeline
     * Expected: GROUP by status, COUNT, SORT, PROJECT
     */
    public List<Document> getStatusSummary() {
        // TODO: Implement this method
        // Hint: Use GROUP by status field, COUNT, SORT, PROJECT
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("status")
                        .count().as("count"),
                Aggregation.project("count")
                        .and("_id").as("status"),
                Aggregation.sort(Sort.Direction.ASC, "status")
        );
        return mongoTemplate.aggregate(aggregation, "ride", Document.class)
                .getMappedResults();
    }
}
