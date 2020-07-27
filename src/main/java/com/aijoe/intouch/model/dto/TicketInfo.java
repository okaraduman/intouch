package com.aijoe.intouch.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class TicketInfo implements Serializable {
    private String originalMessage;
    private String originalMessageUrl;
    private String summaryText;
    private List<String> intents;
    private String outputMessage;

    public TicketInfo() {
        intents = new ArrayList<>();
    }
}
