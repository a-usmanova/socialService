package ru.skillbox.diplom.group32.social.service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@Getter
@Setter
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "cloudinary.development")
public class Properties {

    private String cloudName;
    private String apiKey;
    private String apiSecret;


}
