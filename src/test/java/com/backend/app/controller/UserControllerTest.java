package com.backend.app.controller;

import com.backend.app.entity.User;
import com.backend.app.exception.ExternalApiException;
import com.backend.app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    void loadUsers_Success() throws ExternalApiException {
        doNothing().when(userService).loadUsers();

        ResponseEntity<String> response = userController.loadUsers();

        assertEquals("Users loaded successfully.", response.getBody());
        verify(userService, times(1)).loadUsers();
    }

    @Test
    void getAllUsers_Success() {
        List<User> mockUsers = Arrays.asList(
                new User(1L, "John", "Doe", null, 30, "male", "john.doe@example.com", "1234567890", "john", "password", null, null, null, 180.0, 75.0, null, null, null, null, null, null, null, null, null, null, "user", null),
                new User(2L, "Jane", "Smith", null, 25, "female", "jane.smith@example.com", "0987654321", "jane", "password", null, null, null, 170.0, 65.0, null, null, null, null, null, null, null, null, null, null, "admin", null)
        );

        when(userService.getAllUsers()).thenReturn(mockUsers);

        List<User> users = userController.getAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUsersByRole_Success() {
        String role = "admin";
        List<User> mockUsers = List.of(
                new User(2L, "Jane", "Smith", null, 25, "female", "jane.smith@example.com", "0987654321", "jane", "password", null, null, null, 170.0, 65.0, null, null, null, null, null, null, null, null, null, null, "admin", null)
        );

        when(userService.getUsersByRole(role)).thenReturn(mockUsers);

        List<User> users = userController.getUsersByRole(role);

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("Jane", users.get(0).getFirstName());
        verify(userService, times(1)).getUsersByRole(role);
    }

    @Test
    void getUsersSortedByAge_Success() {
        String order = "asc";
        List<User> mockUsers = List.of(
                new User(1L, "John", "Doe", null, 30, "male", "john.doe@example.com", "1234567890", "john", "password", null, null, null, 180.0, 75.0, null, null, null, null, null, null, null, null, null, null, "user", null),
                new User(2L, "Jane", "Smith", null, 25, "female", "jane.smith@example.com", "0987654321", "jane", "password", null, null, null, 170.0, 65.0, null, null, null, null, null, null, null, null, null, null, "admin", null)
        );

        when(userService.getUsersSortedByAge(order)).thenReturn(mockUsers);

        List<User> users = userController.getUsersSortedByAge(order);

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals(25, users.get(1).getAge());
        verify(userService, times(1)).getUsersSortedByAge(order);
    }

    @Test
    void getUserByIdOrSsn_Success() {
        String idOrSsn = "1";
        User mockUser = new User(1L, "John", "Doe", null, 30, "male", "john.doe@example.com", "1234567890", "john", "password", null, null, null, 180.0, 75.0, null, null, null, null, null, null, null, null, null, null, "user", null);

        when(userService.getUserByIdOrSsn(idOrSsn)).thenReturn(mockUser);

        User user = userController.getUserByIdOrSsn(idOrSsn);

        assertNotNull(user);
        assertEquals("John", user.getFirstName());
        verify(userService, times(1)).getUserByIdOrSsn(idOrSsn);
    }
}

