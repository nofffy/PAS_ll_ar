package pasrest.api.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pasrest.api.DTOs.CreateUpdateRentDTO;
import pasrest.api.DTOs.RentResponseDTO;
import pasrest.managers.RentManager;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/rents")
public class RentController {

    private final RentManager rentManager;

    public RentController(RentManager rentManager) {
        this.rentManager = rentManager;
    }

    @GetMapping
    public ResponseEntity<List<RentResponseDTO>> getAllRents() {
        return ResponseEntity.ok(rentManager.getRents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentResponseDTO> getRentById(@PathVariable("id") String id) {
        RentResponseDTO rent = rentManager.getRent(id);
        return ResponseEntity.ok(rent);
    }

    @PostMapping
    public ResponseEntity<Void> createRent(@Valid @RequestBody CreateUpdateRentDTO rent) {
        RentResponseDTO createdRent = rentManager.createRent(rent);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdRent.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRent(@PathVariable("id") String id) {
        rentManager.removeRent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/client/{id}/current")
    public ResponseEntity<List<RentResponseDTO>> getCurrentRentsByClientId(@PathVariable("id") String id) {
        return ResponseEntity.ok(rentManager.getCurrentRentsByClientId(id));
    }

    @GetMapping("/client/{id}/past")
    public ResponseEntity<List<RentResponseDTO>> getPastRentsByClientId(@PathVariable("id") String id) {
        return ResponseEntity.ok(rentManager.getPastRentsByClientId(id));
    }

    @GetMapping("/plant/{id}/current")
    public ResponseEntity<List<RentResponseDTO>> getCurrentRentsByPlantId(@PathVariable("id") String id) {
        return ResponseEntity.ok(rentManager.getCurrentRentsByPlantId(id));
    }

    @GetMapping("/plant/{id}/past")
    public ResponseEntity<List<RentResponseDTO>> getPastRentsByPlantId(@PathVariable("id") String id) {
        return ResponseEntity.ok(rentManager.getPastRentsByPlantId(id));
    }
}
