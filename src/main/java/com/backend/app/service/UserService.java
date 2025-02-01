package com.backend.app.service;

import com.backend.app.dao.UserRepository;
import com.backend.app.dto.UsersResponse;
import com.backend.app.entity.User;
import com.backend.app.exception.ExternalApiException;
import com.backend.app.exception.InvalidSortingOrderException;
import com.backend.app.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final Validator validator;

    @Value("${external.api.url}")
    private String externalApiUrl;

    public UserService(UserRepository userRepository, RestTemplate restTemplate, Validator validator) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.validator = validator;
    }

    /**
     * Load users from the external API and save them in the H2 database.
     */
    @Transactional
    @Retryable(value = { ExternalApiException.class }, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    @Cacheable(value = "users", key = "#root.method.name")
    public void loadUsers() throws ExternalApiException {
        try {
            UsersResponse response = restTemplate.getForObject(externalApiUrl, UsersResponse.class);

            if (response != null && response.getUsers() != null) {
                logger.info("Received users from external API.");

                // List to store valid users
                List<User> validUsers = new ArrayList<>();
                List<String> validationErrors = new ArrayList<>();

                // Iterate through the users and validate each user
                for (User user : response.getUsers()) {
                    // Use @Valid to validate the user object
                    BindingResult result = new BeanPropertyBindingResult(user, "user");
                    // Perform validation using spring validation API
                    validator.validate(user, result);

                    // If there are validation errors, log them
                    if (result.hasErrors()) {
                        StringBuilder errorMessage = new StringBuilder("Validation failed for user: ");
                        result.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append("; "));
                        validationErrors.add(errorMessage.toString());
                    } else {
                        validUsers.add(user);
                    }
                }

                // If there are validation errors, log them and throw an exception
                if (!validationErrors.isEmpty()) {
                    validationErrors.forEach(logger::error);
                    throw new ExternalApiException("User validation failed: " + String.join(" ", validationErrors), null);
                }

                // Save valid users
                if (!validUsers.isEmpty()) {
                    logger.info("Saving valid users to the database...");
                    List<User> saved = userRepository.saveAll(validUsers);
                    logger.info("Saved " + saved.size() + " users to the database.");
                } else {
                    logger.error("No valid users to save.");
                    throw new ExternalApiException("No valid users to save.", null);
                }
            } else {
                logger.error("No users found in the response from the external API.");
                throw new ExternalApiException("No users found in the response from the external API.", null);
            }
        } catch (Exception e) {
            logger.debug("Failed to fetch and load users from the external API.", e);
            throw new ExternalApiException("Failed to fetch and load users from the external API.", e);
        }
    }

    /**
     * Get all users from the H2 database.
     *
     * @return List of users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get users by their role.
     *
     * @param role User role (e.g., "admin")
     * @return List of users with the specified role
     */
    public List<User> getUsersByRole(String role) {
        List<User> users = userRepository.findByRole(role);
        if (users.isEmpty()) {
            throw new UserNotFoundException("No users found with role: " + role);
        }
        return users;
    }

    /**
     * Get users sorted by age.
     *
     * @param order Sorting order ("asc" or "desc")
     * @return List of users sorted by age
     */
    public List<User> getUsersSortedByAge(String order) {
        if ("asc".equalsIgnoreCase(order)) {
            return userRepository.findAllByOrderByAgeAsc();
        } else if ("desc".equalsIgnoreCase(order)) {
            return userRepository.findAllByOrderByAgeDesc();
        } else {
            throw new InvalidSortingOrderException("Invalid sorting order: " + order + ". Use 'asc' or 'desc'.");
        }
    }

    /**
     * Find a user by their ID or SSN.
     *
     * @param idOrSsn ID or SSN
     * @return The user if found, otherwise throws exception
     */
    public User getUserByIdOrSsn(String idOrSsn) {
        try {
            // Attempt to parse ID as a Long
            Long id = Long.parseLong(idOrSsn);
            return userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        } catch (NumberFormatException e) {
            // If not a valid Long, treat it as SSN
            return userRepository.findBySsn(idOrSsn)
                    .orElseThrow(() -> new UserNotFoundException("User not found with SSN: " + idOrSsn));
        }
    }
}


