package com.davidson.davcoinsapi.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.davidson.davcoinsapi.model.NotionUser;

public interface NotionUserRepository {
    
    Optional<NotionUser> findById(UUID id);

    List<NotionUser> findAll();
}
