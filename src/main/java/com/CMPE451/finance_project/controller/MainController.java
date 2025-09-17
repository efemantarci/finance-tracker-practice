package com.CMPE451.finance_project.controller;

import com.CMPE451.finance_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    UserService userService;

    @GetMapping("/")
    String dummyQuery(){
        return "dummy response";
    }
    @GetMapping("/test1")
    String createUser(){
        userService.registerUser("testuser","dummypass",null);
        return "created user";
    }
    @GetMapping("/test2")
    String testUser(){
        userService.loginUser("testuser","dummypass");
        return "ovyee";
    }
}
