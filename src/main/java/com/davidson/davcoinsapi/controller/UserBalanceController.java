package com.davidson.davcoinsapi.controller;

import java.util.ArrayList;
import java.util.List;

import com.davidson.davcoinsapi.exception.NotionAPIException;
import com.davidson.davcoinsapi.model.NotionUser;
import com.davidson.davcoinsapi.model.UserBalance;
import com.davidson.davcoinsapi.service.NotionUserService;
import com.davidson.davcoinsapi.service.UserBalanceService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(UserBalanceController.class);

    private final NotionUserService notionUserService;

    private final UserBalanceService userBalanceService;

    @GetMapping
    public ResponseEntity<List<JsonNode>> getAllUserBalances() {

        List<NotionUser> notionUsers = notionUserService.getNotionUsersList();

        List<JsonNode> userInfoObjects = new ArrayList<>();

        notionUsers.iterator().forEachRemaining(notionUser -> {
            ObjectNode userInfo = new ObjectMapper().createObjectNode();

            try {
                UserBalance userBalance = userBalanceService.getUserBalance(notionUser.getId()).get();
                userInfo.put("id", notionUser.getId().toString());
                userInfo.put("name", notionUser.getName());
                userInfo.put("balance", userBalance.getBalance().toString());
                userInfoObjects.add(userInfo);
            } catch (IllegalArgumentException illegalArgumentException){
                String errorMessage = String.format("Invalid id for Notion user: name: %s, id: %s", notionUser.getName(), notionUser.getId());
                logger.error(errorMessage);
            } catch (NotionAPIException notionAPIException){
                logger.error(notionAPIException.getMessage());
            }
        });

        return new ResponseEntity<>(userInfoObjects,
                HttpStatus.OK);
    }
}
