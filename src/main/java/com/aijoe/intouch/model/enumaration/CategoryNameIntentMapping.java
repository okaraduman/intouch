package com.aijoe.intouch.model.enumaration;

import java.util.HashMap;

public class CategoryNameIntentMapping {
    public final static HashMap<String, String> CATEGORY_NAME_INTENT_MAP = new HashMap<String, String>() {{
        put("__label__kartborcu", CategoryNameEnum.KART_BORCU);
        put("__label__kartiptal", CategoryNameEnum.KART_IPTAL);
    }};
}
