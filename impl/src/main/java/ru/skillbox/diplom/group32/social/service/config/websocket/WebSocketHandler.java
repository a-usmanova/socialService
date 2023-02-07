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
import ru.skillbox.diplom.group32.social.service.model.account.AccountOnlineDto;
import ru.skillbox.diplom.group32.social.service.model.dialog.DialogMessage;
import ru.skillbox.diplom.group32.social.service.model.dialog.message.MessageDto;
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
    private final KafkaTemplate<String, DialogMessage> kafkaDialogTemplate;
    private final KafkaTemplate<String, AccountOnlineDto> kafkaNotificationTemplate;

    private final DialogService dialogService;

    @KafkaListener(topics = "sender-message", containerFactory = "dialogListener")
    public void sendToSocketMessage(DialogMessage dialogMessage) throws IOException {

        log.info("Received message - {}", dialogMessage);
        if (dialogMessage != null) {
            WebsocketContextUtil.getSessionFromContext(dialogMessage.getAccountId())
                    .sendMessage(new TextMessage(objectMapper.writeValueAsString(dialogMessage)));
            log.info("To user with id: {}, send message to webSocket: {}", dialogMessage.getAccountId(), dialogMessage);
        }

    }

    @KafkaListener(topics = "sender-notification", containerFactory = "notificationListener")
    public void sendToSocketNotification(AccountOnlineDto accountOnlineDto) throws IOException {

        log.info("Received account online notification - {}", accountOnlineDto);
        if (accountOnlineDto != null) {
            WebsocketContextUtil.getSessionFromContext(accountOnlineDto.getId())
                    .sendMessage(new TextMessage(objectMapper.writeValueAsString(accountOnlineDto)));
            log.info("To user with id: {}, send notification about online to webSocket: {}", accountOnlineDto.getId(), accountOnlineDto);
        }

    }

    public void receiveMessage(DialogMessage dialogMessage) {

        kafkaDialogTemplate.send("sender-message", dialogMessage);

    }

    public void changeAccountOnline(AccountOnlineDto accountOnlineDto) {

        kafkaNotificationTemplate.send("receive-notification", accountOnlineDto);

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

            DialogMessage dialogMessage = objectMapper.readValue(payload, DialogMessage.class);
            MessageDto messageDto = dialogService.createMessage(objectMapper.convertValue(dialogMessage.getData(), MessageDto.class));
            dialogMessage.setData(objectMapper.convertValue(messageDto, HashMap.class));
            receiveMessage(dialogMessage);

        }

    }
}
