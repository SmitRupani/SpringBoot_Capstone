package com.smit.uber.repository;

import com.smit.uber.model.Ride;
import com.smit.uber.model.RideStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RideRepository extends MongoRepository<Ride, String> {
    List<Ride> findByStatus(RideStatus status);
    List<Ride> findByUserId(String userId);
    Iterable<Ride> findByPickupLocationAndDropLocation(String from, String to);
    List<Ride> findByCreatedAt(LocalDate date);
}