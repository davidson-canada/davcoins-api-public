package com.davidson.davcoinsapi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.davidson.davcoinsapi.config.AppConfigurationProperties;
import com.davidson.davcoinsapi.exception.NotionAPIException;
import com.davidson.davcoinsapi.model.NotionUser;
import com.davidson.davcoinsapi.repository.NotionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotionUserService {

    private final AppConfigurationProperties props;

    private final NotionRepository notionRepository;

    private static final String DATABASE_ID_STRING = "database_id";

    /**
     * Gets all users from the Notion database.
     * 
     * @return a list containing NotionUser objects.
     * @throws NotionAPIException if the notion API returns an error.
     */
    public List<NotionUser> getNotionUsersList() throws NotionAPIException {
        return new ArrayList<>(getNotionUsersAsMap().values());
    }

    /**
     * Gets all users from the Notion database.
     * 
     * @return a map containing NotionUser objects.
     * @throws NotionAPIException if the notion API returns an error.
     */
    public Map<String, NotionUser> getNotionUsersAsMap() throws NotionAPIException {
        String databaseId = props.getNotion().get(DATABASE_ID_STRING);

        JsonNode result = notionRepository.queryDatabaseGetAllById(databaseId).block();

        Map<String, NotionUser> userMap = new HashMap<>();

        if (result != null) {
            result.get("results").iterator().forEachRemaining(node -> {
                JsonNode title = node.get("properties").get("Nom").get("title").get(0);
                if (title != null) {
                    NotionUser newUser = new NotionUser();
                    String uuidString = node.get("id").asText();
                    newUser.setId(UUID.fromString(uuidString));
                    newUser.setName(title.get("plain_text").asText());
    
                    userMap.put(uuidString, newUser);
                }
            });
        }

        return userMap;
    }

    /**
     * * Gets the notion user with the corresponding UUID.
     * 
     * @param id the UUID of the notion user to find.
     * @return an optional that contains the NotionUser object, or that is empty the
     *         the user was not found.
     * @throws NotionAPIException if the notion API returns an error.
     */
    public Optional<NotionUser> getNotionUserByUUID(final UUID id) throws NotionAPIException {
        Optional<NotionUser> notionUserOptional = Optional.empty();

        if(id != null){
            JsonNode result = notionRepository.getPageById(id.toString()).block();

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

    /**
     * Checks if a notion user with the given id exists.
     * 
     * @param id the notion user's id
     * @return true if the user is found, false otherwise.
     * @throws NotionAPIException if the notion API returns an error.
     */
    public boolean notionUserExists(final UUID id) throws NotionAPIException {
        return id != null && (id.equals(getBankUser().getId()) || getNotionUserByUUID(id).isPresent());
    }

    /**
     * Creates notion user object that represents the bank.
     * 
     * @return created notion user.
     */
    public NotionUser getBankUser() {
        NotionUser bankUser = new NotionUser();
        String bankUserUUID = props.getBo().get("bank_user_uuid");

        bankUser.setId(UUID.fromString(bankUserUUID));
        bankUser.setName("Bank");

        return bankUser;
    }
}
