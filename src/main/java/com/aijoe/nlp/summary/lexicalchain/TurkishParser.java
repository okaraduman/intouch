package com.aijoe.nlp.summary.lexicalchain;

import org.springframework.stereotype.Service;
import zemberek.morphology.TurkishMorphology;

import java.io.IOException;

//tek bir tane olusturmamiz gerek cunku cok zaman aliyor yaklasik 1,5 sn
//server icin iyi degil , bu yuzden bu sekilde hizlandirildi
@Service
public class TurkishParser {
    TurkishMorphology parser;

    public TurkishParser() throws IOException {
        super();
        //parser ı burda oluşturmayıp fonksiyon içinde oluşturursak çok zaman alıyor!
        this.parser = TurkishMorphology.createWithDefaults();
    }

    public TurkishMorphology getParser() {
        return this.parser;
    }
}
