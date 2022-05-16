package com.davidson.davcoinsapi.controller;

import com.davidson.davcoinsapi.model.NotionUser;
import com.davidson.davcoinsapi.service.NotionUserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class NotionUserController {
    
    private final NotionUserService notionUserService;

    @GetMapping
    public ResponseEntity<List<NotionUser>> getAllUsers() {

        List<NotionUser> notionUsers = notionUserService.getNotionUsersList();

        return new ResponseEntity<>(notionUsers,
        HttpStatus.OK);
    }

}
