package com.aijoe.nlp.clarification.service.impl;

import com.aijoe.nlp.clarification.service.ClarifyService;
import org.springframework.stereotype.Service;
import zemberek.morphology.TurkishMorphology;
import zemberek.normalization.TurkishSpellChecker;
import zemberek.tokenization.Token;
import zemberek.tokenization.TurkishTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClarifyServiceImpl implements ClarifyService {

    private TurkishMorphology morphology;
    private TurkishSpellChecker spellChecker;
    private TurkishTokenizer tokenizer;

    public ClarifyServiceImpl(){
        morphology = TurkishMorphology.createWithDefaults();
        tokenizer = TurkishTokenizer.ALL;
        try {
            spellChecker = new TurkishSpellChecker(morphology);
        } catch (IOException e) {
            System.out.println("An error occured while creating spell checker...");
        }
    }

    @Override
    public List<String> clarifySentence(List<String> sentences) {
        return sentences.stream().map(this::fixSpellMistakes).collect(Collectors.toList());
    }

    private String fixSpellMistakes(String sentence) {
        StringBuilder output = new StringBuilder();
        for (Token token : tokenizer.tokenize(sentence)) {
            String text = token.getText();
            if (analyzeToken(token) && !spellChecker.check(text)) {
                List<String> strings = spellChecker.suggestForWord(token.getText());
                if (!strings.isEmpty()) {
                    String suggestion = strings.get(0);
                    output.append(suggestion);
                } else {
                    output.append(text);
                }
            } else {
                output.append(text);
            }
        }
        return output.toString();
    }

    private boolean analyzeToken(Token token) {
        return token.getType() != Token.Type.NewLine
                && token.getType() != Token.Type.SpaceTab
                && token.getType() != Token.Type.UnknownWord
                && token.getType() != Token.Type.RomanNumeral
                && token.getType() != Token.Type.Unknown;
    }
}
