package com.aijoe.socialmedia.factory;

import com.aijoe.socialmedia.config.TwitterProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Component
public class TwitterInstanceFactory  {
    @Autowired
    TwitterProperties twitterProperties;

    public Twitter getInstance(){
        TwitterFactory twitterFactory = new TwitterFactory(getConfigurationBuilder().build());
        Twitter twitterInstance = twitterFactory.getInstance();

        return  twitterInstance;
    }

    private ConfigurationBuilder getConfigurationBuilder(){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(twitterProperties.getConsumerKey())
                .setOAuthConsumerSecret(twitterProperties.getConsumerSecret())
                .setOAuthAccessToken(twitterProperties.getAccessToken())
                .setOAuthAccessTokenSecret(twitterProperties.getAccessTokenSecret())
                .setTweetModeExtended(true);

        return cb;
    }
}
