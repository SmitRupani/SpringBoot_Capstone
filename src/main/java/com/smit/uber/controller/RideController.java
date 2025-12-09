package com.smit.uber.controller;

import com.smit.uber.dto.CreateRideRequest;
import com.smit.uber.model.Ride;
import com.smit.uber.model.User;
import com.smit.uber.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rides")
public class RideController {
    @Autowired
    private RideService rideService;

    // Create ride
    @PostMapping()
    public Ride createRide(@RequestBody CreateRideRequest createRideRequest){
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        String userId = user.getId();

        return rideService.createRide(createRideRequest, userId);
    }

    // Complete ride
    @GetMapping("/{id}/complete")
    public Ride completeRide(@PathVariable String id){
        return rideService.completeRide(id);
    }
}