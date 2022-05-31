package com.davidson.davcoinsapi.service;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.davidson.davcoinsapi.config.AppConfigurationProperties;
import com.davidson.davcoinsapi.model.NotionUser;
import com.davidson.davcoinsapi.repository.NotionUserRepositoryImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.data.MapEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NotionUserServiceTest {

    @Mock
    private AppConfigurationProperties props;

    @Mock
    private NotionUserRepositoryImpl notionUserRepositoryImpl;

    private NotionUserService notionUserService;

    private final UUID validTestUUID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    private final UUID bankUserUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private List<NotionUser> notionUsers;

    private NotionUser validTestNotionUser;

    private Map<String, String> bo;

    private Map<String, String> notion;

    @BeforeEach
    void init() throws JsonMappingException, JsonProcessingException{
        notionUserService = new NotionUserService(props, notionUserRepositoryImpl);
        
        validTestNotionUser = new NotionUser();
        validTestNotionUser.setId(validTestUUID);
        validTestNotionUser.setName("Test");

        notionUsers = new ArrayList<>();
        notionUsers.add(validTestNotionUser);

        bo = new HashMap<>();
        bo.put("bank_user_uuid", bankUserUUID.toString());

        notion = new HashMap<>();
        notion.put("database_id", "test");
    }

    @Test
    void getNotionUserByUUID_validId_returnsNotionUserAsOptional(){
        when(notionUserRepositoryImpl.findById(validTestUUID)).thenReturn(Optional.of(validTestNotionUser));

        Optional<NotionUser> notionUserOptional = notionUserService.getNotionUserByUUID(validTestUUID);

        assertThat(notionUserOptional).isPresent().contains(validTestNotionUser);

        verify(notionUserRepositoryImpl, times(1)).findById(validTestUUID);
    }

    @Test
    void getNotionUserByUUID_nullId_returnsEmptyOptional(){
        when(notionUserRepositoryImpl.findById(null)).thenReturn(Optional.empty());

        Optional<NotionUser> notionUserOptional = notionUserService.getNotionUserByUUID(null);

        assertThat(notionUserOptional).isEmpty();

        verify(notionUserRepositoryImpl, times(1)).findById(null);
    }

    @Test
    void notionUserExists_validId_returnsTrue(){
        when(props.getBo()).thenReturn(bo);
        when(notionUserRepositoryImpl.findById(validTestUUID)).thenReturn(Optional.of(validTestNotionUser));

        boolean userExists = notionUserService.notionUserExists(validTestUUID);

        assertThat(userExists).isTrue();

        verify(notionUserRepositoryImpl, times(1)).findById(validTestUUID);
    }

    @Test
    void notionUserExists_bankUserId_returnsTrue(){
        when(props.getBo()).thenReturn(bo);

        boolean userExists = notionUserService.notionUserExists(bankUserUUID);
        
        assertThat(userExists).isTrue();
    }

    @Test
    void notionUserExists_nullId_returnsFalse(){
        boolean userExists = notionUserService.notionUserExists(null);

        assertThat(userExists).isFalse();
    }

    @Test
    void getNotionUsersAsMap_returnsMap(){
        when(notionUserRepositoryImpl.findAll()).thenReturn(notionUsers);

        Map<String, NotionUser> userMap = notionUserService.getNotionUsersAsMap();

        assertThat(userMap).isNotNull().containsExactly(MapEntry.entry(validTestNotionUser.getId().toString(), validTestNotionUser));
    }

    @Test
    void getNotionUsersList_returnsList(){
        when(notionUserRepositoryImpl.findAll()).thenReturn(notionUsers);

        List<NotionUser> userList = notionUserService.getNotionUsersList();

        assertThat(userList).isNotNull().containsExactly(validTestNotionUser);
    }
}
