package com.aijoe.nlp.summary.service;

import java.util.List;

public interface SummaryService {
    List<String> getSummary(List<String> messageList);
}
