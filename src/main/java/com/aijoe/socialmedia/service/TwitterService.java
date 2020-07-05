package com.aijoe.socialmedia.service;

import com.aijoe.socialmedia.dto.TweetInfo;

import java.util.List;
import java.util.Set;

public interface TwitterService {
    Set<TweetInfo> searchByCompanyName(String companyName);
}
