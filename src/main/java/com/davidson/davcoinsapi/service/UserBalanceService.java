package com.davidson.davcoinsapi.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import com.davidson.davcoinsapi.exception.NotionAPIException;
import com.davidson.davcoinsapi.exception.UserBalanceException;
import com.davidson.davcoinsapi.exception.UserNotFoundException;
import com.davidson.davcoinsapi.model.UserBalance;
import com.davidson.davcoinsapi.repository.UserBalanceRepository;
import com.davidson.davcoinsapi.validation.UserBalanceValidator;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserBalanceService {

    private final UserBalanceRepository userBalanceRepository;

    private final NotionUserService notionUserService;

    private final UserBalanceValidator userBalanceValidator;

    /**
     * Retrieves the user's balance from the database.
     * <p>
     * If the user balance is not found, it checks if the user exists in Notion. 
     * If the user exists in Notion, it returns a user balance with the default balance of 0.
     * 
     * @param id the id of the user.
     * @return an optional containing the user balance, or an empty optional if the user doesn't exist.
     * @throws IllegalArgumentException if the id is null.
     * @throws NotionAPIException if there is an error connecting to the API.
     */
    public Optional<UserBalance> getUserBalance(final UUID id) throws IllegalArgumentException, NotionAPIException {
        Optional<UserBalance> userBalanceOptional = userBalanceRepository.findById(id);

        if(userBalanceOptional.isEmpty() && notionUserService.notionUserExists(id)){
            UserBalance userBalance = new UserBalance();
            userBalance.setId(id);
            userBalance.setBalance(BigDecimal.ZERO.setScale(2));
            userBalanceOptional = Optional.of(userBalance);
        }

        return userBalanceOptional;
    }

    /**
     * Modifies the user's balance based on the amount.
     * <p>
     * The method retrieves the user balance based on the id. If the user balance 
     * is not found, an exception is thrown.
     * <p>
     * If the amount is positive, we are depositing into the user's balance.
     * If the amount is negative, we are withdrawing from the user's balance.
     * 
     * @param id     the UUID of the user.
     * @param amount the amount to add to the user's balance.
     * @return the new user balance.
     * @throws UserBalanceException if the user balance is invalid.
     * @throws UserNotFoundException if the user does not exist.
     * @throws IllegalArgumentException is the id is null.
     */
    public UserBalance changeUserBalance(final UUID id, BigDecimal amount)
            throws UserBalanceException, UserNotFoundException, IllegalArgumentException {        
        if (amount == null || amount.equals(BigDecimal.ZERO)) {
            throw new UserBalanceException("Amount is invalid. amount = " + amount);
        }

        UserBalance userBalance = getUserBalance(id).orElseThrow(() -> new UserNotFoundException("Notion user with UUID " + id.toString() + " does not exist"));

        if (notionUserService.getBankUser().getId().equals(userBalance.getId())) {
            throw new UserBalanceException("Cannot modify bank user's balance.");
        }

        userBalance.setBalance(userBalance.getBalance().add(amount));

        userBalanceValidator.validateUserBalance(userBalance);

        return userBalanceRepository.save(userBalance);
    }

}
