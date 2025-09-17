package com.CMPE451.finance_project.controller;

import com.CMPE451.finance_project.model.User;
import com.CMPE451.finance_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        String password = (String) request.get("password");
        Object budgetObj = request.get("budget");

        BigDecimal budget = null;
        if (budgetObj != null) {
            if (budgetObj instanceof Number) {
                budget = BigDecimal.valueOf(((Number) budgetObj).doubleValue());
            } else if (budgetObj instanceof String) {
                budget = new BigDecimal((String) budgetObj);
            }
        }

        return userService.registerUser(username, password, budget);
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        return userService.loginUser(username, password);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        return userService.getUser(username);
    }

    @PutMapping("/{username}")
    public ResponseEntity<String> updateUser(@PathVariable String username,
                                           @RequestBody Map<String, Object> request) {
        String newPassword = (String) request.get("password");
        Object budgetObj = request.get("budget");

        BigDecimal newBudget = null;
        if (budgetObj != null) {
            if (budgetObj instanceof Number) {
                newBudget = BigDecimal.valueOf(((Number) budgetObj).doubleValue());
            } else if (budgetObj instanceof String) {
                newBudget = new BigDecimal((String) budgetObj);
            }
        }

        return userService.updateUser(username, newPassword, newBudget);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        return userService.deleteUser(username);
    }

    @GetMapping("/{username}/exists")
    public ResponseEntity<Boolean> checkUserExists(@PathVariable String username) {
        return userService.userExists(username);
    }

    @GetMapping("/{username}/available")
    public ResponseEntity<Boolean> checkUsernameAvailable(@PathVariable String username) {
        return userService.isUsernameAvailable(username);
    }

    @PutMapping("/{username}/budget")
    public ResponseEntity<String> updateBudget(@PathVariable String username,
                                             @RequestBody Map<String, Object> request) {
        Object budgetObj = request.get("budget");

        BigDecimal budget = null;
        if (budgetObj != null) {
            if (budgetObj instanceof Number) {
                budget = BigDecimal.valueOf(((Number) budgetObj).doubleValue());
            } else if (budgetObj instanceof String) {
                budget = new BigDecimal((String) budgetObj);
            }
        }

        return userService.updateUserBudget(username, budget);
    }

    @PutMapping("/{username}/password")
    public ResponseEntity<String> changePassword(@PathVariable String username,
                                                @RequestBody Map<String, String> request) {
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");

        return userService.changePassword(username, oldPassword, newPassword);
    }
}