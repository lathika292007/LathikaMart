package com.lathika.lathikamart.service;

import com.lathika.lathikamart.dao.UserDAO;
import com.lathika.lathikamart.dto.LoginRequestDTO;
import com.lathika.lathikamart.dto.RegisterRequestDTO;
import com.lathika.lathikamart.dto.UserResponseDTO;
import com.lathika.lathikamart.exception.AppException;
import com.lathika.lathikamart.exception.AuthException;
import com.lathika.lathikamart.exception.NotFoundException;
import com.lathika.lathikamart.exception.ValidationException;
import com.lathika.lathikamart.model.Role;
import com.lathika.lathikamart.model.User;
import com.lathika.lathikamart.util.PasswordUtil;
import com.lathika.lathikamart.util.ValidationUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of UserService with business logic and validation.
 */
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserResponseDTO register(RegisterRequestDTO dto) throws AppException {
        if (dto == null) {
            throw new ValidationException("Registration data cannot be null.");
        }
        ValidationUtil.requireNonEmpty("Name", dto.getName());
        ValidationUtil.validateEmail(dto.getEmail());
        ValidationUtil.validatePassword(dto.getPassword());

        if (dto.getRole() == null) {
            dto.setRole(Role.BUYER);
        } else if (dto.getRole() == Role.ADMIN) {
            throw new ValidationException("Admin account creation is restricted to system seed.");
        }

        Optional<User> existing = userDAO.findByEmail(dto.getEmail().trim());
        if (existing.isPresent()) {
            throw new ValidationException("User with email '" + dto.getEmail() + "' already exists.");
        }

        User user = new User();
        user.setName(dto.getName().trim());
        user.setEmail(dto.getEmail().trim());
        user.setPasswordHash(PasswordUtil.hashPassword(dto.getPassword()));
        user.setRole(dto.getRole());

        User created = userDAO.create(user);
        return UserResponseDTO.fromEntity(created);
    }

    @Override
    public UserResponseDTO login(LoginRequestDTO dto) throws AppException {
        if (dto == null) {
            throw new ValidationException("Login data cannot be null.");
        }
        ValidationUtil.validateEmail(dto.getEmail());
        ValidationUtil.requireNonEmpty("Password", dto.getPassword());

        Optional<User> userOpt = userDAO.findByEmail(dto.getEmail().trim());
        if (userOpt.isEmpty()) {
            throw new AuthException("Invalid email or password.");
        }

        User user = userOpt.get();
        if (!PasswordUtil.checkPassword(dto.getPassword(), user.getPasswordHash())) {
            throw new AuthException("Invalid email or password.");
        }

        return UserResponseDTO.fromEntity(user);
    }

    @Override
    public UserResponseDTO getUserById(Long id) throws AppException {
        if (id == null || id <= 0) {
            throw new ValidationException("Invalid user ID.");
        }
        User user = userDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
        return UserResponseDTO.fromEntity(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userDAO.findAll().stream()
                .map(UserResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUserRole(Long userId, String newRoleStr) throws AppException {
        if (userId == null || userId <= 0) {
            throw new ValidationException("Invalid user ID.");
        }
        Role newRole;
        try {
            newRole = Role.valueOf(newRoleStr.toUpperCase());
        } catch (Exception e) {
            throw new ValidationException("Invalid role: " + newRoleStr);
        }

        User user = userDAO.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));

        user.setRole(newRole);
        boolean updated = userDAO.update(user);
        if (!updated) {
            throw new AppException("Failed to update user role in database.");
        }
        return UserResponseDTO.fromEntity(user);
    }
}
