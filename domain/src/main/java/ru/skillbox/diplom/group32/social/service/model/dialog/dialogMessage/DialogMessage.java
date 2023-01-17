package ru.skillbox.diplom.group32.social.service.model.dialog.dialogMessage;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import ru.skillbox.diplom.group32.social.service.model.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "dialog_message")
public class DialogMessage extends BaseEntity {

    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Long time;
    @Column(name = "author_id", nullable = false)
    private Long authorId;
    @Column(name = "recipient_id", nullable = false)
    private Long recipientId;
    @Column(name = "message_text", nullable = false)
    private String messageText;

}
