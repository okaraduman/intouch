package com.aijoe.intouch.service.implementation;

import com.aijoe.intouch.model.dto.TicketInfo;
import com.aijoe.intouch.model.output.FeedbackOutput;
import com.aijoe.intouch.service.IntouchService;
import com.aijoe.nlp.clarification.service.ClarifyService;
import com.aijoe.nlp.summary.service.SummaryService;
import com.aijoe.socialmedia.model.dto.SikayetVarInfo;
import com.aijoe.socialmedia.model.dto.TweetInfo;
import com.aijoe.socialmedia.service.SikayetVarService;
import com.aijoe.socialmedia.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zemberek.classification.FastTextClassifier;
import zemberek.core.ScoredItem;
import zemberek.core.turkish.Turkish;
import zemberek.tokenization.TurkishTokenizer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.aijoe.intouch.model.enumaration.CategoryNameIntentMapping.CATEGORY_NAME_INTENT_MAP;

@Service
public class IntouchServiceImpl implements IntouchService {
    @Autowired
    TwitterService twitterService;

    @Autowired
    ClarifyService clarifyService;

    @Autowired
    SikayetVarService sikayetVarService;

    @Autowired
    SummaryService summaryService;

    @Override
    public FeedbackOutput getTwitterFeedbacks(String companyName) {
        FeedbackOutput feedbackOutput = new FeedbackOutput();
        Set<TweetInfo> tweetList = twitterService.searchByCompanyName(companyName);
        tweetList.stream().forEach(tweet -> {
            TicketInfo ticketInfo = new TicketInfo();
            ticketInfo.setOriginalMessage(tweet.getMessage());
            ticketInfo.setOriginalMessageUrl(tweet.getUrl());
            String cleanText = clarifyService.clarifySentence(Arrays.asList(tweet.getMessage())).stream().filter(Objects::nonNull).findFirst().get();
            ticketInfo.setSummaryText(cleanText);
            ticketInfo.setIntents(Arrays.asList("Osman", "Burak", "Kazkilinc"));
            ticketInfo.setOutputMessage("This is a test for twitter...");

            placeTicketsIntoCategory(ticketInfo, feedbackOutput);
        });

        return feedbackOutput;
    }

    @Override
    public FeedbackOutput getSikayetvarFeedbacks(String companyName) {
        FeedbackOutput feedbackOutput = new FeedbackOutput();
        List<SikayetVarInfo> reviewList = sikayetVarService.getReviews(companyName);
        reviewList.stream().forEach(review -> {
            TicketInfo ticketInfo = new TicketInfo();
            ticketInfo.setOriginalMessage(review.getMessage());
            ticketInfo.setOriginalMessageUrl(review.getUrl());
            ticketInfo.setSummaryText(summaryService.getSummary(review.getMessage()));
            ticketInfo.setIntents(Arrays.asList("Ayca", "Limoncello", "Topal"));
            ticketInfo.setOutputMessage("This is a test for sikayetvar...");

            placeTicketsIntoCategory(ticketInfo, feedbackOutput);
        });

        return feedbackOutput;
    }

    private void classifyMessage(String message) {
        try {
            Path path = Paths.get("src/main/resources/corpus/new2.model");
            FastTextClassifier classifier = FastTextClassifier.load(path);

            String s = "İnternetim gitti ttnet.";

            String processed = String.join(" ", TurkishTokenizer.DEFAULT.tokenizeToStrings(s));
            processed = processed.toLowerCase(Turkish.LOCALE);

            // results, only top three.
            List<ScoredItem<String>> res = classifier.predict(processed, 3);

            for (ScoredItem<String> re : res) {
                System.out.println(re);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void placeTicketsIntoCategory(TicketInfo ticketInfo, FeedbackOutput feedbackOutput) {
        ticketInfo.getIntents().stream().forEach(intent -> {
            String categoryName = getCategoryNameByIntent(intent);
            if (feedbackOutput.getCategoryList().containsKey(categoryName)) {
                feedbackOutput.getCategoryList().get(categoryName).add(ticketInfo);
            } else {
                feedbackOutput.getCategoryList().put(categoryName, new ArrayList<TicketInfo>() {{
                    add(ticketInfo);
                }});
            }
        });
    }

    private String getCategoryNameByIntent(String intent) {
        if (CATEGORY_NAME_INTENT_MAP.containsKey(intent)) {
            return CATEGORY_NAME_INTENT_MAP.get(intent);
        }
        return intent;
    }
}
