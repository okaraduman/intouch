package com.aijoe.nlp.clarification.service.impl;

import Corpus.Sentence;
import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import Ngram.NGram;
import Ngram.NoSmoothing;
import SpellChecker.NGramSpellChecker;
import SpellChecker.SimpleSpellChecker;
import com.aijoe.nlp.clarification.service.ClarifyService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import zemberek.morphology.TurkishMorphology;
import zemberek.normalization.TurkishSpellChecker;
import zemberek.tokenization.Token;
import zemberek.tokenization.TurkishTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ClarifyServiceImpl implements ClarifyService {

    @Override
    public List<String> fixSpellMistake(List<String> sentences) {

        TurkishMorphology morphology = TurkishMorphology.createWithDefaults();
        List<String> newSentences = new ArrayList<>();
        try {
            TurkishSpellChecker spellChecker = new TurkishSpellChecker(morphology);
            StringBuilder sb = new StringBuilder();
            sentences.stream().forEach(sentence ->{
                newSentences.add(sentence);
                newSentences.add(doSth(sentence));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newSentences;
    }

    public String doSth(String sentence){
        TurkishTokenizer tokenizer = TurkishTokenizer.ALL;
        TurkishMorphology morphology = TurkishMorphology.createWithDefaults();
        TurkishSpellChecker spellChecker = null;
        try {
            spellChecker = new TurkishSpellChecker(morphology);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    static boolean analyzeToken(Token token) {
        return token.getType() != Token.Type.NewLine
                && token.getType() != Token.Type.SpaceTab
                && token.getType() != Token.Type.UnknownWord
                && token.getType() != Token.Type.RomanNumeral
                && token.getType() != Token.Type.Unknown;
    }
}
