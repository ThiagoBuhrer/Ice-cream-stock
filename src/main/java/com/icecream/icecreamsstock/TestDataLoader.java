package com.icecream.icecreamsstock;

import org.springframework.boot.CommandLineRunner; // Runs code automatically when Spring Boot starts
import org.springframework.stereotype.Component; // Marks this class as managed by Spring
import java.time.LocalDate;

@Component
// @Component is a Spring annotation that tells the framework to create and manage an object of this class automatically
public class TestDataLoader implements CommandLineRunner {
    // CommandLineRunner is a Spring Boot interface that allows you to automatically execute code as soon as the application finishes starting up

    private final IceCreamRepository repository;
    // Repositories are used to access the database
    // obs: this is an instance variable of the TestDataLoader class

    public TestDataLoader(IceCreamRepository repository) {
        // Constructor Injection:
        // Spring automatically injects the repository here
        this.repository = repository;
    }

    @Override
    // This method runs automatically after the application starts
    // The run() method was already defined in the CommandLineRunner interface.
    public void run(String... args) {

        // Creates a new IceCream object
        IceCream iceCream = new IceCream("Chocolate", 5.0, LocalDate.now());

        // Saves the object into the database
        repository.save(iceCream);

        // Prints message in console
        System.out.println("\nIce cream saved!");

    }
}