package com.davidson.davcoinsapi.service;

import java.util.Map;

import com.davidson.davcoinsapi.config.AppConfigurationProperties;
import com.davidson.davcoinsapi.exception.NotionAPIException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NotionService {

    private final AppConfigurationProperties props;
    
    public Mono<JsonNode> queryDatabaseById(String databaseId, JsonNode query) throws NotionAPIException {
        return buildWebClient().post()
        .uri("databases/{id}/query", databaseId)
        .bodyValue(query)
        .retrieve()
        .bodyToMono(JsonNode.class)
        .onErrorResume(this::handleNotionError);
    }

    public Mono<JsonNode> queryDatabaseGetAllById(String databaseId) throws NotionAPIException {
        return queryDatabaseById(databaseId, new ObjectMapper().createObjectNode());
    }

    public Mono<JsonNode> getPageById(String pageId) throws NotionAPIException {
        return buildWebClient().get()
        .uri("pages/{id}", pageId)
        .retrieve()
        .bodyToMono(JsonNode.class)
        .onErrorResume(this::handleNotionError);
    }

    /**
     * Builds WebClient object to makes calls to notion REST API.
     * 
     * @return the WebClient object.
     */
    private WebClient buildWebClient() {
        Map<String, String> notion = props.getNotion();

        String apiUrl = notion.get("api_url");
        String notionVersion = notion.get("api_version");
        String notionApiSecret = notion.get("api_key");

        return WebClient
                .builder()
                .baseUrl(apiUrl)
                .defaultHeaders(
                        header -> {
                            header.add(HttpHeaders.AUTHORIZATION, "Bearer " + notionApiSecret);
                            header.add("Notion-Version", notionVersion);
                            header.add(HttpHeaders.CONTENT_TYPE, "application/json");
                        })
                .build();
    }

    private Mono<JsonNode> handleNotionError(Throwable error){
        String errorMessage = String.format("Notion error: %s", error.getMessage());
        throw new NotionAPIException(errorMessage);
    }
}
