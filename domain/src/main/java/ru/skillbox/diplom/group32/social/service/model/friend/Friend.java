package ru.skillbox.diplom.group32.social.service.model.friend;

import lombok.*;
import ru.skillbox.diplom.group32.social.service.model.account.StatusCode;
import ru.skillbox.diplom.group32.social.service.model.base.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "friend")
public class Friend extends BaseEntity {

    @Column(name = "from_account_id")
    private Long fromAccountId;

    @Column(name = "status_code")
    @Enumerated(EnumType.STRING)
    private StatusCode statusCode;

    //**TODO remove previous_status_code

    @Column(name = "previous_status_code")
    @Enumerated(EnumType.STRING)
    private StatusCode previousStatusCode;

    @Column(name = "to_account_id")
    private Long toAccountId;

    @Column(name = "rating")
    private Long rating;

}
