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
    private TwitterService twitterService;
    private SikayetVarService sikayetVarService;
    private ClarifyService clarifyService;
    private SummaryService summaryService;
    private RespondService respondService;
    private IntouchProperties intouchProperties;

    @Autowired
    public IntouchServiceImpl(TwitterService twitterService, SikayetVarService sikayetVarService, ClarifyService clarifyService, SummaryService summaryService, RespondService respondService, IntouchProperties intouchProperties) {
        this.twitterService = twitterService;
        this.sikayetVarService = sikayetVarService;
        this.clarifyService = clarifyService;
        this.summaryService = summaryService;
        this.respondService = respondService;
        this.intouchProperties = intouchProperties;
    }

    @Override
    public FeedbackOutput getTwitterFeedbacks(String companyName) {
        FeedbackOutput feedbackOutput = new FeedbackOutput();
        String originalCompanyName = companyName;
        Set<TweetInfo> tweetList = twitterService.searchByCompanyName(companyName);
        tweetList.stream().forEach(tweet -> {
            TicketInfo ticketInfo = new TicketInfo();
            ticketInfo.setOriginalMessage(tweet.getMessage());
            ticketInfo.setOriginalMessageUrl(tweet.getUrl());
            ticketInfo.setSummaryText(clarifyService.clarifySentences(tweet.getMessage()));
            List<String> intentsWithLabel = classify(ticketInfo.getSummaryText(), true);
            ticketInfo.setIntents(getCategoryList(intentsWithLabel));
            ticketInfo.setOutputMessage(respondService.produceRespond(ticketInfo.getIntents(), originalCompanyName));

            placeTicketsIntoCategory(ticketInfo, feedbackOutput);
        });
        feedbackOutput.setTotalMessageCount(tweetList.size());
        return feedbackOutput;
    }

    @Override
    public FeedbackOutput getSikayetvarFeedbacks(String companyName) {
        FeedbackOutput feedbackOutput = new FeedbackOutput();
        String originalCompanyName = companyName;
        List<SikayetVarInfo> reviewList = sikayetVarService.getReviews(companyName);
        reviewList.stream().forEach(review -> {
            TicketInfo ticketInfo = new TicketInfo();
            ticketInfo.setOriginalMessage(review.getMessage());
            ticketInfo.setOriginalMessageUrl(review.getUrl());
            ticketInfo.setSummaryText(summaryService.getSummary(review.getMessage()));
            List<String> intentsWithLabel = classify(review.getMessage(), false);
            if (CollectionUtils.isEmpty(intentsWithLabel)) {
                intentsWithLabel = classify(ticketInfo.getSummaryText(), true);

            }
            ticketInfo.setIntents(getCategoryList(intentsWithLabel));
            ticketInfo.setOutputMessage(respondService.produceRespond(ticketInfo.getIntents(), originalCompanyName));

            placeTicketsIntoCategory(ticketInfo, feedbackOutput);
        });
        feedbackOutput.setTotalMessageCount(reviewList.size());
        return feedbackOutput;
    }

    private List<String> classify(String messageText, boolean isSummary) {
        try {
            Set<String> intentList = new HashSet<>();
            Path path = Paths.get("src/main/resources/datasets/corpus.model");
            FastTextClassifier classifier = FastTextClassifier.load(path);

            if (isSummary) {
                addIntoIntentList(intentList, classifier, messageText);
            } else {
                String[] splittedMessage = messageText.split("[.!?:]");
                Arrays.asList(splittedMessage).stream().forEach(message -> {
                    addIntoIntentList(intentList, classifier, message);
                });
            }
            return intentList.stream().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<String>() {{
                add(NO_MATCH);
            }};
        } catch (Exception e) {
            return new ArrayList<String>() {{
                add(NO_MATCH);
            }};
        }
    }

    private void addIntoIntentList(Set<String> intentList, FastTextClassifier classifier, String message) {
        String processed = String.join(" ", TurkishTokenizer.DEFAULT.tokenizeToStrings(message));
        processed = processed.toLowerCase(Turkish.LOCALE);
        List<ScoredItem<String>> classifierResults = classifier.predict(processed, 3);
        if (!CollectionUtils.isEmpty(classifierResults)) {
            intentList.addAll(classifierResults.stream().filter(Objects::nonNull).filter(t -> t.score > intouchProperties.getTreshold()).map(t -> t.item).collect(Collectors.toSet()));
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
            feedbackOutput.getCategoryList().put(NO_MATCH, new ArrayList<TicketInfo>() {{
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
