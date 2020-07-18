package com.aijoe.nlp.clarification.service.implementation;

import com.aijoe.nlp.clarification.service.ClarifyService;
import org.springframework.stereotype.Service;
import zemberek.morphology.TurkishMorphology;
import zemberek.normalization.TurkishSentenceNormalizer;
import zemberek.normalization.TurkishSpellChecker;
import zemberek.tokenization.Token;
import zemberek.tokenization.TurkishTokenizer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ClarifyServiceImpl implements ClarifyService {
    private static final Logger LOGGER = Logger.getLogger(ClarifyServiceImpl.class.getName());


    private TurkishMorphology morphology;
    private TurkishSpellChecker spellChecker;
    private TurkishTokenizer tokenizer;
    private TurkishSentenceNormalizer normalizer;

    public ClarifyServiceImpl() {
        morphology = TurkishMorphology.createWithDefaults();
        tokenizer = TurkishTokenizer.ALL;
        try {
            spellChecker = new TurkishSpellChecker(morphology);
            Path lookupRoot = Paths.get("data/normalization");
            Path lmFile = Paths.get("data/lm/lm.2gram.slm");
            normalizer = new TurkishSentenceNormalizer(morphology, lookupRoot, lmFile);

        } catch (IOException e) {
            LOGGER.info("An error occured while creating spell checker...");
        }
    }

    @Override
    public List<String> clarifySentence(List<String> sentences) {
        return sentences.stream().map(this::normalizeSentence).collect(Collectors.toList());
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

    private String normalizeSentence(String sentence){
        return normalizer.normalize(sentence);
    }

    private boolean analyzeToken(Token token) {
        return token.getType() != Token.Type.NewLine
                && token.getType() != Token.Type.SpaceTab
                && token.getType() != Token.Type.UnknownWord
                && token.getType() != Token.Type.RomanNumeral
                && token.getType() != Token.Type.Unknown;
    }
}
