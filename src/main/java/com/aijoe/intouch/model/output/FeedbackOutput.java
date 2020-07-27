package com.aijoe.intouch.model.output;

import com.aijoe.intouch.model.dto.TicketInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class FeedbackOutput implements Serializable {
    private Map<String, List<TicketInfo>> categoryList;
    private Integer totalMessageCount;

    public FeedbackOutput() {
        categoryList = new HashMap<>();
        totalMessageCount = 0;
    }
}
