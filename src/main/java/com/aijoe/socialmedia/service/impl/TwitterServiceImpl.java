package com.aijoe.socialmedia.service.impl;

import com.aijoe.socialmedia.config.TwitterProperties;
import com.aijoe.socialmedia.dto.TweetInfo;
import com.aijoe.socialmedia.factory.TwitterInstanceFactory;
import com.aijoe.socialmedia.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TwitterServiceImpl implements TwitterService {
    @Autowired
    TwitterInstanceFactory twitterFactory;

    @Autowired
    TwitterProperties twitterProperties;

    @Override
    public List<TweetInfo> searchByCompanyName(String companyName) {
        Twitter twitterInstance = twitterFactory.getInstance();
        QueryResult result = getQueryResult(twitterInstance, clearText(companyName));
        return getTweetList(result);
    }

    public List<TweetInfo> getTweetList(QueryResult queryResult){
        List<TweetInfo> twitterList = new ArrayList<>();
        queryResult.getTweets().stream().filter(Objects::nonNull).forEach(t->{
            TweetInfo tweetInfo = new TweetInfo();
            tweetInfo.setUser(t.getUser().getName());
            tweetInfo.setMessage(t.getText());

            twitterList.add(tweetInfo);
        });
        return twitterList;
    }

    private QueryResult getQueryResult(Twitter twitterInstance, String searchText){
        Query query = new Query("#" + searchText).lang(twitterProperties.getLanguage()).count(twitterProperties.getMaxResultCount());
        QueryResult queryResult = null;
        try{
            queryResult = twitterInstance.search(query);
        }catch (Exception e){
            System.out.println("An error occured while searching...");
        }
        return queryResult;
    }

    private String clearText(String text){
        return text.replaceAll(" ", "");
    }
}
