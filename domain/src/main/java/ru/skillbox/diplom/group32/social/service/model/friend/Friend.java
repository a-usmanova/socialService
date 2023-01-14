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

//    private String photo;
//
//    private String firstName;
//
//    private String lastName;
//
//    private String city;
//
//    private String country;
//
//    private ZonedDateTime birthDate;
//
//    private Boolean isOnline;


    @Column(name = "from_account_id")
    private Long fromAccountId;

    @Column(name = "status_code")
    @Enumerated(EnumType.STRING)
    private StatusCode statusCode;

    @Column(name = "to_account_id")
    private Long toAccountId;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

}
