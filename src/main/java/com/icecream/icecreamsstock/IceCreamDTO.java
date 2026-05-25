package com.icecream.icecreamsstock;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;


// DTO (Data Transfer Object) is an intermediate layer between the API and the database.
// You need an intermediate layer to control and validate data before processing it.
// It's used to avoid exposing the entity directly and to send only the necessary data through the API.
// Like the IceCream class, IceCreamDTO is also considered a Model class in the MVC.

public class IceCreamDTO {

    // Only fields that the client is actually allowed to send to the API.
    // Fields like ID are excluded because they are managed by the database (auto-generated). There are others too.

    @NotBlank
    private String flavor;
    @PositiveOrZero
    private double stockQuantityKG;
    @NotBlank
    private String madeAt;

    // @NotBlank - field cannot be blank
    // @PositiveOrZero - field has to be => 0


    // Getters and Setters
    // Spring uses these methods to convert JSON into Java (JSON Mapping)
    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public double getStockQuantityKG() {
        return stockQuantityKG;
    }

    public void setStockQuantityKG(double stockQuantityKG) {
        this.stockQuantityKG = stockQuantityKG;
    }

    public String getMadeAt() {
        return madeAt;
    }

    public void setMadeAt(String madeAt) {
        this.madeAt = madeAt;
    }

}