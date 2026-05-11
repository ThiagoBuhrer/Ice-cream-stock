import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository automatically provides basic CRUD operations:
// save()       -> saves objects into the database
// findAll()    -> returns all records from the table
// findById()   -> searches for a record by its ID
// deleteById() -> deletes a record by its ID
// This avoids writing SQL manually for common operations.

public interface IceCreamRepository extends JpaRepository<IceCream, Long> {

}
