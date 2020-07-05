package com.aijoe.socialmedia.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PurifyingUtils {
    public static String clearSentence(String sentence){
        return removeEmojis(removeUrl(removeDoubleWhiteSpaces(removeTags(removeNewLineChars(sentence)))));
    }

    public static String removeWhiteSpaces(String text){
        return text.replaceAll(" ", "");
    }

    public static String removeDoubleWhiteSpaces(String text){
        return text.replaceAll("  ", " ");
    }

    public static String removeTags(String text){
        return text.replaceAll("[#|@]\\w+","");
    }

    public static String removeNewLineChars(String text){
        return text.replaceAll("\n","");
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
