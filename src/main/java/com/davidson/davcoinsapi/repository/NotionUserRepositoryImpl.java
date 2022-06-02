package com.davidson.davcoinsapi.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.davidson.davcoinsapi.config.AppConfigurationProperties;
import com.davidson.davcoinsapi.model.NotionUser;
import com.davidson.davcoinsapi.service.NotionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NotionUserRepositoryImpl implements NotionUserRepository {

    private final AppConfigurationProperties props;

    private final NotionService notionService;

    private static final String DATABASE_ID_STRING = "database_id";

    @Override
    public Optional<NotionUser> findById(UUID id) {
        Optional<NotionUser> notionUserOptional = Optional.empty();

        if(id != null){
            JsonNode result = notionService.getPageById(id.toString()).block();

            if(result != null) {
                NotionUser notionUser = new NotionUser();
                notionUser.setId(id);
                JsonNode title = result.get("properties").get("Nom").get("title").get(0);
                notionUser.setName(title == null ? "" : title.get("plain_text").asText());
                notionUserOptional = Optional.of(notionUser);
            }
        }

        return notionUserOptional;
    }

    @Override
    public List<NotionUser> findAll() {
        String databaseId = props.getNotion().get(DATABASE_ID_STRING);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode query = mapper.createObjectNode();
        query.set("sorts", mapper.createArrayNode().add(mapper.createObjectNode().put("property", "Nom").put("direction", "ascending")));

        JsonNode result = notionService.queryDatabaseById(databaseId, query).block();

        List<NotionUser> userList = new ArrayList<>();

        if (result != null) {
            result.get("results").iterator().forEachRemaining(node -> {
                JsonNode title = node.get("properties").get("Nom").get("title").get(0);
                if (title != null) {
                    NotionUser newUser = new NotionUser();
                    String uuidString = node.get("id").asText();
                    newUser.setId(UUID.fromString(uuidString));
                    newUser.setName(title.get("plain_text").asText());
    
                    userList.add(newUser);
                }
            });
        }

        return userList;
    }
    
}
