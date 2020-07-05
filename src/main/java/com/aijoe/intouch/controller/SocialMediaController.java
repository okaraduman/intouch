package com.aijoe.intouch.controller;

import com.aijoe.socialmedia.dto.TweetInfo;
import com.aijoe.socialmedia.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/social-media")
public class SocialMediaController {
    @Autowired
    TwitterService twitterService;

    @GetMapping("/twitter/{companyName}")
    public Set<TweetInfo> getTweetListByCompanyName(@PathVariable("companyName") String companyName){
        return twitterService.searchByCompanyName(companyName);
    }
}
