package com.aijoe.socialmedia.service.impl;

import com.aijoe.socialmedia.config.SikayetVarProperties;
import com.aijoe.socialmedia.service.SikayetVarService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

@Service
public class SikayetVarServiceImpl implements SikayetVarService {
    private static final Logger LOGGER = Logger.getLogger(SikayetVarServiceImpl.class.getName());

    @Autowired
    SikayetVarProperties sikayetVarProperties;

    @Override
    public List<String> getReviews(String companyName) {
        companyName = getCompanyName(companyName);
        StringBuilder stringBuilder = new StringBuilder();
        String url = stringBuilder.append(sikayetVarProperties.getUrl()).append(companyName).toString();
        List<String> reviewList = new ArrayList<>();

        for (int pageNo = 1; pageNo <= sikayetVarProperties.getMaxPageCount(); pageNo++) {
            Document document;
            if (pageNo != 1) {
                StringBuilder newBuilder = new StringBuilder();
                document = callUrl(newBuilder.append(url).append("?").append("page=").append(pageNo).toString());
            } else {
                document = callUrl(url);
            }
            Elements elementsOfPage = getAllElementsOfPage(document);
            reviewList.addAll(getElementsText(elementsOfPage));
        }

        return reviewList;
    }

    private List<String> getElementsText(Elements elements) {
        List<String> textList = new ArrayList<>();
        for (Element element : elements) {
            if (!isReadMoreEmpty(element)) {
                String fullTextUrl = getFullTextUrl(element);
                Document document = callUrl(fullTextUrl);
                String text = getCleanText(document);
                textList.add(text);
            }
        }
        return textList;
    }

    private Document callUrl(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            LOGGER.info("An error occured while connecting " + url);
        }
        return doc;
    }

    private Elements getAllElementsOfPage(Document document) {
        return document.select("[class^=card-text]");
    }

    private boolean isReadMoreEmpty(Element element) {
        return StringUtils.isEmpty(getElementUrlSuffix(element));
    }

    private String getFullTextUrl(Element element) {
        String urlSuffix = getElementUrlSuffix(element);
        return new StringBuilder().append(sikayetVarProperties.getUrl()).append(urlSuffix).toString();
    }

    private String getElementUrlSuffix(Element element) {
        return element.select("a").attr("href");
    }

    private String getCleanText(Document document) {
        return document.select("[class^=card-text]").text().replaceAll("<[^>]*>", "");
    }

    private String getCompanyName(String companyName){
        return companyName.replaceAll(" ", "-").toLowerCase(new Locale("tr"));
    }
}
