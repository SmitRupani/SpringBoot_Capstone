# RideShare Backend – Mini Project 🚗

## 🚀 Project Overview  
This project is a mini ride-sharing backend built using **Spring Boot** and **MongoDB**. It supports:  

- User registration and login with **JWT authentication**  
- Passengers requesting rides  
- Drivers accepting and completing rides  
- Validation using **DTOs** and **Bean Validation**  
- Global exception handling  

The project follows a standard **Controller → Service → Repository** architecture and demonstrates role-based access for passengers and drivers.

## 📁 Project Structure

```text
src/
  main/
    java/
      org/example/rideshare/
        model/         # Entities (User, Ride)
        repository/    # MongoDB repositories
        service/       # Business logic
        controller/    # REST APIs
        config/        # Security and JWT configuration
        dto/           # Request/response DTOs with validation
        exception/     # Custom exceptions and global handler
        util/          # Utility classes (e.g. JWT utils)
    resources/
      application.properties
```


## 🏷 Entities  

### User  
- `id`: String (unique identifier)  
- `username`: String (unique)  
- `password`: String (BCrypt encrypted)  
- `role`: `ROLE_USER` or `ROLE_DRIVER`  

### Ride  
- `id`: String (unique identifier)  
- `userId`: Passenger's user ID  
- `driverId`: Driver's user ID (optional)  
- `pickupLocation`: String  
- `dropLocation`: String  
- `status`: `REQUESTED`, `ACCEPTED`, `COMPLETED`  
- `createdAt`: Timestamp  

---

## ✅ Features Implemented  

1. **User Registration & Login**  
   - JWT-based authentication  
   - Roles: `ROLE_USER` and `ROLE_DRIVER`  

2. **Passenger Functions**  
   - Request a new ride  
   - View own ride history  

3. **Driver Functions**  
   - View all pending ride requests  
   - Accept a ride request  
   - Complete an accepted ride  

4. **Validation & Error Handling**  
   - Request DTOs use `@NotBlank` and other validation annotations  
   - Global exception handler returns consistent error responses  

---

## 🌐 API Endpoints  

| Role      | Endpoint                                 | Method | Description |
|-----------|------------------------------------------|--------|-------------|
| PUBLIC    | `/api/auth/register`                      | POST   | Register a new user |
| PUBLIC    | `/api/auth/login`                         | POST   | Login and get JWT |
| USER      | `/api/v1/rides`                           | POST   | Request a ride |
| USER      | `/api/v1/user/rides`                      | GET    | Get own rides |
| DRIVER    | `/api/v1/driver/rides/requests`          | GET    | Get pending ride requests |
| DRIVER    | `/api/v1/driver/rides/{rideId}/accept`   | POST   | Accept a ride request |
| USER/DRIVER | `/api/v1/rides/{rideId}/complete`      | POST   | Complete a ride |

---

## 🧪 Example Requests  

```bash
# Register a user
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"1234","role":"ROLE_USER"}'

# Register a driver
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"driver1","password":"abcd","role":"ROLE_DRIVER"}'

# Login
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"1234"}'

# Create a ride (replace <token> with JWT)
curl -X POST http://localhost:8081/api/v1/rides \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"pickupLocation":"A","dropLocation":"B"}'
⚙️ How I Implemented It
Spring Boot with Spring Security for JWT authentication

MongoDB for persistence

DTOs for request/response mapping and validation

Exception handling with a global @ControllerAdvice

Role-based access control using @PreAuthorize

📚 Notes
I tested all endpoints using Postman, and they behave as expected.

The project demonstrates clean architecture, role-based access, and basic ride management.

Future improvements could include real-time ride tracking, payment integration, and driver rating.

✅ Submission
All requirements have been implemented according to the assignment instructions, and the backend is fully functional.
