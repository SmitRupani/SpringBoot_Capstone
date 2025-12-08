package com.smit.uber.repository;

import com.smit.uber.model.Ride;
import com.smit.uber.model.RideStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends MongoRepository<Ride, String> {
    List<Ride> findByStatus(RideStatus status);
    List<Ride> findByUserId(String userId);
}