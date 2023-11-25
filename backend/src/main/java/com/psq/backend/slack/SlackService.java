package com.psq.backend.slack;

import com.psq.backend.problem.dto.response.ProblemRecommendResponse;
import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import com.slack.api.webhook.Payload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.slack.api.webhook.WebhookPayloads.payload;


@Service @Slf4j
public class SlackService {

    @Value("${webhook.slack.url}")
    private String SLACK_WEBHOOK_URL;
    private final static String TODAY_PSQ_MESSAGE = "오늘의 추천 문제";

    public void sendMessage(ProblemRecommendResponse problem) {
        Slack slack = Slack.getInstance();
        Payload payload = generatePayload(problem);

        try {
            slack.send(SLACK_WEBHOOK_URL, payload);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Payload generatePayload(ProblemRecommendResponse problem) {
        Map<String, String> payloadDetails = new HashMap<>();

        payloadDetails.put("문제 URL", problem.getUrl());
        payloadDetails.put("문제 유형", problem.getCategory().toString());
        payloadDetails.put("문제 난이도", String.valueOf(problem.getLevel()));

        return payload(p -> p
                .text(TODAY_PSQ_MESSAGE)
                .attachments(List.of(
                        Attachment.builder().color(Color.BLUE.toString())
                                .fields(payloadDetails.keySet().stream()
                                        .map(key -> generateSlackField(key, payloadDetails.get(key)))
                                        .collect(Collectors.toList()))
                                .build()))
        );
    }

    private Field generateSlackField(String title, String value) {
        return Field.builder()
                .title(title)
                .value(value)
                .valueShortEnough(false)
                .build();
    }
}
