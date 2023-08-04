package com.problem.solving.slack.presentation;

import com.problem.solving.slack.application.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/slack")
public class SlackController {
    private final SlackService slackService;

    @GetMapping("/slack/TPS")
    public void error(){
        slackService.sendSlackMessage("", "error");
    }

}
