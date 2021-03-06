package com.aijoe.socialmedia.service.impl;

import com.aijoe.socialmedia.config.TwitterProperties;
import com.aijoe.socialmedia.factory.TwitterInstanceFactory;
import com.aijoe.socialmedia.model.dto.TweetInfo;
import com.aijoe.socialmedia.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;

import java.util.*;

import static com.aijoe.socialmedia.util.PurifyingUtils.clearSentence;
import static com.aijoe.socialmedia.util.PurifyingUtils.removeWhiteSpaces;

@Service
public class TwitterServiceImpl implements TwitterService {
    @Autowired
    TwitterInstanceFactory twitterFactory;

    @Autowired
    TwitterProperties twitterProperties;

    @Override
    public Set<TweetInfo> searchByCompanyName(String companyName) {
        companyName = getCompanyName(companyName);
        Twitter twitterInstance = twitterFactory.getInstance();
        List<QueryResult> queryResultList = getQueryResults(twitterInstance, companyName);
        return getTweetList(queryResultList);
    }

    private List<QueryResult> getQueryResults(Twitter twitterInstance, String companyName) {
        List<QueryResult> queryResultList = new ArrayList<>();
        QueryResult resultWithHashTag = getQueryResultWithHashTag(twitterInstance, removeWhiteSpaces(companyName));
        QueryResult resultWithAtSignTag = getQueryResultWithAtSignTag(twitterInstance, removeWhiteSpaces(companyName));

        queryResultList.add(resultWithHashTag);
        queryResultList.add(resultWithAtSignTag);

        return queryResultList;
    }

    private QueryResult getQueryResultWithHashTag(Twitter twitterInstance, String searchText) {
        Query queryWithHashTag = new Query("#" + searchText).lang(twitterProperties.getLanguage()).count(twitterProperties.getMaxResultCount());
        QueryResult queryResult = null;
        try {
            queryResult = twitterInstance.search(queryWithHashTag);
        } catch (Exception e) {
            System.out.println("An error occured while searching...");
        }
        return queryResult;
    }

    private QueryResult getQueryResultWithAtSignTag(Twitter twitterInstance, String searchText) {
        Query queryWithAtSignTag = new Query("@" + searchText).lang(twitterProperties.getLanguage()).count(twitterProperties.getMaxResultCount());
        QueryResult queryResult = null;
        try {
            queryResult = twitterInstance.search(queryWithAtSignTag);
        } catch (Exception e) {
            System.out.println("An error occured while searching...");
        }
        return queryResult;
    }

    private Set<TweetInfo> getTweetList(List<QueryResult> queryResultList) {
        Set<TweetInfo> twitterList = new HashSet<>();
        queryResultList.stream().filter(Objects::nonNull).forEach(queryResult -> {
            queryResult.getTweets().stream().filter(Objects::nonNull).forEach(t -> {
                TweetInfo tweetInfo = new TweetInfo();
                String messageText = t.isRetweet() ? t.getRetweetedStatus().getText() : t.getText();
                tweetInfo.setMessage(clearSentence(messageText));
                tweetInfo.setUrl("https://twitter.com/i/web/status/" + t.getId());
                twitterList.add(tweetInfo);
            });
        });

        return twitterList;
    }

    private String getCompanyName(String companyName){
        return companyName.replaceAll(" ", "").toLowerCase(new Locale("tr"));
    }
}
