package com.icecream.icecreamsstock;

import org.springframework.web.bind.annotation.*;

// Controller responsible for authentication-related endpoints such as login

@RestController // Marks this class as a REST API controller
public class AuthController {

    // Repository field (Dependency)
    // Used to access User data from the database
    private final UserRepository repository;


    // Constructor injection (Dependency Injection)
    // Spring automatically injects UserRepository to allow database access without manual instantiation
    public AuthController(UserRepository repository) {
        this.repository = repository;
    }


    // POST endpoint
    // Login endpoint that receives username and password and validates user credentials
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestParam String username, @RequestParam String password) {
        // @RequestParam extracts query parameters from the URL and passes them into the method as arguments.

        // Searches the database for a user with the provided username
        User user = repository.findByUsername(username);

        if (user == null || !user.getPassword().equals(password)) {

            throw new RuntimeException("Invalid credentials");

        }
        return new LoginResponseDTO(
                user.getUsername(),
                user.getRole()
        );

    }

}