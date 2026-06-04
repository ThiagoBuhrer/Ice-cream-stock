package com.icecream.icecreamsstock;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// JpaRepository automatically provides basic CRUD operations:
// save()       -> saves objects into the database
// findById()   -> searches for a record by its ID
// findAll()    -> returns all records from the table
// deleteById() -> deletes a record by its ID
// existsById(id) -> check if an ID exists in the repository
// deleteAll() -> deletes all the records
// This avoids writing SQL manually for common operations.

public interface IceCreamRepository extends JpaRepository<IceCream, Long> {
    // The interface looks empty here, but Spring automatically generates all the required methods at runtime, so it is not actually empty.
    // JpaRepository already provides a full implementation behind the scenes.

    // Custom query method generated automatically by Spring Data JPA
    // Spring reads this and automatically generates: SELECT * FROM ice_cream WHERE LOWER(flavor) = LOWER(?)
    Optional<IceCream> findByFlavorIgnoreCase(String flavor);

}
