package com.aijoe.ruleengine.service.implementation;

import com.aijoe.ruleengine.service.RespondService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
            String intentCombinationText = String.join(" ve ", intentList);
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


}
