package com.davidson.davcoinsapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.davidson.davcoinsapi.model.NotionUser;
import com.davidson.davcoinsapi.model.Transaction;
import com.davidson.davcoinsapi.service.NotionUserService;
import com.davidson.davcoinsapi.service.TransactionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    
    private final NotionUserService notionUserService;

    @PostMapping("/create")
    public ResponseEntity<Transaction[]> createTransactions(@RequestBody Transaction[] transactions){
        Transaction[] newTransactions = transactionService.createTransactions(transactions);

        return new ResponseEntity<>(newTransactions, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<JsonNode>> getTransactions(){
        List<Transaction> transactions = transactionService.getAllMostRecentTransactions();

        Map<String, NotionUser> notionUsersMap = notionUserService.getNotionUsersAsMap();

        NotionUser bankUser = notionUserService.getBankUser();
        notionUsersMap.put(bankUser.getId().toString(), bankUser);

        List<JsonNode> transactionJsonNodeList = new ArrayList<>();

        transactions.forEach( transaction -> {
            ObjectNode transactionJsonNode = new ObjectMapper().createObjectNode();

            transactionJsonNode.put("transaction_id", transaction.getId());
            String fromUserUUID = transaction.getFromUser().toString();
            transactionJsonNode.put("from_user_uuid", fromUserUUID);
            String fromUserName = notionUsersMap.get(fromUserUUID) == null ? "< DELETED USER >" : notionUsersMap.get(fromUserUUID).getName();
            transactionJsonNode.put("from_user_name", fromUserName);
            String toUserUUID = transaction.getToUser().toString();
            transactionJsonNode.put("to_user_uuid", toUserUUID);
            String toUserName = notionUsersMap.get(toUserUUID) == null ? "< DELETED USER >" : notionUsersMap.get(toUserUUID).getName();
            transactionJsonNode.put("to_user_name", toUserName);
            transactionJsonNode.put("transaction_amount", transaction.getAmount());
            transactionJsonNode.put("transaction_date", transaction.getTransactionDate().toString());
            transactionJsonNode.put("transaction_description", transaction.getTransactionDescription());
            transactionJsonNode.put("transaction_reason", transaction.getTransactionReason());

            transactionJsonNodeList.add(transactionJsonNode);
        });

        return new ResponseEntity<>(transactionJsonNodeList, HttpStatus.OK);
    }
    
}
