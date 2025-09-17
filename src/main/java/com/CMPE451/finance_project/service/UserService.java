package com.CMPE451.finance_project.service;

import com.CMPE451.finance_project.model.User;
import com.CMPE451.finance_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Register new user
    public ResponseEntity<String> registerUser(String username, String password, BigDecimal initialBudget) {
        // Validate input
        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Username cannot be empty");
        }
        if (password == null || password.length() < 6) {
            return ResponseEntity.badRequest().body("Password must be at least 6 characters");
        }

        // Check if user already exists
        if (userRepository.userExists(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        // Hash password (you should implement proper hashing)
        String hashedPassword = hashPassword(password);

        // Create user
        User newUser = new User(username, hashedPassword, initialBudget != null ? initialBudget : BigDecimal.ZERO);
        boolean created = userRepository.createUser(newUser);

        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user");
        }
    }

    // Login user
    public ResponseEntity<User> loginUser(String username, String password) {
        if (username == null || password == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<User> userOpt = userRepository.getUser(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userOpt.get();
        if (verifyPassword(password, user.getPasswordHash())) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // Get user profile
    public ResponseEntity<User> getUserProfile(String username) {
        Optional<User> userOpt = userRepository.getUser(username);
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update budget
    public ResponseEntity<String> updateUserBudget(String username, BigDecimal newBudget) {
        if (newBudget == null || newBudget.compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.badRequest().body("Budget must be non-negative");
        }

        if (!userRepository.userExists(username)) {
            return ResponseEntity.notFound().build();
        }

        boolean updated = userRepository.updateBudget(username, newBudget);
        if (updated) {
            return ResponseEntity.ok("Budget updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update budget");
        }
    }

    // Change password
    public ResponseEntity<String> changePassword(String username, String oldPassword, String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            return ResponseEntity.badRequest().body("New password must be at least 6 characters");
        }

        // Verify old password first
        ResponseEntity<User> loginResult = loginUser(username, oldPassword);
        if (loginResult.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Current password is incorrect");
        }

        String newHashedPassword = hashPassword(newPassword);
        boolean updated = userRepository.updatePasswordHash(username, newHashedPassword);

        if (updated) {
            return ResponseEntity.ok("Password changed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to change password");
        }
    }

    // Check if username is available
    public ResponseEntity<Boolean> isUsernameAvailable(String username) {
        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        boolean available = !userRepository.userExists(username);
        return ResponseEntity.ok(available);
    }

    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    // Proper password verification with BCrypt
    private boolean verifyPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
