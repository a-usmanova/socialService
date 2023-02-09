package ru.skillbox.diplom.group32.social.service.mapper.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group32.social.service.model.notification.NotificationSettingDto;
import ru.skillbox.diplom.group32.social.service.model.notification.NotificationSettings;
import ru.skillbox.diplom.group32.social.service.model.notification.NotificationSettingsDto;

import java.util.*;

@Component
public class NotificationSettingsMapper {
    private final ObjectMapper mapper = new ObjectMapper();

    private final Set<String> notDataFields = new HashSet<>(List.of("userId", "isDeleted", "id"));

    public NotificationSettingsDto entityToDto(NotificationSettings entity) {
        Map<String, Object> entityMap = mapper.convertValue(entity, HashMap.class);
        NotificationSettingsDto list = new NotificationSettingsDto();
        for (String key : entityMap.keySet()) {
            if (notDataFields.contains(key))
                continue;
            Map<String, Object> m = new HashMap<>();
            m.put("notification_type", convertLowerCamelToUpperUnderscore(key));
            m.put("enable", entityMap.get(key));
            NotificationSettingDto notificationSettingDto = mapper.convertValue(m, NotificationSettingDto.class);
            list.add(notificationSettingDto);
        }
        return list;
    }

    public NotificationSettings changeSettings(NotificationSettings existingSettings, NotificationSettingDto updatedSetting) {
        Map<String, Boolean> settingsMap = entityToMap(existingSettings);
        settingsMap.put(updatedSetting.getNotificationType().toString(), updatedSetting.getEnable());
        NotificationSettings newNotificationSettings = mapToEntity(settingsMap, existingSettings);
        return newNotificationSettings;
    }

    public Map<String, Boolean> entityToMap(NotificationSettings entity) {
        Map<String, Object> map = mapper.convertValue(entity, HashMap.class);
        notDataFields.forEach(map::remove);
        Map<String, Object> map2 = new HashMap<>();
        for (String key : map.keySet()) {
            String newKey = convertLowerCamelToUpperUnderscore(key);
            map2.put(newKey, map.get(key));
        }
        Map<String, Boolean> map3 = mapper.convertValue(map2, HashMap.class);

        return map3;
    }

    public NotificationSettings mapToEntity(Map<String, Boolean> map, NotificationSettings settings) {
        Map<String, Object> map2 = mapper.convertValue(map, HashMap.class);
        Map<String, Object> map3 = new HashMap<>();
        for (String key : map2.keySet()) {
            String newKey = convertUpperUnderscoreToLowerCamel(key);
            map3.put(newKey, map2.get(key));
        }
        Map<String, Object> wholeEntityMap = mapper.convertValue(settings, HashMap.class);

        wholeEntityMap.putAll(map3);
        settings = mapper.convertValue(wholeEntityMap, NotificationSettings.class);
        return settings;
    }

    private String convertLowerCamelToUpperUnderscore(String string) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, string);
    }

    private String convertUpperUnderscoreToLowerCamel(String string) {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, string);
    }
}
