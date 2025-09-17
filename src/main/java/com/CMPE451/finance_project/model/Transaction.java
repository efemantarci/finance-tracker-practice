package com.CMPE451.finance_project.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {
    private int transactionId;
    private String user;
    private BigDecimal amount;
    private int categoryId;
    private TransactionType transactionType;
    private LocalDate transactionDate;

    // Constructor
    public Transaction(int transactionId, String user, BigDecimal amount,
                       int categoryId, TransactionType transactionType, LocalDate transactionDate) {
        this.transactionId = transactionId;
        this.user = user;
        this.amount = amount;
        this.categoryId = categoryId;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
    }

    // Constructor without ID (for inserts)
    public Transaction(String user, BigDecimal amount, int categoryId,
                       TransactionType transactionType, LocalDate transactionDate) {
        this.user = user;
        this.amount = amount;
        this.categoryId = categoryId;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
    }

    // Getters and setters
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }

    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }
}
