package com.CMPE451.finance_project.service;

import com.CMPE451.finance_project.dto.TransactionWithDetails;
import com.CMPE451.finance_project.model.Transaction;
import com.CMPE451.finance_project.model.TransactionCategory;
import com.CMPE451.finance_project.model.TransactionType;
import com.CMPE451.finance_project.model.User;
import com.CMPE451.finance_project.repository.TransactionCategoryRepository;
import com.CMPE451.finance_project.repository.TransactionRepository;
import com.CMPE451.finance_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TransactionCategoryRepository transactionCategoryRepository;
    @Autowired
    TransactionRepository transactionRepository;

    public ResponseEntity<TransactionWithDetails> getTransaction(int transactionId) {
        Optional<Transaction> transactionOpt = transactionRepository.getTransaction(transactionId);
        if (transactionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Transaction t = transactionOpt.get();
        Optional<User> userOpt = userRepository.getUser(t.getUser());
        Optional<TransactionCategory> category = transactionCategoryRepository.getCategory(t.getCategoryId());
        // Create response object
        TransactionWithDetails res = new TransactionWithDetails(
                t,
                userOpt.orElse(null),
                category.orElse(null)
        );

        return ResponseEntity.ok(res);
    }

    // Create new transaction
    public ResponseEntity<String> createTransaction(String username, BigDecimal amount,
                                                    int categoryId, TransactionType type, LocalDate date) {
        // Validate input
        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Username cannot be empty");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body("Amount must be positive");
        }
        if (date == null) {
            return ResponseEntity.badRequest().body("Date cannot be null");
        }

        // Check if user exists
        if (!userRepository.userExists(username)) {
            return ResponseEntity.badRequest().body("User does not exist");
        }

        // Check if category exists
        if (!transactionCategoryRepository.categoryExists(categoryId)) {
            return ResponseEntity.badRequest().body("Category does not exist");
        }

        // Create transaction
        Transaction transaction = new Transaction(username, amount, categoryId, type, date);
        boolean created = transactionRepository.createTransaction(transaction);

        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Transaction created successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create transaction");
        }
    }

    // Get all transactions for a user
    public ResponseEntity<List<Transaction>> getUserTransactions(String username) {
        if (!userRepository.userExists(username)) {
            return ResponseEntity.notFound().build();
        }

        List<Transaction> transactions = transactionRepository.getTransactionsByUser(username);
        return ResponseEntity.ok(transactions);
    }

    // Get transactions by type (income/expense)
    public ResponseEntity<List<Transaction>> getUserTransactionsByType(String username, TransactionType type) {
        if (!userRepository.userExists(username)) {
            return ResponseEntity.notFound().build();
        }

        List<Transaction> transactions = transactionRepository.getTransactionsByUserAndType(username, type);
        return ResponseEntity.ok(transactions);
    }

    // Get transactions by category
    public ResponseEntity<List<Transaction>> getUserTransactionsByCategory(String username, int categoryId) {
        if (!userRepository.userExists(username)) {
            return ResponseEntity.notFound().build();
        }

        if (!transactionCategoryRepository.categoryExists(categoryId)) {
            return ResponseEntity.badRequest().build();
        }

        List<Transaction> transactions = transactionRepository.getTransactionsByUserAndCategory(username, categoryId);
        return ResponseEntity.ok(transactions);
    }

    // Get transactions by date range
    public ResponseEntity<List<Transaction>> getUserTransactionsByDateRange(String username,
                                                                            LocalDate startDate, LocalDate endDate) {
        if (!userRepository.userExists(username)) {
            return ResponseEntity.notFound().build();
        }

        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            return ResponseEntity.badRequest().build();
        }

        List<Transaction> transactions = transactionRepository.getTransactionsByUserAndDateRange(username, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    // Update transaction
    public ResponseEntity<String> updateTransaction(int transactionId, BigDecimal amount,
                                                    int categoryId, TransactionType type, LocalDate date) {
        // Check if transaction exists
        Optional<Transaction> existingTransaction = transactionRepository.getTransaction(transactionId);
        if (existingTransaction.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Validate input
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body("Amount must be positive");
        }
        if (date == null) {
            return ResponseEntity.badRequest().body("Date cannot be null");
        }

        // Check if category exists
        if (!transactionCategoryRepository.categoryExists(categoryId)) {
            return ResponseEntity.badRequest().body("Category does not exist");
        }

        // Update transaction
        Transaction transaction = existingTransaction.get();
        transaction.setAmount(amount);
        transaction.setCategoryId(categoryId);
        transaction.setTransactionType(type);
        transaction.setTransactionDate(date);

        boolean updated = transactionRepository.updateTransaction(transaction);

        if (updated) {
            return ResponseEntity.ok("Transaction updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update transaction");
        }
    }

    // Delete transaction
    public ResponseEntity<String> deleteTransaction(int transactionId) {
        Optional<Transaction> transaction = transactionRepository.getTransaction(transactionId);
        if (transaction.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        boolean deleted = transactionRepository.deleteTransaction(transactionId);

        if (deleted) {
            return ResponseEntity.ok("Transaction deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete transaction");
        }
    }

    // Get financial summary for user in date range
    public ResponseEntity<String> getFinancialSummary(String username, LocalDate startDate, LocalDate endDate) {
        if (!userRepository.userExists(username)) {
            return ResponseEntity.notFound().build();
        }

        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            return ResponseEntity.badRequest().body("Invalid date range");
        }

        BigDecimal totalIncome = transactionRepository.getTotalIncomeByUserAndDateRange(username, startDate, endDate);
        BigDecimal totalExpenses = transactionRepository.getTotalExpensesByUserAndDateRange(username, startDate, endDate);
        BigDecimal netSavings = totalIncome.subtract(totalExpenses);

        String summary = String.format(
                "Financial Summary for %s (%s to %s):\nTotal Income: $%.2f\nTotal Expenses: $%.2f\nNet Savings: $%.2f",
                username, startDate, endDate, totalIncome, totalExpenses, netSavings
        );

        return ResponseEntity.ok(summary);
    }
}
