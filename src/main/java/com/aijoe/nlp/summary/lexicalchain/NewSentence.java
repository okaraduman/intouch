package com.aijoe.nlp.summary.lexicalchain;

import zemberek.tokenization.TurkishSentenceExtractor;

import java.util.List;

public class NewSentence {

    public List<String> getSentences(String text) {
        TurkishSentenceExtractor extractor = TurkishSentenceExtractor.DEFAULT;
        List<String> sentences = extractor.fromParagraph(text);
        return sentences;
    }

    //ilk cümleyi alır ve başlık sonunda bir noktalama işareti olmadığı için, satırlara böler, ilk satırı başlık olarak geri dönderir
    public String getTitleSentence(List<String> sentences) {
        String[] title = sentences.get(0).split("\\r?\\n");
        return title[0];
    }

}
