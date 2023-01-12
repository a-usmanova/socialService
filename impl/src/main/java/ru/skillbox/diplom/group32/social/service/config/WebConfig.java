package ru.skillbox.diplom.group32.social.service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.skillbox.diplom.group32.social.service.utils.zonedDateTime.ZonedDateTimeConverter;

import java.time.ZoneId;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new ZonedDateTimeConverter(ZoneId.systemDefault()));
    }
}
