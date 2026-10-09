package com.lathika.lathikamart;

import com.lathika.lathikamart.dao.UserDAO;
import com.lathika.lathikamart.dto.LoginRequestDTO;
import com.lathika.lathikamart.dto.RegisterRequestDTO;
import com.lathika.lathikamart.dto.UserResponseDTO;
import com.lathika.lathikamart.exception.AppException;
import com.lathika.lathikamart.exception.AuthException;
import com.lathika.lathikamart.exception.ValidationException;
import com.lathika.lathikamart.model.Role;
import com.lathika.lathikamart.model.User;
import com.lathika.lathikamart.service.UserService;
import com.lathika.lathikamart.service.UserServiceImpl;
import com.lathika.lathikamart.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Service business rule validation tests using Mockito.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userDAO);
    }

    @Test
    public void testRegisterSuccess() throws AppException {
        RegisterRequestDTO dto = new RegisterRequestDTO("Alice", "alice@example.com", "password123", Role.BUYER);

        when(userDAO.findByEmail("alice@example.com")).thenReturn(Optional.empty());
        when(userDAO.create(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(10L);
            return u;
        });

        UserResponseDTO response = userService.register(dto);
        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Alice", response.getName());
        assertEquals("alice@example.com", response.getEmail());
    }

    @Test
    public void testRegisterDuplicateEmailThrowsValidationException() {
        RegisterRequestDTO dto = new RegisterRequestDTO("Bob", "existing@example.com", "password123", Role.BUYER);
        when(userDAO.findByEmail("existing@example.com")).thenReturn(Optional.of(new User()));

        assertThrows(ValidationException.class, () -> userService.register(dto));
    }

    @Test
    public void testHashPassword() {
        String hash = PasswordUtil.hashPassword("password123");
        System.out.println("ACTUAL_BCRYPT_HASH_FOR_PASSWORD123: " + hash);
        assertTrue(PasswordUtil.checkPassword("password123", hash));
    }

    @Test
    public void testLoginInvalidPasswordThrowsAuthException() {
        LoginRequestDTO dto = new LoginRequestDTO("john@example.com", "wrongpass");
        User user = new User(1L, "John", "john@example.com", PasswordUtil.hashPassword("correctpass"), Role.BUYER, null);

        when(userDAO.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        assertThrows(AuthException.class, () -> userService.login(dto));
    }
}
