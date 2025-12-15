package com.smit.uber.controller.v1;

import com.smit.uber.exception.BadRequestException;
import com.smit.uber.model.Ride;
import com.smit.uber.model.Role;
import com.smit.uber.model.User;
import com.smit.uber.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    RideService rideService;

    @GetMapping("/rides")
    public List<Ride> getRequestRides(){
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if(user.getRole() != Role.ROLE_USER){
            throw new BadRequestException("User only endpoint");
        }

        String userId = user.getId();

        return rideService.getUserRides(userId);
    }
}