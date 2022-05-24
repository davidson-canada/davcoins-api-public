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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        return new ResponseEntity<>(getTransactionsWithUserNames(transactionService.getAllMostRecentTransactions()), HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<JsonNode>> getTransactionPage(@RequestParam int pageNumber, @RequestParam int pageSize){
        Page<Transaction> transactionPage = transactionService.getTransactionPage(pageNumber, pageSize);
        Page<JsonNode> jsonNodePage = new PageImpl<>(getTransactionsWithUserNames(transactionPage.getContent()), transactionPage.getPageable(), transactionPage.getTotalElements());
        return new ResponseEntity<>(jsonNodePage, HttpStatus.OK);
    }

    private List<JsonNode> getTransactionsWithUserNames(List<Transaction> transactions){
        Map<String, NotionUser> notionUsersMap = notionUserService.getNotionUsersAsMap();

        NotionUser bankUser = notionUserService.getBankUser();
        notionUsersMap.put(bankUser.getId().toString(), bankUser);

        List<JsonNode> transactionJsonNodeList = new ArrayList<>();

        transactions.forEach( transaction -> {
            ObjectNode transactionJsonNode = new ObjectMapper().createObjectNode();

            transactionJsonNode.put("transactionId", transaction.getId());
            String fromUserUUID = transaction.getFromUser().toString();
            transactionJsonNode.put("fromUserUuid", fromUserUUID);
            String fromUserName = notionUsersMap.get(fromUserUUID) == null ? "< DELETED USER >" : notionUsersMap.get(fromUserUUID).getName();
            transactionJsonNode.put("fromUserName", fromUserName);
            String toUserUUID = transaction.getToUser().toString();
            transactionJsonNode.put("toUserUuid", toUserUUID);
            String toUserName = notionUsersMap.get(toUserUUID) == null ? "< DELETED USER >" : notionUsersMap.get(toUserUUID).getName();
            transactionJsonNode.put("toUserName", toUserName);
            transactionJsonNode.putPOJO("product", transaction.getProduct());
            transactionJsonNode.put("transactionAmount", transaction.getTransactionAmount());
            transactionJsonNode.put("transactionDate", transaction.getTransactionDate().toString());
            transactionJsonNode.put("transactionDescription", transaction.getTransactionDescription());

            transactionJsonNodeList.add(transactionJsonNode);
        });

        return transactionJsonNodeList;
    }
    
}
