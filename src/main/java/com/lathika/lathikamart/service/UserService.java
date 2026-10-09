package com.lathika.lathikamart.service;

import com.lathika.lathikamart.dto.LoginRequestDTO;
import com.lathika.lathikamart.dto.RegisterRequestDTO;
import com.lathika.lathikamart.dto.UserResponseDTO;
import com.lathika.lathikamart.exception.AppException;

import java.util.List;

/**
 * Service interface for user authentication and user management.
 */
public interface UserService {
    UserResponseDTO register(RegisterRequestDTO dto) throws AppException;
    UserResponseDTO login(LoginRequestDTO dto) throws AppException;
    UserResponseDTO getUserById(Long id) throws AppException;
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO updateUserRole(Long userId, String newRole) throws AppException;
}
