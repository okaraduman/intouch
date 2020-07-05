package com.aijoe.socialmedia.util;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PurifyingUtils {
    public static String clearSentence(String sentence){
        return removeEmojis(removeQuotes(reorganizeDots(removeDoubleWhiteSpaces(removeTags(removeNewLineChars(removeUrl(sentence)))))));
    }

    public static String removeWhiteSpaces(String text){
        return text.replaceAll(" ", "");
    }

    public static String removeDoubleWhiteSpaces(String text){
        return text.replaceAll("  ", " ");
    }

    public static String reorganizeDots(String text) {
        text = text.replaceAll(" \\.",".");
        return text.replaceAll("\\.\\.",".");
    }

    public static String removeQuotes(String text) {
        return text.replaceAll("\"","");
    }

    public static String removeTags(String text){
        List<String> sentences = Arrays.asList(text.split("\\. "));
        StringBuilder sb = new StringBuilder();
        for(String sentence : sentences){
            if(sentences.size()>1 && sentences.indexOf(sentence) == sentences.size()-1){
                sentence = sentence.replaceAll("[#|@][\\p{L}\\p{N}ın\\s]+","").replaceAll("\\.","");
            }
            sb.append(sentence).append(".").append(" ");
        }
        return sb.toString().trim().replaceAll("[#|@]","");
    }

    public static String removeNewLineChars(String text){
        String[] sentences = text.split("\n");
        StringBuilder sb = new StringBuilder();
        for(String sentence : sentences){
            if(!StringUtils.isEmpty(sentence))
                sb.append(sentence.trim()).append(".").append(" ");
        }
        return sb.toString().trim();
    }

    public static String removeEmojis(String text){
        return text.replaceAll("[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]","");
    }

    public static String removeUrl(String text) {
        String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern p = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        int i = 0;
        while (m.find()) {
            text = text.replaceAll(m.group(i),"").trim();
            i++;
        }
        return text;
    }
}
