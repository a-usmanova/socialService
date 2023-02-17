package ru.skillbox.diplom.group32.social.service.resource.notification;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group32.social.service.model.notification.*;
import ru.skillbox.diplom.group32.social.service.resource.utils.web.WebConstant;

@RestController
@RequestMapping(WebConstant.VERSION_URL + "/notifications")
public interface NotificationController {

    @GetMapping("/settings")
    ResponseEntity<NotificationSettingsDto> getSettings();

    @PutMapping("/settings")
    @ResponseStatus(HttpStatus.OK)
    void updateSettings(@RequestBody NotificationSettingDto setting);

    @GetMapping
    ResponseEntity<NotificationsDto> getNotifcations();

    @GetMapping("/count")
    ResponseEntity<NotificationCountDto> getNotificationsCount();

    @PostMapping("/add")
    public void addNotification(@RequestBody EventNotification notification);
}
