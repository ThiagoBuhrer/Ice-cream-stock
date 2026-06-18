package com.icecream.icecreamsstock;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


// Initializes default users when the application starts

@Component // @Component marks class as a Spring-managed component. This allows Spring to create and manage its object automatically.
public class UserDataInitializer implements CommandLineRunner {

    // Instance Variables
    // Repository used to access and save User data in the database
    private final UserRepository repository;


    // Constructor Method
    public UserDataInitializer(UserRepository repository) {
        this.repository = repository;
    }


    //Instance Methods
    // Method automatically executed by Spring Boot after the application starts
    @Override
    public void run(String... args) {

        // Creates default users only if the database has no users
        if (repository.count() == 0) {

            User staff = new User(
                    "staff",
                    "1234",
                    Role.STAFF
            );

            User manager = new User(
                    "manager",
                    "1234",
                    Role.MANAGER
            );

            repository.save(staff);
            repository.save(manager);

        }

    }

}