package ru.skillbox.diplom.group32.social.service.model.account;

import lombok.*;
import ru.skillbox.diplom.group32.social.service.model.auth.User;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "account")
public class Account extends User {

    private String phone;
    private String photo;
    private String about;
    private String city;
    private String country;
    @Column(name = "status_code")
    @Enumerated(EnumType.STRING)
    private StatusCode statusCode;
    @Column(name = "reg_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime regDate;
    @Column(name = "birth_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime birthDate;
    @Column(name = "message_permission")
    private String messagePermission;
    @Column(name = "last_online_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime lastOnlineTime;
    @Column(name = "is_online")
    private Boolean isOnline;
    @Column(name = "is_blocked")
    private Boolean isBlocked;
    @Column(name = "photo_id")
    private String photoId;
    @Column(name = "photo_name")
    private String photoName;
    @Column(name = "created_on", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdOn;
    @Column(name = "updated_on", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime updatedOn;

}
