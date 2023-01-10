package ru.skillbox.diplom.group32.social.service.model.auth.passwordRecovery;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "password_recovery")
public class PasswordRecovery {

    @Id
    @Column(name = "secret_link_id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID secretLinkId;

    private String email;
}
