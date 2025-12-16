package com.smit.uber.service;

import com.smit.uber.model.Ride;
import com.smit.uber.repository.RideRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

// Note: Imports will be used during live coding implementation

/**
 * V2 Query Service - Empty implementations for live coding demonstration
 * These methods use Criteria and MongoTemplate for complex queries
 */
@Service
public class RideQueryService {

    // Will be used during live coding implementation
    @SuppressWarnings("unused")
    private final MongoTemplate mongoTemplate;
    private final RideRepository rideRepository;

    public RideQueryService(MongoTemplate mongoTemplate, RideRepository rideRepository) {
        this.mongoTemplate = mongoTemplate;
        this.rideRepository = rideRepository;
    }

    /**
     * API 2: Filter rides by distance range (min-max)
     * TODO: Implement using Criteria with gte and lte operators
     * Expected: Criteria.where("distanceKm").gte(min).lte(max)
     */
    public List<Ride> filterByDistance(Double min, Double max) {
        // TODO: Implement this method
        // Hint: Use Criteria.where("distanceKm").gte(min).lte(max)
        Criteria criteria = Criteria.where("distance")
                .gte(min != null ? min : 0)
                .lte(max != null ? max : Double.MAX_VALUE);
        // Hint: Create Query with criteria, then use mongoTemplate.find()

        Query query = new Query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }

    /**
     * API 3: Filter rides between date range
     * TODO: Implement using Criteria with gte and lte for dates
     * Expected: Criteria.where("createdDate").gte(start).lte(end)
     */
    public List<Ride> filterByDateRange(LocalDate start, LocalDate end) {
        // TODO: Implement this method
        // Hint: Similar to filterByDistance but for date field
        Criteria criteria = Criteria.where("createdAt")
                .gte(start)
                .lte(end);

        Query query = new Query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }

    /**
     * API 4: Sort rides by fare amount
     * TODO: Implement using Sort with Query
     * Expected: Sort.by(direction, "fareAmount") and query.with(sort)
     */
    public List<Ride> sortByFare(String order) {
        // TODO: Implement this method
        // Hint: Create Sort object with direction (ASC/DESC) and field name
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "fare");

        // Hint: Create Query and use query.with(sort)
        Query query = new Query().with(sort);
        return mongoTemplate.find(query, Ride.class);
    }

    /**
     * API 1: Search rides by pickup OR drop location
     * TODO: Implement using Criteria with orOperator and regex
     * Expected: new Criteria().orOperator(...) with regex patterns
     */
    public List<Ride> searchRides(String text) {
        // TODO: Implement this method
        // Hint: Create Pattern with CASE_INSENSITIVE flag
        // Hint: Pattern.compile(text, Pattern.CASE_INSENSITIVE)
        Pattern regex = Pattern.compile(text, Pattern.CASE_INSENSITIVE);
        // Hint: Use orOperator with pickupLocation and dropLocation regex
        Criteria criteria = new Criteria().orOperator(
                Criteria.where("pickupLocation").regex(regex),
                Criteria.where("dropLocation").regex(regex)
        );
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }

    /**
     * API 8: Filter rides by status and keyword search (AND + OR combo)
     * TODO: Implement complex AND + OR combination
     * Expected: status = X AND (pickup contains text OR drop contains text)
     */
    public List<Ride> filterByStatusAndSearch(String status, String search) {

        List<Criteria> criteriaList = new ArrayList<>();

        // Status filter (if provided)
        if (status != null && !status.isEmpty()) {
            criteriaList.add(Criteria.where("status").is(status));
        }

        // Search filter (pickup OR drop)
        if (search != null && !search.isEmpty()) {
            Pattern regex = Pattern.compile(search, Pattern.CASE_INSENSITIVE);

            Criteria searchCriteria = new Criteria().orOperator(
                    Criteria.where("pickupLocation").regex(regex),
                    Criteria.where("dropLocation").regex(regex)
            );

            criteriaList.add(searchCriteria);
        }

        // If no filters were given — return everything or empty list (your choice)
        if (criteriaList.isEmpty()) {
            return mongoTemplate.findAll(Ride.class);
        }

        // Build final query
        Criteria finalCriteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
        Query query = new Query(finalCriteria);

        return mongoTemplate.find(query, Ride.class);
    }


    /**
     * API 9: Advanced search with multiple criteria + pagination
     * TODO: Implement with sorting and pagination
     * Expected: Criteria + Sort + Pageable
     */
    public List<Ride> advancedSearch(String search, String status, String sortField,
                                     String order, int page, int size) {
        // TODO: Implement this method
        // Hint: Similar to filterByStatusAndSearch but add:
        //   - Sorting using query.with(Sort.by(...))
        //   - Pagination using query.with(PageRequest.of(page, size))
        List<Criteria> criteriaList = new ArrayList<>();
        // Status filter
        if (status != null && !status.isEmpty()) {
            criteriaList.add(Criteria.where("status").is(status));
        }
        // Search filter
        if (search != null && !search.isEmpty()) {
            Pattern regex = Pattern.compile(search, Pattern.CASE_INSENSITIVE);
            Criteria searchCriteria = new Criteria().orOperator(
                    Criteria.where("pickupLocation").regex(regex),
                    Criteria.where("dropLocation").regex(regex)
            );
            criteriaList.add(searchCriteria);
        }
        // Sorting
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortField != null ? sortField : "fare");
        // Pagination
        Pageable pageable = PageRequest.of(page, size);
        // If no filters were given — return paginated and sorted results
        if (criteriaList.isEmpty()) {
            Query query = new Query().with(sort).with(pageable);
            return mongoTemplate.find(query, Ride.class);
        }
        // Build final query
        Criteria finalCriteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
        Query query = new Query(finalCriteria).with(sort).with(pageable);
        return mongoTemplate.find(query, Ride.class);
    }

    /**
     * API 7: Get active rides for a driver (status = ACCEPTED)
     * TODO: Implement using Criteria with AND operator
     * Expected: Criteria.where("driverId").is(id).and("status").is("ACCEPTED")
     */
    public List<Ride> getDriverActiveRides(String driverId) {
        // TODO: Implement this method
        // Hint: Use AND operator: .and("status").is("ACCEPTED")
        Criteria criteria = Criteria.where("driverId").is(driverId)
                .and("status").is("ACCEPTED");
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }

    public List<Ride> getRidesForDate(LocalDate date) {
        return rideRepository.findByCreatedAt(date);
    }
}
