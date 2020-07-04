package com.aijoe.intouch;

import com.aijoe.socialmedia.config.TwitterProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties(value = {TwitterProperties.class})
@ComponentScan({"com.aijoe"})
public class InTouchApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(InTouchApplication.class, args);
    }

}
