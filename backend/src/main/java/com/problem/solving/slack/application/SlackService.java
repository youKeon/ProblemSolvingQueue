package com.problem.solving.slack.application;

import com.problem.solving.global.error.ErrorResponse;
import com.problem.solving.problem.domain.Category;
import com.problem.solving.slack.dto.request.TodayProblemSolvingRequest;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.model.block.composition.TextObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;

@Service
@Slf4j
public class SlackService {
    private static final String ERROR_CHANNEL = "#장애확인";
    private static final String TODAY_PROBLEM_SOLVING_CHANNEL = "#오늘의 문제";
    @Value(value = "${slack.token}")
    String token;

    public void requestProblemCategory(Category category, )

    public void responseProblem(TodayProblemSolvingRequest request, String channel) throws IOException {
        String channelInfo = "";
        if(channel.equals("error")){
            channelInfo = ERROR_CHANNEL;
        } else if(channel.equals("TPS")){
            channelInfo = TODAY_PROBLEM_SOLVING_CHANNEL;
        }

        // Slack 메세지 보내기
        try {
            List<TextObject> textObjects = new ArrayList<>();
            textObjects.add(markdownText("*제목* : " + request.getTitle()));
            textObjects.add(markdownText("*링크* : " + request.getUrl()));
            textObjects.add(markdownText("*유형* : " + request.getCategory()));

            MethodsClient methods = Slack.getInstance().methods(token);
            ChatPostMessageRequest messageRequest = ChatPostMessageRequest.builder()
                    .channel(channelInfo)
                    .blocks(asBlocks(
                            header(header -> header.text(plainText("오늘의 추천 문제입니다!"))),
                            divider(),
                            section(section -> section.fields(textObjects)
                            ))).build();

            methods.chatPostMessage(messageRequest);
        } catch (SlackApiException | IOException e) {
            new Exception("서버 오류로 메시지 발송이 불가합니다.");
        }
    }
}
