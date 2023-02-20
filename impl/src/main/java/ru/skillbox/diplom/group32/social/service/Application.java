package ru.skillbox.diplom.group32.social.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.skillbox.diplom.group32.social.service.config.Properties;
import ru.skillbox.diplom.group32.social.service.repository.base.BaseRepositoryImpl;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "ru.skillbox.diplom.group32.social.service.repository",
        repositoryBaseClass = BaseRepositoryImpl.class)
@EnableConfigurationProperties(Properties.class)
@EnableFeignClients
public class Application {

    public static void main(String[] args) {
        {
            SpringApplication.run(Application.class, args);
        }

    }
}