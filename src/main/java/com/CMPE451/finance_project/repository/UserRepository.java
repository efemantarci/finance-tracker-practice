package com.CMPE451.finance_project.repository;

import com.CMPE451.finance_project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Get user by username
    public Optional<User> getUser(String username){
        try {
            User user = jdbcTemplate.queryForObject(
                    "SELECT username, password_hash, budget FROM users WHERE username = ?",
                    (rs, rowNum) -> new User(
                            rs.getString("username"),
                            rs.getString("password_hash"),
                            rs.getBigDecimal("budget")
                    ),
                    username
            );
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // Create new user
    public boolean createUser(User user) {
        try {
            int result = jdbcTemplate.update(
                    "INSERT INTO users (username, password_hash, budget) VALUES (?, ?, ?)",
                    user.getUsername(),
                    user.getPasswordHash(),
                    user.getBudget()
            );
            return result > 0;
        } catch (Exception e) {
            return false; // Username already exists or other error
        }
    }

    // Update user budget
    public boolean updateBudget(String username, BigDecimal newBudget) {
        int result = jdbcTemplate.update(
                "UPDATE users SET budget = ? WHERE username = ?",
                newBudget,
                username
        );
        return result > 0;
    }

    // Update password
    public boolean updatePasswordHash(String username, String newPasswordHash) {
        int result = jdbcTemplate.update(
                "UPDATE users SET password_hash = ? WHERE username = ?",
                newPasswordHash,
                username
        );
        return result > 0;
    }

    // Check if username exists
    public boolean userExists(String username) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE username = ?",
                Integer.class,
                username
        );
        return count != null && count > 0;
    }

    // Delete user
    public boolean deleteUser(String username) {
        int result = jdbcTemplate.update(
                "DELETE FROM users WHERE username = ?",
                username
        );
        return result > 0;
    }

    // Get all users
    public List<User> getAllUsers() {
        return jdbcTemplate.query(
                "SELECT username, password_hash, budget FROM users",
                (rs, rowNum) -> new User(
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getBigDecimal("budget")
                )
        );
    }


}
