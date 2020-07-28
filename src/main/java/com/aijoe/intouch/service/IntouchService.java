package com.aijoe.intouch.service;

import com.aijoe.intouch.model.output.FeedbackOutput;

public interface IntouchService {
    FeedbackOutput getTwitterFeedbacks(String companyName);

    FeedbackOutput getSikayetvarFeedbacks(String companyName);
}
