package com.backend.app.service;

import com.backend.app.dao.UserRepository;
import com.backend.app.dto.UsersResponse;
import com.backend.app.entity.User;
import com.backend.app.exception.ExternalApiException;
import com.backend.app.exception.InvalidSortingOrderException;
import com.backend.app.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.Validator;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private RestTemplate restTemplate;
    private Validator validator;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        restTemplate = mock(RestTemplate.class);
        validator = mock(Validator.class);
        userService = new UserService(userRepository, restTemplate, validator);

        ReflectionTestUtils.setField(userService, "externalApiUrl", "https://mocked-url.com");

    }

    @Test
    void loadUsers_Success() throws ExternalApiException {
        UsersResponse mockResponse = new UsersResponse();
        mockResponse.setUsers(Arrays.asList(
                new User(1L, "John", "Doe", null, 30, "male", "john.doe@example.com", "1234567890", "john", "password", null, null, null, 180.0, 75.0, null, null, null, null, null, null, null, null, null, null, "user", null),
                new User(2L, "Jane", "Smith", null, 25, "female", "jane.smith@example.com", "0987654321", "jane", "password", null, null, null, 170.0, 65.0, null, null, null, null, null, null, null, null, null, null, "admin", null)
        ));

        when(restTemplate.getForObject(eq("https://mocked-url.com"), eq(UsersResponse.class)))
                .thenReturn(mockResponse);

        when(userRepository.saveAll(Mockito.anyList())).thenReturn(mockResponse.getUsers());

        userService.loadUsers();
        System.out.println("here3");
        verify(userRepository, times(1)).saveAll(Mockito.anyList());
    }

    @Test
    void loadUsers_ThrowsExternalApiExceptionWhenNoUsersReturned() {
        when(restTemplate.getForObject(Mockito.anyString(), eq(UsersResponse.class))).thenReturn(new UsersResponse());

        Exception exception = assertThrows(ExternalApiException.class, userService::loadUsers);
        System.out.println("msg :" + exception.getMessage());
        assertTrue(exception.getMessage().contains("Failed to fetch and load users from the external API."));
    }

    @Test
    void getAllUsers_ReturnsUsers() {
        List<User> mockUsers = Arrays.asList(
                new User(1L, "John", "Doe", null, 30, "male", "john.doe@example.com", "1234567890", "john", "password", null, null, null, 180.0, 75.0, null, null, null, null, null, null, null, null, null, null, "user", null)
        );
        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUsersByRole_ReturnsUsers() {
        String role = "admin";
        List<User> mockUsers = Arrays.asList(
                new User(1L, "Jane", "Smith", null, 25, "female", "jane.smith@example.com", "0987654321", "jane", "password", null, null, null, 170.0, 65.0, null, null, null, null, null, null, null, null, null, null, role, null)
        );
        when(userRepository.findByRole(role)).thenReturn(mockUsers);

        List<User> users = userService.getUsersByRole(role);

        assertEquals(1, users.size());
        assertEquals(role, users.get(0).getRole());
        verify(userRepository, times(1)).findByRole(role);
    }

    @Test
    void getUsersByRole_ThrowsUserNotFoundException() {
        String role = "nonexistent";
        when(userRepository.findByRole(role)).thenReturn(List.of());

        Exception exception = assertThrows(UserNotFoundException.class, () -> userService.getUsersByRole(role));
        assertTrue(exception.getMessage().contains("No users found with role"));
    }

    @Test
    void getUsersSortedByAge_ThrowsInvalidSortingOrderException() {
        Exception exception = assertThrows(InvalidSortingOrderException.class, () -> userService.getUsersSortedByAge("invalid"));
        assertTrue(exception.getMessage().contains("Invalid sorting order"));
    }

//    @Test
//    void getUserByIdOrSsn_ReturnsUserById() {
//        Long id = 1L;
//        User mockUser = new User(id, "John", "Doe", null, 30, "male", "john.doe@example.com", "1234567890", "john", "password", null, null, null, 180.0, 75.0, null, null, null, null, null, null, null, null, null, null, "user", null);
//        when(userRepository.findById(id)).thenReturn(Optional.of(mockUser));
//
//        User user = userService.getUserByIdOrSsn(String.valueOf(id));
//
//        assertEquals(id, user.getId());
//        verify(userRepository, times(1)).findById(id);
//    }
//
//    @Test
//    void getUserByIdOrSsn_ThrowsUserNotFoundExceptionForInvalidId() {
//        Long id = 99L;
//        when(userRepository.findById(id)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(UserNotFoundException.class, () -> userService.getUserByIdOrSsn(String.valueOf(id)));
//        assertTrue(exception.getMessage().contains("User not found with ID"));
//    }

    @Test
    void getUserByIdOrSsn_ValidId() {
        // Arrange
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        User result = userService.getUserByIdOrSsn(String.valueOf(userId));

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).findBySsn(anyString());
    }

    @Test
    void getUserByIdOrSsn_ValidSsn() {
        // Arrange
        String ssn = "123-45-6789";
        User mockUser = new User();
        mockUser.setSsn(ssn);
        when(userRepository.findBySsn(ssn)).thenReturn(Optional.of(mockUser));

        // Act
        User result = userService.getUserByIdOrSsn(ssn);

        // Assert
        assertNotNull(result);
        assertEquals(ssn, result.getSsn());
        verify(userRepository, times(1)).findBySsn(ssn);
        verify(userRepository, never()).findById(anyLong());
    }

    @Test
    void getUserByIdOrSsn_IdNotFound() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserByIdOrSsn(String.valueOf(userId));
        });

        assertEquals("User not found with ID: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).findBySsn(anyString());
    }

    @Test
    void getUserByIdOrSsn_SsnNotFound() {
        // Arrange
        String ssn = "123-45-6789";
        when(userRepository.findBySsn(ssn)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserByIdOrSsn(ssn);
        });

        assertEquals("User not found with SSN: 123-45-6789", exception.getMessage());
        verify(userRepository, times(1)).findBySsn(ssn);
        verify(userRepository, never()).findById(anyLong());
    }

    @Test
    void getUserByIdOrSsn_InvalidInput() {
        // Arrange
        String invalidInput = "invalid";

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserByIdOrSsn(invalidInput);
        });

        assertEquals("User not found with SSN: invalid", exception.getMessage());
        verify(userRepository, times(1)).findBySsn(invalidInput);
        verify(userRepository, never()).findById(anyLong());
    }

//    @Test
//    void getUserByIdOrSsn_NullInput() {
//        // Act & Assert
//        assertThrows(NullPointerException.class, () -> {
//            userService.getUserByIdOrSsn(null);
//        });
//
//        verify(userRepository, never()).findById(anyLong());
//        verify(userRepository, never()).findBySsn(anyString());
//    }
}

