package com.aijoe.socialmedia.service;

import com.aijoe.socialmedia.model.dto.TweetInfo;

import java.util.Set;

public interface TwitterService {
    Set<TweetInfo> searchByCompanyName(String companyName);
}
