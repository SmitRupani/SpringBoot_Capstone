package com.smit.uber.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateRideRequest {
    @NotBlank(message = "Pickup is required")
    private String pickupLocation;
    @NotBlank(message = "Drop is required")
    private String dropLocation;
    @NotBlank(message = "Fare is required")
    private double fare;

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
    }

    public double getFare() {return fare;}

    public void setFare(double fare) {this.fare = fare;}
}