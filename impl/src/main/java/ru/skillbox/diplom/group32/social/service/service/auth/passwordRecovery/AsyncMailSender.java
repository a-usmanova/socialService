package ru.skillbox.diplom.group32.social.service.service.auth.passwordRecovery;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncMailSender {
    private final JavaMailSender javaMailSender;

    @Async
    public void sendMail(SimpleMailMessage mailMessage) {
        javaMailSender.send(mailMessage);
    }
}
