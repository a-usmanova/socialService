package ru.skillbox.diplom.group32.social.service.repository.dialog.message;

import ru.skillbox.diplom.group32.social.service.model.dialog.message.Message;
import ru.skillbox.diplom.group32.social.service.model.dialog.message.ReadStatus;
import ru.skillbox.diplom.group32.social.service.repository.base.BaseRepository;

import java.util.List;

public interface MessageRepository extends BaseRepository<Message> {

    Long countByRecipientIdAndReadStatus(Long authorId, ReadStatus readStatus);

}
