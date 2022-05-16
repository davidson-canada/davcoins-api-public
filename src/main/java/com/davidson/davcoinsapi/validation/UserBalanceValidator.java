package com.davidson.davcoinsapi.validation;

import java.math.BigDecimal;

import com.davidson.davcoinsapi.exception.UserBalanceException;
import com.davidson.davcoinsapi.model.UserBalance;

import org.springframework.stereotype.Service;

@Service
public class UserBalanceValidator {

    /**
     * Validates the user balance. 
     * <p>
     * Ensures that the user balance and its fields are not null. 
     * Also checks that the user balance id is not the bank user's id, 
     * and that the balance is positive.
     * 
     * @param userBalance the object representing the user's balance.
     * @throws UserBalanceException
     */
    public void validateUserBalance(UserBalance userBalance) throws UserBalanceException {
        if (userBalance == null) {
            throw new UserBalanceException("User balance is null.");
        }
        if (userBalance.getId() == null) {
            throw new UserBalanceException("User balance's id is null.");
        }
        if (userBalance.getBalance() == null) {
            throw new UserBalanceException("User balance's balance is null.");
        }
        if (userBalance.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new UserBalanceException("The user has insufficient funds.");
        }
    }

}
