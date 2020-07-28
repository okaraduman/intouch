package com.aijoe.ruleengine.model.map;

import java.util.HashMap;

import static com.aijoe.ruleengine.model.constant.IntentResponseConstants.KART_BORCU;

public class IntentCombinationRespondMap {
    public static final HashMap RESPOND_COMBINATION_MAP = new HashMap<String, String>() {{
        put(KART_BORCU, "");
    }};
}
