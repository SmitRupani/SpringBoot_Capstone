package com.smit.uber.service;

import com.smit.uber.dto.CreateRideRequest;
import com.smit.uber.exception.BadRequestException;
import com.smit.uber.exception.NotFoundException;
import com.smit.uber.model.Ride;
import com.smit.uber.model.RideStatus;
import com.smit.uber.model.User;
import com.smit.uber.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    public Ride createRide(CreateRideRequest request, String userId) {
        Ride ride =  new Ride();
        ride.setUserId(userId);
        ride.setPickupLocation(request.getPickupLocation());
        ride.setDropLocation(request.getDropLocation());
        rideRepository.save(ride);
        return ride;
    }

    public List<Ride> getPendingRides() {
        return rideRepository.findByStatus(RideStatus.REQUESTED);
    }

    public Ride acceptRide(String rideId, String driverId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));
        if(ride.getStatus() == RideStatus.REQUESTED){
            ride.setDriverId(driverId);
            ride.setStatus(RideStatus.ACCEPTED);
            rideRepository.save(ride);
        }else{
            throw new BadRequestException("Ride is already requested");
        }
        return ride;
    }

    public Ride completeRide(String rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if(
                !ride.getUserId().equals(user.getId()) ||
                        !ride.getDriverId().equals(user.getId())
        ){
            throw new  BadRequestException("You are not allowed to complete this ride");
        }

        if(ride.getStatus() == RideStatus.ACCEPTED){
            ride.setStatus(RideStatus.COMPLETED);
            ride.setCompletedAt(new Date());
            ride.setDuration();
            rideRepository.save(ride);
        }else{
            throw new BadRequestException("Ride is either already completed or in REQUESTED state");
        }
        return ride;
    }

    public List<Ride> getUserRides(String userId) {

        return rideRepository.findByUserId(userId);
    }
}