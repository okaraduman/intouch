package com.aijoe.socialmedia.service;

import com.aijoe.socialmedia.model.dto.SikayetVarInfo;

import java.util.List;

public interface SikayetVarService {
    List<SikayetVarInfo> getReviews(String companyName);
}
