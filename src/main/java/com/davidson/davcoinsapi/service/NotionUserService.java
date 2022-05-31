package com.davidson.davcoinsapi.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.davidson.davcoinsapi.config.AppConfigurationProperties;
import com.davidson.davcoinsapi.exception.NotionAPIException;
import com.davidson.davcoinsapi.model.NotionUser;
import com.davidson.davcoinsapi.repository.NotionUserRepositoryImpl;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotionUserService {

    private final AppConfigurationProperties props;

    private final NotionUserRepositoryImpl notionUserRepositoryImpl;

    /**
     * Gets all users from the Notion database.
     * 
     * @return a list containing NotionUser objects.
     * @throws NotionAPIException if the notion API returns an error.
     */
    public List<NotionUser> getNotionUsersList() throws NotionAPIException {
        return notionUserRepositoryImpl.findAll();
    }

    /**
     * Gets all users from the Notion database.
     * 
     * @return a map containing NotionUser objects.
     * @throws NotionAPIException if the notion API returns an error.
     */
    public Map<String, NotionUser> getNotionUsersAsMap() throws NotionAPIException {
        Map<String, NotionUser> userMap = new HashMap<>();

        for(NotionUser notionUser : getNotionUsersList()){
            userMap.put(notionUser.getId().toString(), notionUser);
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
        return notionUserRepositoryImpl.findById(id);
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
