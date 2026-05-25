package com.icecream.icecreamsstock;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Sort;
import jakarta.validation.Valid;


// Marks this class as a REST API controller
@RestController
public class IceCreamController {


    // Repository field (Dependency)
    // Responsible for database operations (the bridge between the Controller and the database layer)
    private final IceCreamRepository repository;


    // Constructor injection (Dependency Injection)
    // Spring automatically injects IceCreamRepository to allow database access without manual instantiation
    // "To inject" means that Spring creates the object for you and automatically delivers it where it's needed.
    public IceCreamController(IceCreamRepository repository) {
        this.repository = repository;
    }


    // GetMapping test
    // GetMapping tells Spring: "this method responds to HTTP GET requests"
    // ("/icecream") is the endpoint (URL path) that activates this method.
    @GetMapping("/icecream/test")
    public String testRoute() {

        // Returns a simple text response
        return "Ice Cream API is working!";

    }


    // GET endpoint
    // Retrieves all IceCream records from the database and returns them as JSON.
    @GetMapping("/icecream")
    public List<IceCream> getAllIceCreams() {

        return repository.findAll(Sort.by("id"));
    }


    // GET endpoint (with path variable)
    // Search for a specific IceCream from using its ID
    @GetMapping("/icecream/{id}")
    public IceCream getIceCreamById(@PathVariable Long id) { // @PathVariable binds the value from the URL path (e.g. /icecream/1) to the method parameter "id"

        // Finds IceCream or throws error if not found
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "IceCream with id " + id + " not found" )); // Spring-specific exception. Allows explicit control of HTTP status codes (e.g. 404 Not Found)
    }


    // POST endpoint
    // Receives an IceCream object in JSON format and saves it into the database
    @PostMapping("/icecream")
    public IceCream createIceCream(@RequestBody @Valid IceCreamDTO dto) {
        // @RequestBody converts the JSON data from the HTTP request body into a Java object.
        // @Valid allow the constraints we chose in IceCreamDTO (intermediary layer) to actually work.

        IceCream iceCream = new IceCream(
                dto.getFlavor(),
                dto.getStockQuantityKG(),
                java.time.LocalDate.parse(dto.getMadeAt())
        );

        return repository.save(iceCream);
    }


    // PUT endpoint
    // Updates an existing IceCream in the database using its ID
    @PutMapping("/icecream/{id}")
    public IceCream updateIceCream(@PathVariable Long id, @RequestBody @Valid IceCreamDTO dto) {
        // PUT needs two different things at the same time: @PathVariable (identifies which icecream) + @RequestBody (receives the new data).
        // We also add @Valid, so that the constraints we chose in IceCreamDTO (intermediary layer) actually work.

        IceCream existingIceCream = repository.findById(id).orElse(null);

        if (existingIceCream == null) {
            return null;
        }

        // Updates fields
        existingIceCream.setFlavor(dto.getFlavor());
        existingIceCream.setStockQuantityKG(dto.getStockQuantityKG());
        existingIceCream.setMadeAt(java.time.LocalDate.parse(dto.getMadeAt()));

        return repository.save(existingIceCream);
    }


    // DELETE endpoint
    // Removes an IceCream from the database using its ID
    @DeleteMapping("/icecream/{id}")
    public String deleteIceCream(@PathVariable Long id) {

        // Checks if chosen id exists, then delete.
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "IceCream with id " + id + " was deleted successfully";
        }

        return "IceCream with id " + id + " was not found.";
    }


}