package com.davidson.davcoinsapi.service;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import com.davidson.davcoinsapi.exception.UserBalanceException;
import com.davidson.davcoinsapi.model.NotionUser;
import com.davidson.davcoinsapi.model.UserBalance;
import com.davidson.davcoinsapi.repository.UserBalanceRepository;
import com.davidson.davcoinsapi.validation.UserBalanceValidator;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserBalanceServiceTest {

    @Mock
    private UserBalanceRepository userBalanceRepository;

    @Mock
    private NotionUserService notionUserService;
    
    @Mock
    private UserBalanceValidator userBalanceValidator;

    private UserBalanceService userBalanceService;

    private final UUID validTestUUID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    private final UUID bankUserUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @BeforeEach
    void init(){
        userBalanceService = new UserBalanceService(userBalanceRepository, notionUserService, userBalanceValidator);
    }

    @Test
    void getUserBalance_validUserId_returnsUserBalanceAsOptional(){
        UserBalance userBalance = new UserBalance();

        when(userBalanceRepository.findById(validTestUUID)).thenReturn(Optional.of(userBalance));

        Optional<UserBalance> userBalanceOptional = userBalanceService.getUserBalance(validTestUUID);

        assertThat(userBalanceOptional).isPresent().contains(userBalance);

        verify(userBalanceRepository, times(1)).findById(validTestUUID);
    }

    @Test
    void getUserBalance_nullUserId_throwsIllegalArgumentException(){
        UUID uuid = null;

        when(userBalanceRepository.findById(uuid)).thenThrow(IllegalArgumentException.class);

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> userBalanceService.getUserBalance(uuid));

        verify(userBalanceRepository, times(1)).findById(uuid);
    }

    @Test
    void changeUserBalance_validInput_returnsUserBalance(){
        UserBalance userBalance = new UserBalance();
        userBalance.setId(validTestUUID);
        userBalance.setBalance(BigDecimal.ZERO);
        BigDecimal amount = new BigDecimal(1);
        NotionUser bankUser = new NotionUser();
        bankUser.setId(bankUserUUID);

        when(userBalanceRepository.findById(validTestUUID)).thenReturn(Optional.of(userBalance));
        doNothing().when(userBalanceValidator).validateUserBalance(userBalance);
        when(userBalanceRepository.save(userBalance)).thenReturn(userBalance);
        when(notionUserService.getBankUser()).thenReturn(bankUser);

        UserBalance newUserBalance = userBalanceService.changeUserBalance(validTestUUID, amount);

        assertThat(newUserBalance).isNotNull().isEqualTo(userBalance);

        verify(userBalanceRepository, times(1)).save(userBalance);
    }

    @Test
    void changeUserBalance_nullId_throwsIllegalArgumentException(){
        UUID uuid = null;
        BigDecimal amount = new BigDecimal(1);

        when(userBalanceService.getUserBalance(uuid)).thenThrow(IllegalArgumentException.class);

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> userBalanceService.changeUserBalance(uuid, amount));
    }

    @Test
    void changeUserBalance_nullAmount_throwsUserBalanceException(){
        BigDecimal amount = null;

        assertThatExceptionOfType(UserBalanceException.class).isThrownBy(() -> userBalanceService.changeUserBalance(validTestUUID, amount));
    }

    @Test
    void changeUserBalance_zeroAmount_throwsUserBalanceException(){
        BigDecimal amount = BigDecimal.ZERO;

        assertThatExceptionOfType(UserBalanceException.class).isThrownBy(() -> userBalanceService.changeUserBalance(validTestUUID, amount));
    }
}
