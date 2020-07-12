package com.aijoe.intouch.controller;

import com.aijoe.nlp.clarification.service.ClarifyService;
import com.aijoe.socialmedia.dto.TweetInfo;
import com.aijoe.socialmedia.service.SikayetVarService;
import com.aijoe.socialmedia.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/intouch")
public class InTouchController {
    @Autowired
    TwitterService twitterService;

    @Autowired
    ClarifyService clarifyService;

    @Autowired
    SikayetVarService sikayetVarService;

    @GetMapping("/test/{companyName}")
    public List<TweetInfo> testService(@PathVariable("companyName") String companyName){
        List<String> reviewList = sikayetVarService.getReviews(companyName);
        Set<TweetInfo> tweetList = twitterService.searchByCompanyName(companyName);
        List<String> messages = tweetList.stream().map(t->t.getMessage()).collect(Collectors.toList());
        List<String> clearList = clarifyService.clarifySentence(messages);
        clearList.stream().filter(Objects::nonNull).forEach(t->{

        });

    @GetMapping("/test/{companyName}")
    public List<TweetInfo> testService(@PathVariable("companyName") String companyName){
        Set<TweetInfo> tweetList = twitterService.searchByCompanyName(companyName);
        List<String> messages = tweetList.stream().map(t->t.getMessage()).collect(Collectors.toList());
        List<String> clearList = clarifyService.clarifySentence(messages);
        List<TweetInfo> newList = new ArrayList<>();
        messages.forEach(t-> {
            TweetInfo tweetInfo = new TweetInfo();
            tweetInfo.setMessage(t);

            newList.add(tweetInfo);
        });

        return newList;
    }

}
