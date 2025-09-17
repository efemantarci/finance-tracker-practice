package com.CMPE451.finance_project.controller;

import com.CMPE451.finance_project.model.TransactionCategory;
import com.CMPE451.finance_project.service.TransactionCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class TransactionCategoryController {

    @Autowired
    private TransactionCategoryService transactionCategoryService;

    @PostMapping
    public ResponseEntity<String> createCategory(@RequestBody Map<String, String> request) {
        String categoryName = request.get("name");
        return transactionCategoryService.createCategory(categoryName);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<TransactionCategory> getCategory(@PathVariable int categoryId) {
        return transactionCategoryService.getCategory(categoryId);
    }

    @GetMapping("/name/{categoryName}")
    public ResponseEntity<TransactionCategory> getCategoryByName(@PathVariable String categoryName) {
        return transactionCategoryService.getCategoryByName(categoryName);
    }

    @GetMapping
    public ResponseEntity<List<TransactionCategory>> getAllCategories() {
        return transactionCategoryService.getAllCategories();
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<String> updateCategory(@PathVariable int categoryId,
                                               @RequestBody Map<String, String> request) {
        String newCategoryName = request.get("name");
        return transactionCategoryService.updateCategory(categoryId, newCategoryName);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable int categoryId) {
        return transactionCategoryService.deleteCategory(categoryId);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TransactionCategory>> searchCategories(@RequestParam String term) {
        return transactionCategoryService.searchCategories(term);
    }

    @GetMapping("/{categoryId}/exists")
    public ResponseEntity<Boolean> checkCategoryExists(@PathVariable int categoryId) {
        return transactionCategoryService.categoryExists(categoryId);
    }

    @GetMapping("/name/{categoryName}/exists")
    public ResponseEntity<Boolean> checkCategoryNameExists(@PathVariable String categoryName) {
        return transactionCategoryService.categoryNameExists(categoryName);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<TransactionCategory>> getCategoriesUsedByUser(@PathVariable String username) {
        return transactionCategoryService.getCategoriesUsedByUser(username);
    }

    @GetMapping("/unused")
    public ResponseEntity<List<TransactionCategory>> getUnusedCategories() {
        return transactionCategoryService.getUnusedCategories();
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getCategoryCount() {
        return transactionCategoryService.getCategoryCount();
    }
}