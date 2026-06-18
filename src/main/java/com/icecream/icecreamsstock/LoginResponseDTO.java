package com.icecream.icecreamsstock;


// DTO used to transfer authentication response data to the frontend
// It prevents exposing sensitive fields from the User entity, such as your password

public class LoginResponseDTO {

    private String username;
    private Role role;


    // Constructor
    public LoginResponseDTO(String username, Role role) {

        this.username = username;
        this.role = role;

    }


    // Getter methods
    public String getUsername() {

        return username;

    }


    public Role getRole() {

        return role;

    }

}