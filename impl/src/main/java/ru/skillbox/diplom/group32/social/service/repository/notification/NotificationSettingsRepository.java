package ru.skillbox.diplom.group32.social.service.repository.notification;

import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group32.social.service.model.account.Account;
import ru.skillbox.diplom.group32.social.service.model.auth.User;
import ru.skillbox.diplom.group32.social.service.model.notification.NotificationSettings;
import ru.skillbox.diplom.group32.social.service.repository.base.BaseRepository;

import java.util.Optional;

@Repository
public interface NotificationSettingsRepository extends BaseRepository<NotificationSettings> {
    Optional<NotificationSettings> findByUserId(Long userId);
}
