package com.CMPE451.finance_project.dto;

import com.CMPE451.finance_project.model.Transaction;
import com.CMPE451.finance_project.model.TransactionCategory;
import com.CMPE451.finance_project.model.User;

public class TransactionWithDetails {
    private Transaction transaction;
    private User user;
    private TransactionCategory category;

    public TransactionWithDetails(Transaction transaction, User user, TransactionCategory category) {
        this.transaction = transaction;
        this.user = user;
        this.category = category;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TransactionCategory getCategory() {
        return category;
    }

    public void setCategory(TransactionCategory category) {
        this.category = category;
    }
}
