package com.smit.uber.controller;

import com.smit.uber.dto.RideRequest;
import com.smit.uber.dto.RideResponse;
import com.smit.uber.model.Ride;
import com.smit.uber.service.RideService;
import com.smit.uber.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/rides")
public class RideController {

    @Autowired
    private RideService rideService;

    @Autowired
    private UserService userService;

    @PostMapping
    public RideResponse createRide(@RequestBody RideRequest request, Authentication auth) {
        String userId = userService.findByUsername(auth.getName()).getId();
        Ride ride = new Ride();
        ride.setPickupLocation(request.getPickupLocation());
        ride.setDropLocation(request.getDropLocation());
        ride.setUserId(userId);
        ride.setStatus(com.smit.uber.model.RideStatus.REQUESTED);
        Ride saved = rideService.createRide(ride);
        return mapToResponse(saved);
    }

    @GetMapping("/my")
    public List<RideResponse> getMyRides(Authentication auth) {
        String userId = userService.findByUsername(auth.getName()).getId();
        return rideService.findRidesByUser(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private RideResponse mapToResponse(Ride ride) {
        RideResponse r = new RideResponse();
        r.setId(ride.getId());
        r.setUserId(ride.getUserId());
        r.setDriverId(ride.getDriverId());
        r.setPickupLocation(ride.getPickupLocation());
        r.setDropLocation(ride.getDropLocation());
        r.setStatus(ride.getStatus());
        r.setCreatedAt(ride.getCreatedAt().toString());
        return r;
    }
}
