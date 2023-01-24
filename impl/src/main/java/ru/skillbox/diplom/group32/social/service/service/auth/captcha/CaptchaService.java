package ru.skillbox.diplom.group32.social.service.service.auth.captcha;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.model.auth.CaptchaDto;
import ru.skillbox.diplom.group32.social.service.model.auth.RegistrationDto;
import ru.skillbox.diplom.group32.social.service.model.auth.captcha.Captcha;
import ru.skillbox.diplom.group32.social.service.repository.auth.captcha.CaptchaRepository;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaService {

    private final CaptchaRepository captchaRepository;
    private final String IMAGE_FORMAT = "png";

    public CaptchaDto getCaptcha() {
        Captcha captcha = createCapthca();

        captcha.setTime(ZonedDateTime.now());
        captchaRepository.save(captcha);

        CaptchaDto captchaDto = new CaptchaDto();
        captchaDto.setSecret(captcha.getSecret().toString());

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(captcha.getImage(), IMAGE_FORMAT, os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String prefix = "data:image/" + IMAGE_FORMAT + ";base64, ";
        captchaDto.setImage(prefix + Base64.getEncoder().encodeToString(os.toByteArray()));

        return captchaDto;
    }

    public boolean passCaptcha(RegistrationDto registrationDto) {

        Captcha captchaOrigin = captchaRepository.getById(UUID.fromString(registrationDto.getCaptchaSecret()));

        ZonedDateTime createdTime = captchaOrigin.getTime();
        if (createdTime.plus(2, ChronoUnit.MINUTES).isBefore(ZonedDateTime.now()))
            return false;

        return captchaOrigin.getCode().equals(registrationDto.getCaptchaCode());
    }

    @Scheduled(cron = "@daily")
    public void truncateCaptchaTable() {
        captchaRepository.deleteAllInBatch();
    }

    private Captcha createCapthca() {
        Captcha captcha = CaptchaBuilder.getBuilder()
                .createImage(150, 75)
                .setText(30, 10, 40)
                .setLines(300).build();
        return captcha;
    }
}
