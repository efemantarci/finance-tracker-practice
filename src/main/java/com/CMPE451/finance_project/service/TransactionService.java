package com.CMPE451.finance_project.service;

import com.CMPE451.finance_project.dto.TransactionWithDetails;
import com.CMPE451.finance_project.model.Transaction;
import com.CMPE451.finance_project.model.TransactionCategory;
import com.CMPE451.finance_project.model.User;
import com.CMPE451.finance_project.repository.TransactionCategoryRepository;
import com.CMPE451.finance_project.repository.TransactionRepository;
import com.CMPE451.finance_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TransactionCategoryRepository transactionCategoryRepository;
    @Autowired
    TransactionRepository transactionRepository;

    ResponseEntity<TransactionWithDetails> getTransaction(int transactionId){
        Optional<Transaction> transactionOpt = transactionRepository.getTransaction(transactionId);
        if(transactionOpt.isEmpty()){
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
}
