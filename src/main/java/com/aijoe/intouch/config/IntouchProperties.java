package com.aijoe.intouch.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "intouch")
@Data
@Component
public class IntouchProperties {
    @Value("${intouch.treshold}")
    private Float treshold;
}
