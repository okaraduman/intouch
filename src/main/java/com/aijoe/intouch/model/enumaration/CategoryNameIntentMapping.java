package com.aijoe.intouch.model.enumaration;

import java.util.HashMap;

import static com.aijoe.intouch.model.enumaration.CategoryNameEnum.*;
import static com.aijoe.intouch.model.enumaration.IntentNameEnum.*;

public class CategoryNameIntentMapping {
    public final static HashMap<String, String> CATEGORY_NAME_INTENT_MAP = new HashMap<String, String>() {{
        put(LABEL_KART_UCRETI_IPTALI, KART_UCRETI_IPTALI);
        put(LABEL_CM_SIKAYETI, CM_SIKAYETI);
        put(LABEL_ATM_SORUNLARI, ATM_SORUNLARI);
        put(LABEL_KART_SORUNLARI, KART_SORUNLARI);
        put(LABEL_DIJITAL_KANAL_SIKAYETLERI, DIJITAL_KANAL_SIKAYETLERI);
        put(LABEL_KAMPANYA_SORUNLARI, KAMPANYA_SORUNLARI);
        put(LABEL_KOMISYON_UCRETLERI_SIKAYETI, KOMISYON_UCRETLERI_SIKAYETI);
        put(LABEL_SUBE_SIKAYETI, SUBE_SIKAYETI);
        put(LABEL_ODEME_SORUNLARI, ODEME_SORUNLARI);
        put(LABEL_HGS_OGS_SORUNLARI, HGS_OGS_SORUNLARI);
        put(LABEL_URUN_BASVURU_REDDI, URUN_BASVURU_REDDI);
        put(LABEL_URUN_IPTAL_SORUNLARI, URUN_IPTAL_SORUNLARI);
        put(LABEL_ILETISIM_TERCIHI_DEGISIKLIGI, ILETISIM_TERCIHI_DEGISIKLIGI);
        put(LABEL_PARA_TRANSFERI_SORUNLARI, PARA_TRANSFERI_SORUNLARI);
    }};
}
