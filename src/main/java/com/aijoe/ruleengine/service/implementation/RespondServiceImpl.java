package com.aijoe.ruleengine.service.implementation;

import com.aijoe.ruleengine.service.RespondService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

import static com.aijoe.ruleengine.model.constant.IntentResponseConstants.RESPOND_FOOTER;
import static com.aijoe.ruleengine.model.constant.IntentResponseConstants.RESPOND_HEADER;
import static com.aijoe.ruleengine.model.map.IntentCombinationRespondMap.RESPOND_COMBINATION_MAP;

@Service
public class RespondServiceImpl implements RespondService {

    @Override
    public String produceRespond(List<String> intentList, String companyName) {
        StringBuilder respondBuilder = new StringBuilder();
        String combinationKey = String.join("-", intentList);

        respondBuilder.append(RESPOND_HEADER);
        if (RESPOND_COMBINATION_MAP.containsKey(combinationKey)) {
            respondBuilder.append(RESPOND_COMBINATION_MAP.get(combinationKey));
        } else {
            intentList.stream().forEach(intent -> {
                if (RESPOND_COMBINATION_MAP.containsKey(intent)) {
                    respondBuilder.append(RESPOND_COMBINATION_MAP.get(intent)).append(" ");
                }
            });
        }

        respondBuilder.append(RESPOND_FOOTER).append(companyName.toUpperCase(new Locale("tr")));
        return respondBuilder.toString();
    }
}
