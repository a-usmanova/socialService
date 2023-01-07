package ru.skillbox.diplom.group32.social.service.utils.websocket;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@UtilityClass
public class WebsocketContextUtil {

    private static ConcurrentHashMap<Long, WebSocketSession> websocketContext = new ConcurrentHashMap<>();

    public static void putSessionToContext(Long id, WebSocketSession webSocketSession) {
        websocketContext.put(id, webSocketSession);
        log.info("Session put to context with id: {}", id);
    }

    public static WebSocketSession getSessionFromContext(Long id) {
        return websocketContext.get(id);
    }

    public static boolean contextContains(Long id) {
        return websocketContext.containsKey(id);
    }


    public static void removeSessionFromContext(Long id){
        websocketContext.remove(id);
        log.info("Session with id: {} - removed from context", id);
    }
}
