package com.aijoe.nlp.clarification.service.impl;

import Corpus.Sentence;
import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import Ngram.NGram;
import Ngram.NoSmoothing;
import SpellChecker.NGramSpellChecker;
import com.aijoe.nlp.clarification.service.ClarifyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClarifyServiceImpl implements ClarifyService {

    @Override
    public List<String> fixSpellMistake(List<String> sentences) {

        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer();
        NGram<String> nGram = new NGram<String>("ngram.txt");
        nGram.calculateNGramProbabilities(new NoSmoothing<>());
        NGramSpellChecker nGramSpellChecker = new NGramSpellChecker(fsm, nGram);
        List<String> correctList = new ArrayList<>();
        for(String sentence : sentences){
            Sentence sentenceSample = new Sentence(sentence);
            correctList.add(nGramSpellChecker.spellCheck(sentenceSample).toString());
        }
        return correctList;
    }
}
