package com.icecream.icecreamsstock;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Sort;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;


@RestController // Marks this class as a REST API controller
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
                dto.getStockBuckets(),
                java.time.LocalDate.parse(dto.getMadeAt()),
                dto.getIcon()
        );

        iceCream.setIcon(dto.getIcon() != null ? dto.getIcon() : "blank");
        return repository.save(iceCream);
    }


    // POST endpoint
    // Sells 01 cup of icecream (100g per cup)
    // ResponseEntity allows returning a fully controlled HTTP response (status + body) instead of only data (because this endpoint includes business logic and validation beyond simple CRUD operations).
    @PostMapping("/sell")
    public ResponseEntity<?> sellIceCream(@RequestParam String flavor, @RequestParam int cups) { // @RequestParam extracts query parameters from the URL and passes them into the method as arguments.

        // Find ice cream by flavor (research is not case-sensitive)
        IceCream iceCream = repository.findByFlavorIgnoreCase(flavor)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "IceCream flavor not found: " + flavor
                ));


        double KG_PER_CUP = 0.1;
        double totalToSell = cups * KG_PER_CUP;

        // Validate stock
        if (iceCream.getStockQuantityKG() < totalToSell) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("ERROR: Insufficient stock for the selected number of cups. Try again later.");
        }

        // Update stock
        double newStock = iceCream.getStockQuantityKG() - totalToSell;
        iceCream.setStockQuantityKG(newStock);

        // Recalculate buckets (5kg = 1 bucket)
        int newBuckets = (int) (newStock / 5.0);
        iceCream.setStockBuckets(newBuckets);

        // Save update to DB
        repository.save(iceCream);

        return ResponseEntity.ok(cups + " cups of " + flavor + " sold successfully");
    }



    // PUT endpoint
    // Updates an existing IceCream in the database using its ID
    @PutMapping("/icecream/{id}")
    public IceCream updateIceCream(@PathVariable Long id, @RequestBody @Valid IceCreamDTO dto) {
        // PUT needs two different things at the same time: @PathVariable (identifies which icecream) + @RequestBody (receives the new data).
        // We also add @Valid, so that the constraints we chose in IceCreamDTO (intermediary layer) actually work.

        IceCream existingIceCream = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Updates fields
        existingIceCream.setFlavor(dto.getFlavor());
        existingIceCream.setIcon(dto.getIcon());

        // Calculate the difference in buckets and add to existing KG
        int currentBuckets = existingIceCream.getStockBuckets();
        int newBuckets = dto.getStockBuckets();
        int bucketDifference = newBuckets - currentBuckets;

        if (bucketDifference > 0) {
            // Adding buckets: add 5kg per new bucket
            double newKg = existingIceCream.getStockQuantityKG() + (bucketDifference * 5.0);
            existingIceCream.setStockQuantityKG(newKg);
        } else if (bucketDifference < 0) {
            // Removing buckets: subtract 5kg per removed bucket (but don't go below 0)
            double newKg = existingIceCream.getStockQuantityKG() + (bucketDifference * 5.0);
            if (newKg < 0) newKg = 0;
            existingIceCream.setStockQuantityKG(newKg);
        } else {
            // Same number of buckets, just update KG if changed directly
            existingIceCream.setStockQuantityKG(dto.getStockQuantityKG());
        }

        existingIceCream.setStockBuckets(newBuckets);
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


    // DELETE endpoint
    // Removes ALL icecreams from the database
    @DeleteMapping("/icecream/all")
    public ResponseEntity<String> deleteAllIceCreams() {

        if (repository.count() == 0) {
            return ResponseEntity.badRequest()
                    .body("There are no ice creams listed. Try again later.");
        }

        repository.deleteAll();

        return ResponseEntity.ok(
                "All ice cream records were deleted successfully!"
        );
    }


}