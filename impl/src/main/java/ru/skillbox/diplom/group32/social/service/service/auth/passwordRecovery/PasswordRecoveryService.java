package ru.skillbox.diplom.group32.social.service.service.auth.passwordRecovery;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.skillbox.diplom.group32.social.service.model.auth.passwordRecovery.NewPasswordDto;
import ru.skillbox.diplom.group32.social.service.model.auth.passwordRecovery.PasswordRecoveryDto;
import ru.skillbox.diplom.group32.social.service.model.auth.User;
import ru.skillbox.diplom.group32.social.service.model.auth.passwordRecovery.PasswordRecovery;
import ru.skillbox.diplom.group32.social.service.repository.auth.UserRepository;
import ru.skillbox.diplom.group32.social.service.repository.auth.passwordRecovery.PasswordRecoveryRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordRecoveryService {

    @Value("${host}")
    private String host;

    @Value("${spring.mail.username}")
    private String mailUsername;

    private final AsyncMailSender asyncMailSender;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final PasswordRecoveryRepository passwordRecoveryRepository;

    public void sendEmail(PasswordRecoveryDto passwordRecoveryDto) {
        log.info("PasswordRecoveryService.sendMail: Новый запрос на восстановление пароля для {}", passwordRecoveryDto.getEmail());

        userRepository.findUserByEmail(passwordRecoveryDto.getEmail())
                .orElseThrow(() -> { log.error("PasswordRecoveryService.sendMail: {} не зарегистрирован", passwordRecoveryDto.getEmail());
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Такой email не зарегистрирован");});

        PasswordRecovery passwordRecovery = new PasswordRecovery();
        passwordRecovery.setEmail(passwordRecoveryDto.getEmail());
        String uuid = passwordRecoveryRepository.save(passwordRecovery).getSecretLinkId().toString();

        String text = setText(uuid);

        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(mailUsername);
        email.setTo(passwordRecoveryDto.getEmail());
        email.setSubject("Восстановление пароля к social-service-team32");
        email.setText(text);
        asyncMailSender.sendMail(email);
        log.info("PasswordRecoveryService.sendMail: Письмо для восстановления пароля отправлено на {}", passwordRecoveryDto.getEmail());
    }

    public void setPassword(String uuid, NewPasswordDto newPasswordDto) {

        PasswordRecovery passwordRecovery = passwordRecoveryRepository.getById(UUID.fromString(uuid));

        String userEmail = passwordRecovery.getEmail();
        User user = userRepository.findUserByEmail(userEmail).orElseThrow();
        user.setPassword(passwordEncoder.encode(newPasswordDto.getPassword()));
        userRepository.save(user);
        passwordRecoveryRepository.deleteAllByIdInBatch(List.of(UUID.fromString(uuid)));
        log.info("PasswordRecoveryService.setPassword: Установлен новый пароль для пользователя {}", user.getEmail());
    }


    private String setText(String uuid) {
        return "Чтобы придумать новый пароль, перейдите по этой ссылке: "
                + "http://" + host + "/change-password/" + uuid;
    }

    @Scheduled(cron = "@daily")
    public void truncatePasswordRecovery() {
        passwordRecoveryRepository.deleteAllInBatch();
    }
}
