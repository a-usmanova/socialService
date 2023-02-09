package ru.skillbox.diplom.group32.social.service.repository.notification;

import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group32.social.service.model.notification.UserNotification;
import ru.skillbox.diplom.group32.social.service.repository.base.BaseRepository;

import java.util.List;

@Repository
public interface NotificationRepository extends BaseRepository<UserNotification> {

    List<UserNotification> findAllByReceiverIdAndIsDeletedFalse(Long id);

    Long countFindAllByReceiverIdAndIsDeletedFalse(Long id);
}
