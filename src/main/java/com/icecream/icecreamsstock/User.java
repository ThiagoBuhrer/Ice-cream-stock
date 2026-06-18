package com.icecream.icecreamsstock;

import jakarta.persistence.Table;
import jakarta.persistence.*;

// Entity class representing a User table in the database.
// Hibernate uses this class to map Java objects to database records.

@Entity
@Table(name = "users") // @Table defines the database table name that this entity is mapped to avoid conflicts with reserved keywords
public class User {

    // Instance Variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING) // Enum field that restricts user roles to predefined values instead of free text
    private Role role;

    // No-args constructor required by Hibernate ORM
    public User() {
    }

    // Constructor Method
    public User(String username, String password, Role role) {

        this.username = username;
        this.password = password;
        this.role = role;

    }

    // Access Methods (Getters)
    public Long getId() {
        return id;
    }


    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }


    public Role getRole() {
        return role;
    }

}