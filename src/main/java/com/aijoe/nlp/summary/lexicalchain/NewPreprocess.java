package com.aijoe.nlp.summary.lexicalchain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;


public class NewPreprocess {
    private static final Logger LOGGER = Logger.getLogger(NewPreprocess.class.getName());

    NewNounFinder findNoun;
    NewVerbFinder findVerb;
    private NewSentence sentenceOperation;
    private NewParagraph paragraphs;
    NewLexicalChain lexicalChain;
    NewChainScores chainScores;
    List<String> cleanedParagraphsOfText;
    List<String> cleanedSentencesOfText;
    List<String> originalParagraphsOfText;
    List<String> originalSentencesOfText;
    String titleOfText;
    String classOfText;
    NewExtractSentences extractSentence;
    NewClassifier classifier;

    public NewPreprocess(TurkishParser tp) throws IOException {
        super();
        this.findNoun = new NewNounFinder(tp);
        this.findVerb = new NewVerbFinder();
        this.setSentenceOperation(new NewSentence());
        this.setParagraphs(new NewParagraph());
        this.lexicalChain = new NewLexicalChain();
        this.chainScores = new NewChainScores();
        this.cleanedParagraphsOfText = new ArrayList<String>();
        this.cleanedSentencesOfText = new ArrayList<String>();
        this.originalParagraphsOfText = new ArrayList<String>();
        this.originalSentencesOfText = new ArrayList<String>();
        this.extractSentence = new NewExtractSentences();
        this.titleOfText = "";
        this.classifier = new NewClassifier();
    }


    public List<String> getAllNouns(String textFile) throws IOException {
        List<String> nouns = new ArrayList<String>();
        // noktalamaları kaldır
        // "’" işareti türkçede
        String strippedInput = textFile.replaceAll("\\p{Punct}+", "").replaceAll("’", "");
        strippedInput = strippedInput.replaceAll("\\d", "");
        nouns = this.findNoun.getNounRoots(strippedInput);
        return nouns;
    }

    // burda stopwords listesindeki tüm kelimeler çıkartılacak text içinden
    public String cleanStopWords(String textFile) throws IOException {
        String stopWordsFilename = "/datasets/stopwordsTurkish.txt";
        String getStopWords = getFile(stopWordsFilename);
        String stopWordsText = readTextFile(getStopWords);
        stopWordsText = stopWordsText.toLowerCase();
        String stopWords[] = stopWordsText.split("\\r?\\n");
        List<String> stopWordSet = new ArrayList<String>(Arrays.asList(stopWords));
        textFile = textFile.toLowerCase();
        StringBuilder result = new StringBuilder(textFile.length());
        for (String s : textFile.split(" ")) {
            if (!stopWordSet.contains(s))
                result.append(s + " ");
        }

        return result.toString();
    }

    private String getFile(String fileName) {

        StringBuilder result = new StringBuilder("");

        // Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();

    }

    public String readTextFile(String fileName) throws IOException {
        String[] split = fileName.split("\\.");
        String ext = split[split.length - 1];
        if (ext.equals("txt")) {
            BufferedReader input = new BufferedReader(new FileReader(fileName));
            StringBuilder sb = new StringBuilder();
            String line = input.readLine();
            while ((line != null)) // metin dosyası satır satır okundu.
            {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = input.readLine();
            }
            input.close();
            String text = sb.toString();
            if (text.length() < 10) {
                return "Özetlenemeyecek kadar kısa bir metin..";
            }

            return text;
        } else {
            return "Wrong extension of text file..";
        }
    }

    public List<NewLexical> getAllLexicals(String cleanedText, String originalText) throws IOException {
        int paragraphNo = 0;
        int sentenceNo = 0;
        List<NewLexical> lexicals = new ArrayList<NewLexical>();
        List<String> tempParagraphsOfText = this.getParagraphs().getParagraphs(cleanedText);
        List<String> tempOriginalParagraphsOfText = this.getParagraphs().getParagraphs(originalText);
        for (int i = 0; i < tempParagraphsOfText.size(); i++) { // tüm paragrafları yaz.
            List<String> sentences = this.getSentenceOperation().getSentences(tempParagraphsOfText.get(i));
            List<String> originalSentences = this.getSentenceOperation().getSentences(tempOriginalParagraphsOfText.get(i));
            if (i == 0 && sentences.size() == 1) {
                this.titleOfText = sentences.get(i);
            } else {
                for (int j = 0; j < sentences.size() - 1; j++) { // paragrafın içindeki tüm
                    // cümleleri yaz.
                    List<String> nounsOfText = getAllNouns(sentences.get(j));
                    for (String noun : nounsOfText) {
                        NewLexical lexical = new NewLexical(noun, sentenceNo, paragraphNo);
                        lexicals.add(lexical);
                    }
                    sentenceNo = sentenceNo + 1;
                    this.cleanedSentencesOfText.add(sentences.get(j));
                    this.originalSentencesOfText.add(originalSentences.get(j));
                }
                paragraphNo = paragraphNo + 1;
                this.cleanedParagraphsOfText.add(tempParagraphsOfText.get(i));
                this.originalParagraphsOfText.add(tempOriginalParagraphsOfText.get(i));
            }
        }
        return lexicals;
    }

    public String createChains(List<NewLexical> lexicals) throws IOException {
        List<NewChain> chains = lexicalChain.chainBuilder(lexicals); //Burası cok uzun sürüyor
        LOGGER.info("#############All Chains: " + chains.size());
        List<NewChain> uniqueChains = lexicalChain.chainAnalyse(chains);
        LOGGER.info("#############Unique Chains: " + uniqueChains.size());
        uniqueChains = chainScores.calculateChainScores(uniqueChains);
        uniqueChains = chainScores.calculateChainStrengths(uniqueChains);
        List<NewChain> strongChains = chainScores.getStrongChains(uniqueChains);
        LOGGER.info("#############Strong Chains: " + strongChains.size());
//		for(NewChain c : strongChains){
//			LOGGER.info(c.getChainInformation());
//		}
        //basarisiz algoritma
//		List<String> extractedSentences1 = this.extractSentence.heuristic1(strongChains, this.originalParagraphsOfText,
//				this.originalSentencesOfText);
//		System.out.println("#############Heuristic1 OUTPUT############");
//		for (String sentence : extractedSentences1) {
//			System.out.println(sentence);
//		}
        List<String> extractedSentences2 = this.extractSentence.heuristic3(strongChains, this.originalParagraphsOfText, this.originalSentencesOfText);
        LOGGER.info("#############Heuristic2 OUTPUT: " + extractedSentences2);

        //basarisiz algoritma
//		List<String> extractedSentences3 = this.extractSentence.heuristic3(strongChains, this.originalParagraphsOfText, this.originalSentencesOfText);
//		System.out.println("#############Heuristic3 OUTPUT############");
//		for (String sentence : extractedSentences3) {
//			System.out.println(sentence);
//		}
        this.originalParagraphsOfText = new ArrayList<>();
        this.originalSentencesOfText = new ArrayList<>();
        strongChains = new ArrayList<>();
        return extractedSentences2.toString();
    }


    public static void main(String[] args) throws IOException {


    }

    public NewParagraph getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(NewParagraph paragraphs) {
        this.paragraphs = paragraphs;
    }

    public NewSentence getSentenceOperation() {
        return sentenceOperation;
    }

    public void setSentenceOperation(NewSentence sentenceOperation) {
        this.sentenceOperation = sentenceOperation;
    }

}
