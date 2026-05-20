package com.icecream.icecreamsstock;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity // This syntax creates an entity that represents a table. This is done by Hibernate/JPA.
// @Entities are automatically transformed from Java objects into tables and SQL in the database.
public class IceCream {

    // Instance Variables
    @Id // Indicates the Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Database generates the ID automatically
    private long id; // Primary Key (placed right next to @Id)
    private String flavor;
    private double stockQuantityKG;
    private LocalDate madeAt;


    // Empty Constructor Method (required by Hibernate ORM to create objects)
    public IceCream() {
    }


    // Constructor Method
    public IceCream(String flavor, double stockQuantityKG, LocalDate madeAt) {
        this.flavor = flavor;
        this.stockQuantityKG = stockQuantityKG;
        this.madeAt = madeAt;
    }



    // Instance Variables



    // Access Methods (Getters and Setters)
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
    public LocalDate getMadeAt() {
        return madeAt;
    }
    public void setMadeAt(LocalDate madeAt) {
        this.madeAt = madeAt;
    }

}
