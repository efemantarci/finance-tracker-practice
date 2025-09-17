package com.CMPE451.finance_project.repository;

import com.CMPE451.finance_project.model.TransactionCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TransactionCategoryRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Get category by ID
    public Optional<TransactionCategory> getCategory(int categoryId) {
        try {
            TransactionCategory category = jdbcTemplate.queryForObject(
                    "SELECT category_id, category_name FROM transaction_categories WHERE category_id = ?",
                    (rs, rowNum) -> new TransactionCategory(
                            rs.getInt("category_id"),
                            rs.getString("category_name")
                    ),
                    categoryId
            );
            return Optional.of(category);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // Get all categories
    public List<TransactionCategory> getAllCategories() {
        return jdbcTemplate.query(
                "SELECT category_id, category_name FROM transaction_categories ORDER BY category_name",
                (rs, rowNum) -> new TransactionCategory(
                        rs.getInt("category_id"),
                        rs.getString("category_name")
                )
        );
    }

    // Create new category
    public boolean createCategory(TransactionCategory category) {
        try {
            int result = jdbcTemplate.update(
                    "INSERT INTO transaction_categories (category_name) VALUES (?)",
                    category.getName()
            );
            return result > 0;
        } catch (Exception e) {
            return false;
        }
    }

    // Update category
    public boolean updateCategory(TransactionCategory category) {
        int result = jdbcTemplate.update(
                "UPDATE transaction_categories SET category_name = ? WHERE category_id = ?",
                category.getName(),
                category.getCategoryId()
        );
        return result > 0;
    }

    // Delete category
    public boolean deleteCategory(int categoryId) {
        int result = jdbcTemplate.update(
                "DELETE FROM transaction_categories WHERE category_id = ?",
                categoryId
        );
        return result > 0;
    }

    // Get category by name
    public Optional<TransactionCategory> getCategoryByName(String categoryName) {
        try {
            TransactionCategory category = jdbcTemplate.queryForObject(
                    "SELECT category_id, category_name FROM transaction_categories WHERE category_name = ?",
                    (rs, rowNum) -> new TransactionCategory(
                            rs.getInt("category_id"),
                            rs.getString("category_name")
                    ),
                    categoryName
            );
            return Optional.of(category);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // Check if category exists by name
    public boolean categoryExistsByName(String categoryName) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM transaction_categories WHERE category_name = ?",
                Integer.class,
                categoryName
        );
        return count != null && count > 0;
    }

    // Check if category exists by ID
    public boolean categoryExists(int categoryId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM transaction_categories WHERE category_id = ?",
                Integer.class,
                categoryId
        );
        return count != null && count > 0;
    }

    // Get categories that contain a search term
    public List<TransactionCategory> searchCategoriesByName(String searchTerm) {
        return jdbcTemplate.query(
                "SELECT category_id, category_name FROM transaction_categories WHERE category_name LIKE ? ORDER BY category_name",
                (rs, rowNum) -> new TransactionCategory(
                        rs.getInt("category_id"),
                        rs.getString("category_name")
                ),
                "%" + searchTerm + "%"
        );
    }

    // Get count of all categories
    public int getCategoryCount() {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM transaction_categories",
                Integer.class
        );
        return count != null ? count : 0;
    }

    // Get categories used by a specific user
    public List<TransactionCategory> getCategoriesUsedByUser(String username) {
        return jdbcTemplate.query(
                "SELECT DISTINCT tc.category_id, tc.category_name FROM transaction_categories tc " +
                "INNER JOIN transactions t ON tc.category_id = t.category_id " +
                "WHERE t.user = ? ORDER BY tc.category_name",
                (rs, rowNum) -> new TransactionCategory(
                        rs.getInt("category_id"),
                        rs.getString("category_name")
                ),
                username
        );
    }

    // Get unused categories (categories with no transactions)
    public List<TransactionCategory> getUnusedCategories() {
        return jdbcTemplate.query(
                "SELECT tc.category_id, tc.category_name FROM transaction_categories tc " +
                "LEFT JOIN transactions t ON tc.category_id = t.category_id " +
                "WHERE t.category_id IS NULL ORDER BY tc.category_name",
                (rs, rowNum) -> new TransactionCategory(
                        rs.getInt("category_id"),
                        rs.getString("category_name")
                )
        );
    }
}
