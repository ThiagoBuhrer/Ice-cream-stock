package com.icecream.icecreamsstock;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity // This syntax creates an entity that represents a table. This is done by Hibernate/JPA.
// @Entities are automatically transformed from Java objects into tables and SQL in the database.
public class IceCream {

    // Instance Variables
    @Id // Indicates the Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Database generates the ID automatically
    private Long id; // Primary Key (placed right next to @Id)
    private String flavor;
    private double stockQuantityKG;
    private int stockBuckets;
    private LocalDate madeAt;
    private static final double KG_PER_BUCKET = 5.0; // Indicates that 01 ice cream bucket weights 5g
    private static final double KG_PER_CUP = 0.1;

    // Empty Constructor Method (required by Hibernate ORM to create objects)
    public IceCream() {
    }

    // Constructor Method
    public IceCream(String flavor, double stockQuantityKG, int stockBuckets, LocalDate madeAt) {
        this.flavor = flavor;
        this.stockQuantityKG = stockBuckets * KG_PER_BUCKET;
        this.stockBuckets = stockBuckets;
        this.madeAt = madeAt;
    }

    // Access Methods
    public Long getId() {
        return id;
    }
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
    public int getStockBuckets() {
        return stockBuckets;
    }
    public void setStockBuckets(int stockBuckets) {
        this.stockBuckets = stockBuckets;
    }
    public LocalDate getMadeAt() {
        return madeAt;
    }
    public void setMadeAt(LocalDate madeAt) {
        this.madeAt = madeAt;
    }

}
