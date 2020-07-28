package com.aijoe.intouch.service.implementation;

import com.aijoe.intouch.config.IntouchProperties;
import com.aijoe.intouch.model.dto.TicketInfo;
import com.aijoe.intouch.model.output.FeedbackOutput;
import com.aijoe.intouch.service.IntouchService;
import com.aijoe.nlp.clarification.service.ClarifyService;
import com.aijoe.nlp.summary.service.SummaryService;
import com.aijoe.ruleengine.service.RespondService;
import com.aijoe.socialmedia.model.dto.SikayetVarInfo;
import com.aijoe.socialmedia.model.dto.TweetInfo;
import com.aijoe.socialmedia.service.SikayetVarService;
import com.aijoe.socialmedia.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import zemberek.classification.FastTextClassifier;
import zemberek.core.ScoredItem;
import zemberek.core.turkish.Turkish;
import zemberek.tokenization.TurkishTokenizer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static com.aijoe.intouch.model.enumaration.CategoryNameEnum.NO_MATCH;
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

    @Autowired
    RespondService respondService;

    @Autowired
    IntouchProperties intouchProperties;

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
        feedbackOutput.setTotalMessageCount(tweetList.size());
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
            //ticketInfo.setSummaryText(summaryService.getSummary(review.getMessage()));
            List<String> intentsWithLabel = classify(review.getMessage());
            ticketInfo.setIntents(getCategoryList(intentsWithLabel));

            List<String> intentEachSentence = classifyEachSentence(review.getMessage());
            ticketInfo.setIntentEachSentence(getCategoryList(intentEachSentence));
            ticketInfo.setIsIntentCountEqual(intentsWithLabel.size() == intentEachSentence.size());
            ticketInfo.setOutputMessage(respondService.produceRespond(intentsWithLabel, companyName));

            placeTicketsIntoCategory(ticketInfo, feedbackOutput);
        });
        feedbackOutput.setTotalMessageCount(reviewList.size());
        return feedbackOutput;
    }

    private List<String> classify(String message) {
        try {
            Path path = Paths.get("src/main/resources/datasets/corpus.model");
            FastTextClassifier classifier = FastTextClassifier.load(path);
            String processed = String.join(" ", TurkishTokenizer.DEFAULT.tokenizeToStrings(message));
            processed = processed.toLowerCase(Turkish.LOCALE);

            List<ScoredItem<String>> classifierResults = classifier.predict(processed, 3);

            if (!CollectionUtils.isEmpty(classifierResults)) {
                return classifierResults.stream().filter(Objects::nonNull).filter(t -> t.score > intouchProperties.getTreshold()).map(t -> t.item).collect(Collectors.toList());
            }
            return new ArrayList<String>() {{
                add(NO_MATCH);
            }};
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<String>() {{
                add(NO_MATCH);
            }};
        }
    }

    private List<String> classifyEachSentence(String messages) {
        try {
            Path path = Paths.get("src/main/resources/datasets/corpus.model");
            FastTextClassifier classifier = FastTextClassifier.load(path);
            String[] splittedMessage = messages.split("[.!?:]");
            Set<String> intentList = new HashSet<>();

            Arrays.asList(splittedMessage).stream().forEach(message -> {
                String processed = String.join(" ", TurkishTokenizer.DEFAULT.tokenizeToStrings(message));
                processed = processed.toLowerCase(Turkish.LOCALE);
                List<ScoredItem<String>> classifierResults = classifier.predict(processed, 3);
                if (!CollectionUtils.isEmpty(classifierResults)) {
                    intentList.addAll(classifierResults.stream().filter(Objects::nonNull).filter(t -> t.score > intouchProperties.getTreshold()).map(t -> t.item).collect(Collectors.toSet()));
                }
            });

            return intentList.stream().collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<String>() {{
                add(NO_MATCH);
            }};
        }
    }

    private List<String> getCategoryList(List<String> intents) {
        return intents.stream().filter(Objects::nonNull).map(this::getCategoryNameByIntent).collect(Collectors.toList());
    }

    private void placeTicketsIntoCategory(TicketInfo ticketInfo, FeedbackOutput feedbackOutput) {
        if (!CollectionUtils.isEmpty(ticketInfo.getIntents())) {
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
        } else {
            feedbackOutput.getCategoryList().put("NO_MATCH", new ArrayList<TicketInfo>() {{
                add(ticketInfo);
            }});
        }
    }

    private String getCategoryNameByIntent(String intent) {
        if (CATEGORY_NAME_INTENT_MAP.containsKey(intent)) {
            return CATEGORY_NAME_INTENT_MAP.get(intent);
        }
        return intent;
    }
}
