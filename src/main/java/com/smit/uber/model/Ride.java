package com.smit.uber.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "rides")
public class Ride {
    @Id
    private String id;
    private String userId;
    private String driverId;
    private String pickupLocation;
    private String dropLocation;
    private RideStatus status;
    private LocalDate createdAt;
    private LocalDate completedAt;
    private long durationMillis;
    private double fare;
    private double distance;

    public Ride() {
        this.createdAt = LocalDate.now();
        this.status = RideStatus.REQUESTED;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public RideStatus getStatus() {
        return status;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDate completedAt) {
        this.completedAt = completedAt;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public void setDurationMillis(long durationMillis) {this.durationMillis = durationMillis;}

    public double getFare() {return fare;}

    public void setFare(double fare) {this.fare = fare;}

    public double getDistance() {return distance;}

    public void setDistance(double distance) {this.distance = distance;}
}
