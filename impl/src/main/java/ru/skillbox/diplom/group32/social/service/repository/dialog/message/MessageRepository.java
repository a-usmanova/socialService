package ru.skillbox.diplom.group32.social.service.repository.dialog.message;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.skillbox.diplom.group32.social.service.model.dialog.message.Message;
import ru.skillbox.diplom.group32.social.service.model.dialog.message.ReadStatus;
import ru.skillbox.diplom.group32.social.service.repository.base.BaseRepository;

public interface MessageRepository extends BaseRepository<Message>, PagingAndSortingRepository <Message, Long> {

    Long countByRecipientIdAndReadStatus(Long authorId, ReadStatus readStatus);

}
