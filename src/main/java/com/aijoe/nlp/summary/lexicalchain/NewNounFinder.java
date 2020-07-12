package com.aijoe.nlp.summary.lexicalchain;

import zemberek.morphology.analysis.WordAnalysis;
import zemberek.tokenization.TurkishTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewNounFinder {

    TurkishParser parser;

    public NewNounFinder(TurkishParser p) throws IOException {
        super();
        this.parser = p;
    }

    public List<String> simpleTokenization(String input) {
        TurkishTokenizer lexer = TurkishTokenizer.ALL;
        return lexer.tokenizeToStrings(input);
    }

    public String parse(String word) {
        WordAnalysis wordAnalysis = this.parser.parser.analyze(word);

        List<String> nouns = new ArrayList<String>();
        wordAnalysis.getAnalysisResults().stream().filter(t-> (t.getDictionaryItem().toString().contains("Noun") && !t.getDictionaryItem().toString().contains("Verb"))).forEach(t-> nouns.addAll(t.getLemmas()));

        if (nouns.size() < 1) {
            return "notNoun";
        } else {
            for (int i = 0; i < nouns.size(); i++) {
                for (int j = 0; j < nouns.size(); j++) {
                    if (word.length() - nouns.get(j).length() == (i + 1)) {
                        return nouns.get(j);
                    }
                }
            }
        }
        return nouns.get(0);
    }

    public List<String> getNounRoots(String textFile) {
        List<String> tokened = new ArrayList<String>();
        List<String> nouns = new ArrayList<String>();
        // boşluğa göre kelimeleri ayırdı
        tokened = simpleTokenization(textFile);
        for (int i = 0; i < tokened.size(); i++) {
            String root = parse(tokened.get(i).toString());
            if (root != "notNoun") {
                nouns.add(root);
            }
        }
        return nouns;
    }

}
