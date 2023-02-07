package ru.skillbox.diplom.group32.social.service.model.dialog;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.skillbox.diplom.group32.social.service.model.base.BaseEntity;
import ru.skillbox.diplom.group32.social.service.model.dialog.message.Message;

import javax.persistence.*;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "dialog")
public class Dialog extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_message_id", referencedColumnName = "id")
    private Message lastMessage;

}
