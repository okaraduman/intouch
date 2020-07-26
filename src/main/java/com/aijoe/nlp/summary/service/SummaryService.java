package com.aijoe.nlp.summary.service;

import java.util.List;

public interface SummaryService {
    String getSummary(String message);

    List<String> getSummaryList(List<String> messageList);
}
