package ru.skillbox.diplom.group32.social.service.service.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.mapper.account.AccountMapper;
import ru.skillbox.diplom.group32.social.service.mapper.notification.NotificationSettingsMapper;
import ru.skillbox.diplom.group32.social.service.mapper.notification.UserNotificationMapper;
import ru.skillbox.diplom.group32.social.service.model.auth.User;
import ru.skillbox.diplom.group32.social.service.model.notification.*;
import ru.skillbox.diplom.group32.social.service.model.streaming.StreamingMessageDto;
import ru.skillbox.diplom.group32.social.service.repository.account.AccountRepository;
import ru.skillbox.diplom.group32.social.service.repository.notification.NotificationRepository;
import ru.skillbox.diplom.group32.social.service.repository.notification.NotificationSettingsRepository;
import ru.skillbox.diplom.group32.social.service.utils.security.SecurityUtil;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NotificationService {

    private final NotificationSettingsRepository notificationSettingsRepository;
    private final NotificationSettingsMapper notificationSettingsMapper;

    private final NotificationRepository notificationRepository;
    private final UserNotificationMapper userNotificationMapper;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    private final Map<NotificationType, NotificationHandler> notificationHandlers;
    private final KafkaTemplate<String, EventNotification> eventNotificationKafkaTemplate;
    private final KafkaTemplate<String, StreamingMessageDto<NotificationDto>> sendNotificationKafkaTemplate;


    public NotificationService(NotificationSettingsRepository notificationSettingsRepository,
                               NotificationSettingsMapper notificationSettingsMapper,
                               NotificationRepository notificationRepository,
                               UserNotificationMapper userNotificationMapper,
                               AccountRepository accountRepository,
                               AccountMapper accountMapper,
                               List<NotificationHandler> notificationHandlerList,
                               KafkaTemplate<String, EventNotification> kafkaTemplate,
                               KafkaTemplate<String, StreamingMessageDto<NotificationDto>> sendNotificationKafkaTemplate
    ) {
        this.notificationSettingsRepository = notificationSettingsRepository;
        this.notificationSettingsMapper = notificationSettingsMapper;
        this.notificationRepository = notificationRepository;
        this.userNotificationMapper = userNotificationMapper;
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.notificationHandlers = notificationHandlerList.stream()
                .collect(Collectors.toMap(x -> x.myType(), x -> x));
        this.eventNotificationKafkaTemplate = kafkaTemplate;
        this.sendNotificationKafkaTemplate = sendNotificationKafkaTemplate;
    }

    public NotificationSettingsDto getSettings() {
        Long userId = SecurityUtil.getJwtUserIdFromSecurityContext();
        NotificationSettings settings = notificationSettingsRepository.findByUserId(userId).orElseThrow();
        return notificationSettingsMapper.entityToDto(settings);
    }

    public void createSettings(User user) {
        NotificationSettings settings = new NotificationSettings();
        settings.setUserId(user.getId());
        settings.setIsDeleted(false);
        notificationSettingsRepository.save(settings);
    }

    public void updateSettings(NotificationSettingDto updatedSetting) {
        Long userId = SecurityUtil.getJwtUserIdFromSecurityContext();
        NotificationSettings settings = notificationSettingsRepository.findByUserId(userId).orElseThrow();
        settings = notificationSettingsMapper.changeSettings(settings, updatedSetting);
        notificationSettingsRepository.save(settings);
        log.info("NotificationsService.updateSettings: Notifications settings updated for user with id {}", userId);
    }

    @KafkaListener(topics = "event-notification", containerFactory = "notificationListener")
    private void pullNotification(EventNotification notification) {
        UserNotification userNotification = userNotificationMapper.toUserNotification(notification);
        userNotification.setTime(ZonedDateTime.now());
        log.info("NotificationsService.pullNotification: handle eventNotification from user with id {}", notification.getAuthorId());
        List<UserNotification> notificationList = notificationHandlers.get(userNotification.getNotificationType()).fillReceiverIds(userNotification);
        sendNotifications(notificationRepository.saveAll(notificationList));
        log.info("NotificationsService.pullNotification: new notification(s) saved and sent to Kafka sent-notifications topic");
    }

    private void sendNotifications(List<UserNotification> notificationList) {
        notificationList.stream().map(n -> {
            StreamingMessageDto<NotificationDto> streamingMessageDto = new StreamingMessageDto<>();
            streamingMessageDto.setType("NOTIFICATION");
            streamingMessageDto.setAccountId(n.getReceiverId());
            NotificationDto dto = userNotificationMapper.toNotificationDto(n);
            dto.setAuthor(accountMapper.convertToDto(accountRepository.findById(n.getAuthorId()).orElseThrow()));
            streamingMessageDto.setData(dto);
            return streamingMessageDto;
        }).forEach(smd -> sendNotificationKafkaTemplate.send("send-notification", smd));
    }

    public NotificationsDto getNotifications() {
        Long userId = SecurityUtil.getJwtUserIdFromSecurityContext();
        List<UserNotification> notificationsList = notificationRepository.findAllByReceiverIdAndIsDeletedFalse(userId);
        notificationsList.forEach(n -> n.setIsDeleted(true));
        notificationRepository.saveAll(notificationsList);
        List<NotificationDto> dtoList = new ArrayList<>();
        for (UserNotification notification : notificationsList) {
            NotificationDto dto = userNotificationMapper.toNotificationDto(notification);
            dto.setAuthor(accountMapper.convertToDto(accountRepository.findById(notification.getAuthorId()).orElseThrow()));
            dtoList.add(dto);
        }
        return new NotificationsDto(ZonedDateTime.now(), dtoList);
    }

    public NotificationCountDto getNotificationsCount() {
        Long userId = SecurityUtil.getJwtUserIdFromSecurityContext();
        Long notificationsCount = notificationRepository.countFindAllByReceiverIdAndIsDeletedFalse(userId);
        return new NotificationCountDto(ZonedDateTime.now().toEpochSecond(), new Count(notificationsCount));
    }

    public void addNotification(EventNotification notification) {
        eventNotificationKafkaTemplate.send("event-notification", notification);
        //pullNotification(notification);
    }


}
