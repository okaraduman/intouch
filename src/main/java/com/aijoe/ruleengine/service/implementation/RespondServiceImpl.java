package com.aijoe.ruleengine.service.implementation;

import com.aijoe.ruleengine.service.RespondService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.aijoe.intouch.model.enumaration.CategoryNameEnum.NO_MATCH;
import static com.aijoe.ruleengine.model.constant.IntentResponseConstants.*;
import static com.aijoe.ruleengine.model.map.IntentCombinationRespondMap.RESPOND_COMBINATION_MAP;

@Service
public class RespondServiceImpl implements RespondService {

    @Override
    public String produceRespond(List<String> intentList, String companyName) {
        StringBuilder respondBuilder = new StringBuilder();
        String combinationKey = getCombinationKey(intentList);

        respondBuilder.append(RESPOND_HEADER);
        if (RESPOND_COMBINATION_MAP.containsKey(combinationKey)) {
            respondBuilder.append(RESPOND_COMBINATION_MAP.get(combinationKey));
        } else {
            String intentCombinationText = getOutputIntentCombinationText(intentList);
            respondBuilder.append(intentCombinationText).append(RESPOND_BODY_INTRO).append(RESPOND_BODY_END);
        }

        respondBuilder.append(RESPOND_FOOTER).append(companyName.toUpperCase(new Locale("tr")));
        return respondBuilder.toString();
    }

    private String getCombinationKey(List<String> intentList) {
        if (!CollectionUtils.isEmpty(intentList)) {
            if (intentList.size() > 1) {
                return String.join("-", intentList);
            } else {
                return intentList.get(0);
            }
        }
        return NO_MATCH;
    }

    private String getOutputIntentCombinationText(List<String> intentList) {
        List<String> keywords = Arrays.asList("Şikayetleri", "Sorunları");
        StringBuilder sb = new StringBuilder();
        keywords.stream().forEach(keyword -> {
            String replaceWord = " " + keyword;
            if (intentList.size() == 2) {
                if (intentList.get(0).contains(keyword) && intentList.get(1).contains(keyword)) {
                    sb.append(intentList.get(0).replaceAll(replaceWord, ""));
                    sb.append(" ve ").append(intentList.get(1));
                } else {
                    if (!sb.toString().contains(intentList.get(1)) && (intentList.get(0).contains(keyword) || intentList.get(1).contains(keyword)))
                        sb.append(intentList.get(0)).append(" ve ").append(intentList.get(1));
                }
            } else if (intentList.size() == 3) {
                if (intentList.get(0).contains(keyword)) {
                    String newTextForFirst = intentList.get(0).replace(replaceWord, "");
                    if (intentList.get(1).contains(keyword) && intentList.get(2).contains(keyword)) {
                        sb.append(newTextForFirst).append(", ").append(intentList.get(1).replaceAll(replaceWord, "")).append(" ve ").append(intentList.get(2));
                    } else if (intentList.get(1).contains(keyword)) {
                        sb.append(newTextForFirst).append(", ").append(intentList.get(1)).append(" ve ").append(intentList.get(2));
                    } else if (intentList.get(2).contains(keyword)) {
                        sb.append(newTextForFirst).append(", ").append(intentList.get(2)).append(" ve ").append(intentList.get(1));
                    } else {
                        if (!sb.toString().contains(intentList.get(1)) && (intentList.get(0).contains(keyword) || intentList.get(1).contains(keyword) || intentList.get(2).contains(keyword)))
                            sb.append(intentList.get(0)).append(", ").append(intentList.get(1)).append(" ve ").append(intentList.get(2));
                    }
                }
            }
        });

        if (sb.toString().length() < 1) {
            sb.append(String.join(", ", intentList));
        }
        return sb.toString();
    }

}
