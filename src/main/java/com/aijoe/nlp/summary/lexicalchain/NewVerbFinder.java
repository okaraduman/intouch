package com.aijoe.nlp.summary.lexicalchain;

import zemberek.morphology.TurkishMorphology;
import zemberek.morphology.analysis.WordAnalysis;

public class NewVerbFinder {

    TurkishMorphology parser;

    public String parse(String word) {
        boolean temp = true;
        WordAnalysis wordAnalysis = this.parser.analyze(word);
        /*for (MorphParse parse : parses) {
            if (parse.formatOnlyIgs().toString().contains("Verb")
                    && !parse.formatOnlyIgs().toString().contains("Noun")
                    && temp) {
                temp = false;
                String verbRoot = parse.getLemma();
                return verbRoot;
            }
        }*/
        return "notVerb";
    }

}
