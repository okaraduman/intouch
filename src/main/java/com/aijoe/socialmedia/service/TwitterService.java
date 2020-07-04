package com.aijoe.socialmedia.service;

import com.aijoe.socialmedia.dto.TweetInfo;

import java.util.List;

public interface TwitterService {
    List<TweetInfo> searchByCompanyName(String companyName);
}
