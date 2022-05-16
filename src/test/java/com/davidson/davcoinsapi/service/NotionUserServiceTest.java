package com.davidson.davcoinsapi.service;

import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.davidson.davcoinsapi.config.AppConfigurationProperties;
import com.davidson.davcoinsapi.model.NotionUser;
import com.davidson.davcoinsapi.repository.NotionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.data.MapEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class NotionUserServiceTest {

    @Mock
    private AppConfigurationProperties props;

    @Mock
    private NotionRepository notionRepository;

    private NotionUserService notionUserService;

    private final UUID validTestUUID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    private final UUID bankUserUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private JsonNode validPage;

    private JsonNode validDatabase;

    private NotionUser validTestNotionUser;

    @BeforeEach
    void init() throws JsonMappingException, JsonProcessingException{
        notionUserService = new NotionUserService(props, notionRepository);
        
        validTestNotionUser = new NotionUser();
        validTestNotionUser.setId(validTestUUID);
        validTestNotionUser.setName("Test");
        
        String validPageJson = "{\"id\":\"" + validTestNotionUser.getId() + "\", \"properties\": { \"Nom\" : { \"title\" : [{ \"plain_text\": \"" + validTestNotionUser.getName() + "\" }]}}}";
        
        ObjectMapper objectMapper = new ObjectMapper();
        validPage = objectMapper.readTree(validPageJson);
        validDatabase = objectMapper.readTree("{\"results\": [" + validPageJson + "]}");
    }

    @Test
    void getNotionUserByUUID_validId_returnsNotionUserAsOptional(){
        when(notionRepository.getPageById(validTestUUID.toString())).thenReturn(Mono.just(validPage));

        Optional<NotionUser> notionUserOptional = notionUserService.getNotionUserByUUID(validTestUUID);

        assertThat(notionUserOptional).isPresent().contains(validTestNotionUser);

        verify(notionRepository, times(1)).getPageById(validTestUUID.toString());
    }

    @Test
    void getNotionUserByUUID_nullId_returnsEmptyOptional(){
        Optional<NotionUser> notionUserOptional = notionUserService.getNotionUserByUUID(null);

        assertThat(notionUserOptional).isEmpty();
    }

    @Test
    void notionUserExists_validId_returnsTrue(){
        Map<String, String> bo = new HashMap<>();
        bo.put("bank_user_uuid", bankUserUUID.toString());

        when(props.getBo()).thenReturn(bo);
        when(notionRepository.getPageById(validTestUUID.toString())).thenReturn(Mono.just(validPage));

        boolean userExists = notionUserService.notionUserExists(validTestUUID);

        assertThat(userExists).isTrue();
    }

    @Test
    void notionUserExists_bankUserId_returnsTrue(){
        Map<String, String> bo = new HashMap<>();
        bo.put("bank_user_uuid", bankUserUUID.toString());
        
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
        Map<String, String> notion = new HashMap<>();
        notion.put("database_id", "test");

        when(props.getNotion()).thenReturn(notion);
        when(notionRepository.queryDatabaseGetAllById(notion.get("database_id"))).thenReturn(Mono.just(validDatabase));

        Map<String, NotionUser> userMap = notionUserService.getNotionUsersAsMap();

        assertThat(userMap).isNotNull().containsExactly(MapEntry.entry(validTestNotionUser.getId().toString(), validTestNotionUser));
    }

    @Test
    void getNotionUsersList_returnsList(){
        Map<String, String> notion = new HashMap<>();
        notion.put("database_id", "test");

        when(props.getNotion()).thenReturn(notion);
        when(notionRepository.queryDatabaseGetAllById(notion.get("database_id"))).thenReturn(Mono.just(validDatabase));

        List<NotionUser> userList = notionUserService.getNotionUsersList();

        assertThat(userList).isNotNull().containsExactly(validTestNotionUser);
    }
}
