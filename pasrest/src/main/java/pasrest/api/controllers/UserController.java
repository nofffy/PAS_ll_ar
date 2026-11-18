package pasrest.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pasrest.api.DTOs.CreateUpdateUserDTO;
import pasrest.api.DTOs.UserResponseDTO;
import jakarta.validation.Valid;
import pasrest.managers.UserManager;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserManager userManager;

    public UserController(UserManager userManager) {
        this.userManager = userManager;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers(
            @RequestParam(value = "loginPattern", required = false) String loginPattern,
            @RequestParam(value = "active", required = false) Boolean isActive) {
        List<UserResponseDTO> users;
        if (loginPattern != null && !loginPattern.isBlank()) {
            users = userManager.getUsersByLogin(loginPattern);
        } else if (isActive != null) {
            users = isActive ? userManager.findAllActiveUsers() : userManager.findAllInactiveUsers();
        } else {
            users = userManager.getAllUsers();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("id") String id) {
        return ResponseEntity.ok(userManager.getUser(id));
    }

    @GetMapping("/login/{login}")
    public ResponseEntity<UserResponseDTO> getUserByExactLogin(@PathVariable("login") String login) {
        return ResponseEntity.ok(userManager.getUserByLogin(login));
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUpdateUserDTO user) {
        UserResponseDTO createdUser = userManager.createUser(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("id") String id, @Valid @RequestBody CreateUpdateUserDTO userDTO) {
        userManager.updateUser(id, userDTO);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable("id") String id) {
        userManager.activateUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable("id") String id) {
        userManager.deactivateUser(id);
        return ResponseEntity.ok().build();
    }
}