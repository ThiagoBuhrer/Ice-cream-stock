package com.icecream.icecreamsstock;

import org.springframework.data.jpa.repository.JpaRepository;

// Repository layer responsible for database operations related to User entities.
// Extends JpaRepository to automatically provide CRUD methods.

public interface UserRepository extends JpaRepository<User, Long> {

    // Custom query method generated automatically by Spring Data JPA to find a user by username
    User findByUsername(String username);

}