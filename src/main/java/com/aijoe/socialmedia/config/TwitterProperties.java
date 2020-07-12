package com.aijoe.socialmedia.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "twitter")
@Data
@Component
public class TwitterProperties {

    @Value("${twitter.consumer.key}")
    private String consumerKey;

    @Value("${twitter.consumer.secret}")
    private String consumerSecret;

    @Value("${twitter.accesstoken}")
    private String accessToken;

    @Value("${twitter.accesstokensecret}")
    private String accessTokenSecret;

    @Value("${twitter.language}")
    private String language;

    @Value("${twitter.maxresult.count}")
    private int maxResultCount;


}
