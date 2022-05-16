package com.davidson.davcoinsapi.config;

import java.io.Serializable;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "com.davidson.davcoinsapi")
public class AppConfigurationProperties implements Serializable {

    private static final long serialVersionUID = -1L;

    private String jwtSecret;

    private Map<String, String> bo;

    private Map<String, String> notion;
}
