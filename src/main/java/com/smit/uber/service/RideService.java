package com.smit.uber.service;

import com.smit.uber.model.Ride;
import com.smit.uber.model.RideStatus;
import com.smit.uber.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    public Ride createRide(Ride ride) {
        ride.setStatus(RideStatus.REQUESTED);
        return rideRepository.save(ride);
    }

    public List<Ride> findRidesByUser(String userId) {
        return rideRepository.findByUserId(userId);
    }

    public List<Ride> findRequestedRides() {
        return rideRepository.findByStatus(RideStatus.REQUESTED);
    }

    public Ride acceptRide(String rideId, String driverId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));
        ride.setDriverId(driverId);
        ride.setStatus(RideStatus.ACCEPTED);
        return rideRepository.save(ride);
    }
}
