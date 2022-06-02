package com.davidson.davcoinsapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.davidson.davcoinsapi.dto.TransactionDTO;
import com.davidson.davcoinsapi.model.NotionUser;
import com.davidson.davcoinsapi.model.Transaction;
import com.davidson.davcoinsapi.service.NotionUserService;
import com.davidson.davcoinsapi.service.TransactionService;

import org.modelmapper.ModelMapper;
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

    private final ModelMapper modelMapper;

    private static final String DELETED_NOTION_USER_NAME = "< DELETED USER >";

    @PostMapping("/create")
    public ResponseEntity<List<TransactionDTO>> createTransactions(@RequestBody List<TransactionDTO> transactionDTOs) {
        List<Transaction> newTransactions = transactionService.createTransactions(convertToEntities(transactionDTOs));

        return new ResponseEntity<>(convertToDTOs(newTransactions), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getTransactions() {
        return new ResponseEntity<>(convertToDTOs(transactionService.getAllMostRecentTransactions()),
                HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<TransactionDTO>> getTransactionPage(@RequestParam int pageNumber,
            @RequestParam int pageSize) {
        Page<Transaction> transactionPage = transactionService.getTransactionPage(pageNumber, pageSize);

        Page<TransactionDTO> transactionDTOPage = new PageImpl<>(convertToDTOs(transactionPage.getContent()),
                transactionPage.getPageable(), transactionPage.getTotalElements());
        return new ResponseEntity<>(transactionDTOPage, HttpStatus.OK);
    }

    private List<Transaction> convertToEntities(List<TransactionDTO> transactionDTOs) {
        List<Transaction> transactions = new ArrayList<>();

        for (TransactionDTO transactionDTO : transactionDTOs) {
            Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);
            transaction.setFromUser(transactionDTO.getFromUser().getId());
            transaction.setToUser(transactionDTO.getToUser().getId());
            transactions.add(transaction);
        }

        return transactions;
    }

    private List<TransactionDTO> convertToDTOs(List<Transaction> transactions) {
        List<TransactionDTO> transactionDTOs = new ArrayList<>();

        Map<String, NotionUser> notionUsersMap = notionUserService.getNotionUsersAsMap();

        NotionUser bankUser = notionUserService.getBankUser();
        notionUsersMap.put(bankUser.getId().toString(), bankUser);

        for (Transaction transaction : transactions) {
            TransactionDTO transactionDTO = modelMapper.map(transaction, TransactionDTO.class);

            UUID fromUserUUID = transaction.getFromUser();
            NotionUser fromUser = new NotionUser();
            if (notionUsersMap.get(fromUserUUID.toString()) == null) {
                fromUser.setId(fromUserUUID);
                fromUser.setName(DELETED_NOTION_USER_NAME);
            } else {
                fromUser = notionUsersMap.get(fromUserUUID.toString());
            }
            transactionDTO.setFromUser(fromUser);

            UUID toUserUUID = transaction.getToUser();
            NotionUser toUser = new NotionUser();
            if (notionUsersMap.get(toUserUUID.toString()) == null) {
                toUser.setId(toUserUUID);
                toUser.setName(DELETED_NOTION_USER_NAME);
            } else {
                toUser = notionUsersMap.get(toUserUUID.toString());
            }
            transactionDTO.setToUser(toUser);

            transactionDTOs.add(transactionDTO);
        }

        return transactionDTOs;
    }

}
