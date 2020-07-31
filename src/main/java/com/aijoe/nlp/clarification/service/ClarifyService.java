package com.aijoe.nlp.clarification.service;

import java.util.List;

public interface ClarifyService {
    String clarifySentences(String sentence);

    List<String> clarifySentences(List<String> sentences);
}

