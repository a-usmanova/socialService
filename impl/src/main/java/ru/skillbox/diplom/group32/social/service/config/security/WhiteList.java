package ru.skillbox.diplom.group32.social.service.config.security;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "white-list")
@Getter
@Setter
public class WhiteList {

    private String[] links;

}
