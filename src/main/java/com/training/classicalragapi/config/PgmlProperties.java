package com.training.classicalragapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix ="datasource.pgml")
@Component
public class PgmlProperties {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
