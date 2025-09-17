package com.CMPE451.finance_project.model;

public class TransactionCategory {
    private int categoryId;
    private String name;

    public TransactionCategory(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public TransactionCategory(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
