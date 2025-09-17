package com.CMPE451.finance_project.controller;

import com.CMPE451.finance_project.dto.TransactionWithDetails;
import com.CMPE451.finance_project.model.Transaction;
import com.CMPE451.finance_project.model.TransactionType;
import com.CMPE451.finance_project.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<String> createTransaction(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        Object amountObj = request.get("amount");
        Object categoryIdObj = request.get("categoryId");
        String typeStr = (String) request.get("type");
        String dateStr = (String) request.get("date");

        BigDecimal amount = null;
        if (amountObj instanceof Number) {
            amount = BigDecimal.valueOf(((Number) amountObj).doubleValue());
        } else if (amountObj instanceof String) {
            amount = new BigDecimal((String) amountObj);
        }

        Integer categoryId = null;
        if (categoryIdObj instanceof Number) {
            categoryId = ((Number) categoryIdObj).intValue();
        } else if (categoryIdObj instanceof String) {
            categoryId = Integer.parseInt((String) categoryIdObj);
        }

        TransactionType type = null;
        if (typeStr != null) {
            type = TransactionType.valueOf(typeStr.toUpperCase());
        }

        LocalDate date = null;
        if (dateStr != null) {
            date = LocalDate.parse(dateStr);
        }

        return transactionService.createTransaction(username, amount, categoryId, type, date);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionWithDetails> getTransaction(@PathVariable int transactionId) {
        return transactionService.getTransaction(transactionId);
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<String> updateTransaction(@PathVariable int transactionId,
                                                   @RequestBody Map<String, Object> request) {
        Object amountObj = request.get("amount");
        Object categoryIdObj = request.get("categoryId");
        String typeStr = (String) request.get("type");
        String dateStr = (String) request.get("date");

        BigDecimal amount = null;
        if (amountObj instanceof Number) {
            amount = BigDecimal.valueOf(((Number) amountObj).doubleValue());
        } else if (amountObj instanceof String) {
            amount = new BigDecimal((String) amountObj);
        }

        Integer categoryId = null;
        if (categoryIdObj instanceof Number) {
            categoryId = ((Number) categoryIdObj).intValue();
        } else if (categoryIdObj instanceof String) {
            categoryId = Integer.parseInt((String) categoryIdObj);
        }

        TransactionType type = null;
        if (typeStr != null) {
            type = TransactionType.valueOf(typeStr.toUpperCase());
        }

        LocalDate date = null;
        if (dateStr != null) {
            date = LocalDate.parse(dateStr);
        }

        return transactionService.updateTransaction(transactionId, amount, categoryId, type, date);
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<String> deleteTransaction(@PathVariable int transactionId) {
        return transactionService.deleteTransaction(transactionId);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<Transaction>> getUserTransactions(@PathVariable String username) {
        return transactionService.getUserTransactions(username);
    }

    @GetMapping("/user/{username}/type/{type}")
    public ResponseEntity<List<Transaction>> getUserTransactionsByType(@PathVariable String username,
                                                                      @PathVariable String type) {
        TransactionType transactionType = TransactionType.valueOf(type.toUpperCase());
        return transactionService.getUserTransactionsByType(username, transactionType);
    }

    @GetMapping("/user/{username}/category/{categoryId}")
    public ResponseEntity<List<Transaction>> getUserTransactionsByCategory(@PathVariable String username,
                                                                          @PathVariable int categoryId) {
        return transactionService.getUserTransactionsByCategory(username, categoryId);
    }

    @GetMapping("/user/{username}/daterange")
    public ResponseEntity<List<Transaction>> getUserTransactionsByDateRange(@PathVariable String username,
                                                                           @RequestParam String startDate,
                                                                           @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return transactionService.getUserTransactionsByDateRange(username, start, end);
    }

    @GetMapping("/user/{username}/summary")
    public ResponseEntity<String> getFinancialSummary(@PathVariable String username,
                                                     @RequestParam String startDate,
                                                     @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return transactionService.getFinancialSummary(username, start, end);
    }
}