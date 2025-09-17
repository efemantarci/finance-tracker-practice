package com.CMPE451.finance_project.service;

import com.CMPE451.finance_project.model.TransactionCategory;
import com.CMPE451.finance_project.repository.TransactionCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionCategoryService {

    @Autowired
    private TransactionCategoryRepository transactionCategoryRepository;

    // Create new category
    public ResponseEntity<String> createCategory(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Category name cannot be empty");
        }

        if (transactionCategoryRepository.categoryExistsByName(categoryName)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Category already exists");
        }

        TransactionCategory category = new TransactionCategory(categoryName);
        boolean created = transactionCategoryRepository.createCategory(category);

        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Category created successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create category");
        }
    }

    // Get category by ID
    public ResponseEntity<TransactionCategory> getCategory(int categoryId) {
        Optional<TransactionCategory> category = transactionCategoryRepository.getCategory(categoryId);
        if (category.isPresent()) {
            return ResponseEntity.ok(category.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get category by name
    public ResponseEntity<TransactionCategory> getCategoryByName(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<TransactionCategory> category = transactionCategoryRepository.getCategoryByName(categoryName);
        if (category.isPresent()) {
            return ResponseEntity.ok(category.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get all categories
    public ResponseEntity<List<TransactionCategory>> getAllCategories() {
        List<TransactionCategory> categories = transactionCategoryRepository.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Update category
    public ResponseEntity<String> updateCategory(int categoryId, String newCategoryName) {
        if (newCategoryName == null || newCategoryName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Category name cannot be empty");
        }

        if (!transactionCategoryRepository.categoryExists(categoryId)) {
            return ResponseEntity.notFound().build();
        }

        // Check if new name already exists (but not for the same category)
        Optional<TransactionCategory> existingCategory = transactionCategoryRepository.getCategoryByName(newCategoryName);
        if (existingCategory.isPresent() && existingCategory.get().getCategoryId() != categoryId) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Category name already exists");
        }

        TransactionCategory category = new TransactionCategory(categoryId, newCategoryName);
        boolean updated = transactionCategoryRepository.updateCategory(category);

        if (updated) {
            return ResponseEntity.ok("Category updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update category");
        }
    }

    // Delete category
    public ResponseEntity<String> deleteCategory(int categoryId) {
        if (!transactionCategoryRepository.categoryExists(categoryId)) {
            return ResponseEntity.notFound().build();
        }

        boolean deleted = transactionCategoryRepository.deleteCategory(categoryId);

        if (deleted) {
            return ResponseEntity.ok("Category deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete category");
        }
    }

    // Search categories by name
    public ResponseEntity<List<TransactionCategory>> searchCategories(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<TransactionCategory> categories = transactionCategoryRepository.searchCategoriesByName(searchTerm);
        return ResponseEntity.ok(categories);
    }

    // Check if category exists
    public ResponseEntity<Boolean> categoryExists(int categoryId) {
        boolean exists = transactionCategoryRepository.categoryExists(categoryId);
        return ResponseEntity.ok(exists);
    }

    // Check if category name exists
    public ResponseEntity<Boolean> categoryNameExists(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        boolean exists = transactionCategoryRepository.categoryExistsByName(categoryName);
        return ResponseEntity.ok(exists);
    }

    // Get categories used by a user
    public ResponseEntity<List<TransactionCategory>> getCategoriesUsedByUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<TransactionCategory> categories = transactionCategoryRepository.getCategoriesUsedByUser(username);
        return ResponseEntity.ok(categories);
    }

    // Get unused categories
    public ResponseEntity<List<TransactionCategory>> getUnusedCategories() {
        List<TransactionCategory> categories = transactionCategoryRepository.getUnusedCategories();
        return ResponseEntity.ok(categories);
    }

    // Get category count
    public ResponseEntity<Integer> getCategoryCount() {
        int count = transactionCategoryRepository.getCategoryCount();
        return ResponseEntity.ok(count);
    }
}