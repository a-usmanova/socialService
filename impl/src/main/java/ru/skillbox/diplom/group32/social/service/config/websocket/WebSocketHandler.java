package ru.skillbox.diplom.group32.social.service.config.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.skillbox.diplom.group32.social.service.config.security.JwtTokenProvider;
import ru.skillbox.diplom.group32.social.service.mapper.dialog.streaming.StreamingMapper;
import ru.skillbox.diplom.group32.social.service.model.account.AccountOnlineDto;
import ru.skillbox.diplom.group32.social.service.model.dialog.message.MessageDto;
import ru.skillbox.diplom.group32.social.service.model.streaming.StreamingDataDto;
import ru.skillbox.diplom.group32.social.service.model.streaming.StreamingMessageDto;
import ru.skillbox.diplom.group32.social.service.service.account.AccountService;
import ru.skillbox.diplom.group32.social.service.service.dialog.DialogService;
import ru.skillbox.diplom.group32.social.service.utils.websocket.WebsocketContextUtil;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.HashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, StreamingMessageDto> kafkaDialogTemplate;
    private final KafkaTemplate<String, AccountOnlineDto> kafkaNotificationTemplate;
    private final DialogService dialogService;
    private final StreamingMapper streamingMapper;
    private final AccountService accountService;

    @KafkaListener(topics = "sender-message", containerFactory = "dialogListener")
    public void sendToSocketMessage(StreamingMessageDto streamingMessageDto) throws IOException {

        log.info("Received message - {}", streamingMessageDto);
        if (streamingMessageDto != null && WebsocketContextUtil.contextContains(streamingMessageDto.getAccountId())) {
            WebsocketContextUtil.getSessionFromContext(streamingMessageDto.getAccountId())
                    .sendMessage(new TextMessage(objectMapper.writeValueAsString(streamingMessageDto)));
            log.info("To user with id: {}, send message to webSocket: {}", streamingMessageDto.getAccountId(), streamingMessageDto);
        }

    }

    @KafkaListener(topics = "sender-account-online", containerFactory = "accountOnlineListener")
    public void sendToSocketNotification(AccountOnlineDto accountOnlineDto) {

        log.info("Received account online notification - {}", accountOnlineDto);
        if (accountOnlineDto != null) {
            accountService.updateAccountOnline(accountOnlineDto);
        }

    }

    public void receiveMessage(StreamingMessageDto streamingMessageDto) {

        kafkaDialogTemplate.send("sender-message", streamingMessageDto);

    }

    public void changeAccountOnline(AccountOnlineDto accountOnlineDto) {

        kafkaNotificationTemplate.send("sender-account-online", accountOnlineDto);

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {

        String token = session.getHandshakeHeaders().get("cookie").get(0).substring(4);

        Long id = jwtTokenProvider.getCurrentUserIdFromJwt(token);
        log.info("Connection established with userId: {}", id);

        AccountOnlineDto accountOnlineDto = new AccountOnlineDto(id, null, true);
        changeAccountOnline(accountOnlineDto);

        WebsocketContextUtil.putSessionToContext(id, session);

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {

        String token = session.getHandshakeHeaders().get("cookie").get(0).substring(4);

        Long id = jwtTokenProvider.getCurrentUserIdFromJwt(token);
        log.info("Connection closed with userId: {}", id);

        AccountOnlineDto accountOnlineDto = new AccountOnlineDto(id, ZonedDateTime.now(), false);
        changeAccountOnline(accountOnlineDto);

        WebsocketContextUtil.removeSessionFromContext(id);

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {

        String payload = message.getPayload();
        log.info("Received message: {}", payload);

        JsonNode jsonNode = objectMapper.readTree(payload);
        if (jsonNode.get("type").textValue().equals("MESSAGE")) {

            StreamingMessageDto streamingMessageDto = objectMapper.readValue(payload, StreamingMessageDto.class);

            MessageDto messageDtoFromData = streamingMapper.convertToMessageDto((objectMapper.readValue(jsonNode.get("data").toString(), StreamingDataDto.class)));

            MessageDto messageDto = dialogService.createMessage(messageDtoFromData);
            streamingMessageDto.setData(objectMapper.convertValue(messageDto, HashMap.class));
            receiveMessage(streamingMessageDto);

        }

    }
}
