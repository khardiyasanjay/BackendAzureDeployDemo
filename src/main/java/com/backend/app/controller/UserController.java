package com.backend.app.controller;

import com.backend.app.entity.User;
import com.backend.app.exception.ExternalApiException;
import com.backend.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/load")
    @Operation(summary = "Load users from external API and save to the database")
    public ResponseEntity<String> loadUsers() throws ExternalApiException {
        userService.loadUsers();
        return ResponseEntity.ok("Users loaded successfully.");
    }

    @GetMapping
    @Operation(summary = "Get all users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/role/{role}")
    @Operation(summary = "Get users by role")
    public List<User> getUsersByRole(@PathVariable @Pattern(
            regexp = "^(admin|user|manager|moderator)$",
            message = "Role must be one of the following: admin, user, manager"
    )  String role) {
        return userService.getUsersByRole(role);
    }

    @GetMapping("/sort")
    @Operation(summary = "Get users sorted by age")
    public List<User> getUsersSortedByAge(@RequestParam(defaultValue = "asc") String order) {
        return userService.getUsersSortedByAge(order);
    }

    @GetMapping("/{idOrSsn}")
    @Operation(summary = "Get user by ID or SSN")
    public User getUserByIdOrSsn(@PathVariable String idOrSsn) {
        return userService.getUserByIdOrSsn(idOrSsn);
    }
}

