package com.CMPE451.finance_project.repository;

import com.CMPE451.finance_project.model.TransactionCategory;
import com.CMPE451.finance_project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TransactionCategoryRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public Optional<TransactionCategory> getCategory(int categoryId){
        try {
            TransactionCategory category = jdbcTemplate.queryForObject(
                    "SELECT category_id,category_name FROM transactions WHERE transaction_id = ?",
                    (rs, rowNum) -> new TransactionCategory(
                            rs.getInt("category_id"),
                            rs.getString("password_hash")
                    ),
                    categoryId
            );
            return Optional.of(category);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
