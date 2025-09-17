package com.CMPE451.finance_project.repository;

import com.CMPE451.finance_project.model.Transaction;
import com.CMPE451.finance_project.model.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

@Repository
public class TransactionRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Get transaction by ID
    public Optional<Transaction> getTransaction(int transactionId) {
        try {
            Transaction transaction = jdbcTemplate.queryForObject(
                    "SELECT transaction_id, user, amount, category_id, transaction_type, transaction_date FROM transactions WHERE transaction_id = ?",
                    (rs, rowNum) -> new Transaction(
                            rs.getInt("transaction_id"),
                            rs.getString("user"),
                            rs.getBigDecimal("amount"),
                            rs.getInt("category_id"),
                            TransactionType.valueOf(rs.getString("transaction_type").toUpperCase()),
                            rs.getDate("transaction_date").toLocalDate()
                    ),
                    transactionId
            );
            return Optional.of(transaction);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // Create new transaction
    public boolean createTransaction(Transaction transaction) {
        try {
            int result = jdbcTemplate.update(
                    "INSERT INTO transactions (user, amount, category_id, transaction_type, transaction_date) VALUES (?, ?, ?, ?, ?)",
                    transaction.getUser(),
                    transaction.getAmount(),
                    transaction.getCategoryId(),
                    transaction.getTransactionType().name().toLowerCase(),
                    transaction.getTransactionDate()
            );
            return result > 0;
        } catch (Exception e) {
            return false;
        }
    }

    // Get all transactions for a user
    public List<Transaction> getTransactionsByUser(String username) {
        return jdbcTemplate.query(
                "SELECT transaction_id, user, amount, category_id, transaction_type, transaction_date FROM transactions WHERE user = ? ORDER BY transaction_date DESC",
                (rs, rowNum) -> new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getString("user"),
                        rs.getBigDecimal("amount"),
                        rs.getInt("category_id"),
                        TransactionType.valueOf(rs.getString("transaction_type").toUpperCase()),
                        rs.getDate("transaction_date").toLocalDate()
                ),
                username
        );
    }

    // Get transactions by user and type
    public List<Transaction> getTransactionsByUserAndType(String username, TransactionType type) {
        return jdbcTemplate.query(
                "SELECT transaction_id, user, amount, category_id, transaction_type, transaction_date FROM transactions WHERE user = ? AND transaction_type = ? ORDER BY transaction_date DESC",
                (rs, rowNum) -> new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getString("user"),
                        rs.getBigDecimal("amount"),
                        rs.getInt("category_id"),
                        TransactionType.valueOf(rs.getString("transaction_type").toUpperCase()),
                        rs.getDate("transaction_date").toLocalDate()
                ),
                username, type.name().toLowerCase()
        );
    }

    // Get transactions by user and category
    public List<Transaction> getTransactionsByUserAndCategory(String username, int categoryId) {
        return jdbcTemplate.query(
                "SELECT transaction_id, user, amount, category_id, transaction_type, transaction_date FROM transactions WHERE user = ? AND category_id = ? ORDER BY transaction_date DESC",
                (rs, rowNum) -> new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getString("user"),
                        rs.getBigDecimal("amount"),
                        rs.getInt("category_id"),
                        TransactionType.valueOf(rs.getString("transaction_type").toUpperCase()),
                        rs.getDate("transaction_date").toLocalDate()
                ),
                username, categoryId
        );
    }

    // Get transactions by date range
    public List<Transaction> getTransactionsByUserAndDateRange(String username, LocalDate startDate, LocalDate endDate) {
        return jdbcTemplate.query(
                "SELECT transaction_id, user, amount, category_id, transaction_type, transaction_date FROM transactions WHERE user = ? AND transaction_date BETWEEN ? AND ? ORDER BY transaction_date DESC",
                (rs, rowNum) -> new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getString("user"),
                        rs.getBigDecimal("amount"),
                        rs.getInt("category_id"),
                        TransactionType.valueOf(rs.getString("transaction_type").toUpperCase()),
                        rs.getDate("transaction_date").toLocalDate()
                ),
                username, startDate, endDate
        );
    }

    // Update transaction
    public boolean updateTransaction(Transaction transaction) {
        int result = jdbcTemplate.update(
                "UPDATE transactions SET amount = ?, category_id = ?, transaction_type = ?, transaction_date = ? WHERE transaction_id = ?",
                transaction.getAmount(),
                transaction.getCategoryId(),
                transaction.getTransactionType().name().toLowerCase(),
                transaction.getTransactionDate(),
                transaction.getTransactionId()
        );
        return result > 0;
    }

    // Delete transaction
    public boolean deleteTransaction(int transactionId) {
        int result = jdbcTemplate.update(
                "DELETE FROM transactions WHERE transaction_id = ?",
                transactionId
        );
        return result > 0;
    }

    // Get total income for user in date range
    public BigDecimal getTotalIncomeByUserAndDateRange(String username, LocalDate startDate, LocalDate endDate) {
        BigDecimal total = jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE user = ? AND transaction_type = 'income' AND transaction_date BETWEEN ? AND ?",
                BigDecimal.class,
                username, startDate, endDate
        );
        return total != null ? total : BigDecimal.ZERO;
    }

    // Get total expenses for user in date range
    public BigDecimal getTotalExpensesByUserAndDateRange(String username, LocalDate startDate, LocalDate endDate) {
        BigDecimal total = jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE user = ? AND transaction_type = 'expense' AND transaction_date BETWEEN ? AND ?",
                BigDecimal.class,
                username, startDate, endDate
        );
        return total != null ? total : BigDecimal.ZERO;
    }

    // Get total expenses by category for user in date range
    public BigDecimal getTotalExpensesByCategoryAndDateRange(String username, int categoryId, LocalDate startDate, LocalDate endDate) {
        BigDecimal total = jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE user = ? AND category_id = ? AND transaction_type = 'expense' AND transaction_date BETWEEN ? AND ?",
                BigDecimal.class,
                username, categoryId, startDate, endDate
        );
        return total != null ? total : BigDecimal.ZERO;
    }
}
