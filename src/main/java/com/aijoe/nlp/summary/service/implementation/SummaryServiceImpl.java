package com.aijoe.nlp.summary.service.implementation;

import com.aijoe.nlp.summary.lexicalchain.NewLexical;
import com.aijoe.nlp.summary.lexicalchain.NewPreprocess;
import com.aijoe.nlp.summary.lexicalchain.TurkishParser;
import com.aijoe.nlp.summary.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class SummaryServiceImpl implements SummaryService {
    private static final Logger LOGGER = Logger.getLogger(SummaryServiceImpl.class.getName());

    @Autowired
    TurkishParser turkishParser;

    @Override
    public List<String> getSummary(List<String> messageList) {
        List<String> summaryList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            NewPreprocess preprocess = new NewPreprocess(turkishParser);

            for (String message : messageList) {
                String cleanText = preprocess.cleanStopWords(message);
                List<NewLexical> lexicals = preprocess.getAllLexicals(cleanText, message);
                stringBuilder.append(preprocess.createChains(lexicals));
                summaryList.add(stringBuilder.toString());
                stringBuilder.setLength(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return summaryList;
    }
}
