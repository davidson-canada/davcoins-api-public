package com.davidson.davcoinsapi.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.davidson.davcoinsapi.dto.UserBalanceDTO;
import com.davidson.davcoinsapi.model.NotionUser;
import com.davidson.davcoinsapi.model.UserBalance;
import com.davidson.davcoinsapi.service.NotionUserService;
import com.davidson.davcoinsapi.service.UserBalanceService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user-balances")
@RequiredArgsConstructor
public class UserBalanceController {

    private final NotionUserService notionUserService;

    private final UserBalanceService userBalanceService;

    @GetMapping
    public ResponseEntity<List<UserBalanceDTO>> getAllUserBalances() {

        List<NotionUser> notionUsers = notionUserService.getNotionUsersList();

        List<UserBalanceDTO> userBalanceDTOs = new ArrayList<>();

        for(NotionUser notionUser : notionUsers){
            UserBalanceDTO userBalanceDTO = new UserBalanceDTO();

            userBalanceDTO.setNotionUser(notionUser);

            Optional<UserBalance> userBalanceOptional = userBalanceService.getUserBalance(notionUser.getId());
            if(userBalanceOptional.isPresent()){
                userBalanceDTO.setBalance(userBalanceOptional.get().getBalance());
            } else {
                userBalanceDTO.setBalance(BigDecimal.ZERO);
            }

            userBalanceDTOs.add(userBalanceDTO);
        }

        return new ResponseEntity<>(userBalanceDTOs,
                HttpStatus.OK);
    }

}
