package ru.skillbox.diplom.group32.social.service.model.dialog.message;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import ru.skillbox.diplom.group32.social.service.model.base.BaseEntity;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "message")
public class Message extends BaseEntity {

    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime time;
    @Column(name = "author_id", nullable = false)
    private Long authorId;
    @Column(name = "recipient_id", nullable = false)
    private Long recipientId;
    @Column(name = "message_text", nullable = false)
    private String messageText;
    @Enumerated(EnumType.STRING)
    @Column(name = "read_status", nullable = false)
    private ReadStatus readStatus;
    @Column(name = "dialog_id", nullable = false)
    private Long dialogId;
}
