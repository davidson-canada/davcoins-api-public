package com.davidson.davcoinsapi.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.davidson.davcoinsapi.exception.InvalidTransactionException;
import com.davidson.davcoinsapi.exception.UserBalanceException;
import com.davidson.davcoinsapi.exception.UserNotFoundException;
import com.davidson.davcoinsapi.model.Transaction;
import com.davidson.davcoinsapi.repository.TransactionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final NotionUserService notionUserService;

    private final TransactionRepository transactionRepository;

    private final UserBalanceService userBalanceService;

    @Transactional
    public Transaction[] createTransactions(Transaction[] transactions)
            throws InvalidTransactionException, UserBalanceException, UserNotFoundException {

        int numTransactions = transactions.length;

        Transaction[] newTransactions = new Transaction[numTransactions];

        UUID bankUserUUID = notionUserService.getBankUser().getId();

        for (int i = 0; i < numTransactions; i++) {
            Transaction transaction = transactions[i];

            validateTransaction(transaction);

            if (transaction.getFromUser().equals(bankUserUUID)) {
                userBalanceService.changeUserBalance(transaction.getToUser(), transaction.getAmount());
            } else {
                userBalanceService.changeUserBalance(transaction.getFromUser(), transaction.getAmount().negate());
                if (!transaction.getToUser().equals(bankUserUUID)) {
                    userBalanceService.changeUserBalance(transaction.getToUser(), transaction.getAmount());
                }
            }

            newTransactions[i] = transactionRepository.save(transaction);
        }

        return newTransactions;
    }

    public Iterable<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getAllMostRecentTransactions() {
        return transactionRepository.findAllByOrderByTransactionDateDesc();
    }

    private void validateTransaction(Transaction transaction) throws InvalidTransactionException {

        StringBuilder validationErrorMessage = new StringBuilder();

        if (transaction.getToUser() == null || transaction.getToUser().toString().isBlank()) {
            validationErrorMessage.append("Missing to user. ");
        }
        if (transaction.getFromUser() == null || transaction.getFromUser().toString().isBlank()) {
            validationErrorMessage.append("Missing from user. ");
        }
        if (transaction.getToUser().equals(transaction.getFromUser())) {
            validationErrorMessage.append("To and from user are the same. ");
        }
        if (transaction.getAmount() == null) {
            validationErrorMessage.append("Missing amount. ");
        } else if (transaction.getAmount().compareTo(BigDecimal.ZERO) < 1) {
            validationErrorMessage.append("Amount must be greater than 0. ");
        }

        if (!validationErrorMessage.toString().isBlank()) {
            throw new InvalidTransactionException(validationErrorMessage.toString());
        }
    }
}
