package com.icecream.icecreamsstock;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// JpaRepository automatically provides basic CRUD operations:
// save()       -> saves objects into the database) -> CREATE / UPDATE
// findById()   -> searches for a record by its ID -> READ
// findAll()    -> returns all records from the table -> READ
// deleteById() -> deletes a record by its ID -> DELETE
// existsById(id) -> check if an ID exists in the repository -> READ
// deleteAll() -> deletes all the records -> DELETE
// This avoids writing SQL manually for common operations

public interface IceCreamRepository extends JpaRepository<IceCream, Long> {
    // The interface looks empty here, but Spring automatically generates all the required methods at runtime, so it is not actually empty.
    // JpaRepository already provides a full implementation behind the scenes.

    // Custom query method generated automatically by Spring Data JPA
    // Spring reads this and automatically generates: SELECT * FROM ice_cream WHERE LOWER(flavor) = LOWER(?)
    Optional<IceCream> findByFlavorIgnoreCase(String flavor);

}
