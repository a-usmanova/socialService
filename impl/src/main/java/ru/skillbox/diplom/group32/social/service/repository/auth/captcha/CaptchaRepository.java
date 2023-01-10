package ru.skillbox.diplom.group32.social.service.repository.auth.captcha;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group32.social.service.model.auth.captcha.Captcha;

import java.util.UUID;


@Repository
public interface CaptchaRepository extends JpaRepository<Captcha, UUID> {
}
