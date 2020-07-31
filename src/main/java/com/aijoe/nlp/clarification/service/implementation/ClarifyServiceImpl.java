package com.aijoe.nlp.clarification.service.implementation;

import com.aijoe.nlp.clarification.service.ClarifyService;
import com.aijoe.socialmedia.util.PurifyingUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClarifyServiceImpl implements ClarifyService {
    public ClarifyServiceImpl() {
    }

    @Override
    public List<String> clarifySentences(List<String> sentences) {
        return sentences.stream().map(PurifyingUtils::clearSentence).collect(Collectors.toList());
    }

    @Override
    public String clarifySentences(String sentence) {
        return PurifyingUtils.clearSentence(sentence);
    }

}
