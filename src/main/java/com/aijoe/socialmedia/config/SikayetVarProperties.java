package com.aijoe.socialmedia.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "sikayetvar")
@Data
@Component
public class SikayetVarProperties {
    @Value("${sikayetvar.url}")
    private String url;

    @Value("${sikayetvar.maxpagecount}")
    private int maxPageCount;
}
