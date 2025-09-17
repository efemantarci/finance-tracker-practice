package com.CMPE451.finance_project.model;

import java.math.BigDecimal;

public class User {
    private String username;
    private String passwordHash;
    private BigDecimal budget;

    public User() {
    }

    public User(String username, String passwordHash, BigDecimal budget) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.budget = budget;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }
}
