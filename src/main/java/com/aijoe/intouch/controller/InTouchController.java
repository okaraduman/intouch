package com.aijoe.intouch.controller;

import com.aijoe.intouch.model.output.FeedbackOutput;
import com.aijoe.intouch.service.IntouchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/intouch")
public class InTouchController {
    @Autowired
    IntouchService intouchService;

    @GetMapping("/twitter/{companyName}")
    public FeedbackOutput getTwitterFeedbacks(@PathVariable("companyName") String companyName) {
        return intouchService.getTwitterFeedbacks(companyName);
    }

    @GetMapping("/sikayetvar/{companyName}")
    public FeedbackOutput getSikayetvarFeedbacks(@PathVariable("companyName") String companyName) {
        return intouchService.getSikayetvarFeedbacks(companyName);
    }

}
