package com.CMPE451.finance_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    String dummyQuery(){
        try {
            jdbcTemplate.query("SELECT * FROM users WHERE username = ?",
                    (rs, rowNum) -> {
                        System.out.println("Found user: " + rs.getString("username"));
                        return rs.getString("username");
                    },
                    "dummy");
            return "Database connection successful";
        } catch (Exception e) {
            return "Database error: " + e.getMessage();
        }
    }
}
