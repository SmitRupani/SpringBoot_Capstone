package com.smit.uber.controller;

import com.smit.uber.model.Ride;
import com.smit.uber.model.User;
import com.smit.uber.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/driver")
public class DriverController {
    @Autowired
    private RideService rideService;


    // view all pending request
    @GetMapping("/rides/requests")
    public List<Ride> getRequestRides() {
        return rideService.getPendingRides();
    }

    // accept ride
    @PostMapping("/rides/{id}/accept")
    public Ride acceptRide(@PathVariable("id") String rideId) {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        String driverId = user.getId();
        return rideService.acceptRide(rideId, driverId);
    }

}