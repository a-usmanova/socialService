# social-service

Это бэк-энд составляющая проекта «Социальная сеть». Групповой проект разрабатывался по методологии Scrum в течение 3 месяцев.
 
### Функции соцсети:
* система авторизации и регистрации,
* личный профиль и его настройки,
* возможность поиска и добавления людей в друзья,
* просмотр списка друзей,
* переписка между пользователями,
* публикация постов,
* отметка лайков,
* написание комментариев к постам и другое.

### Как устроен проект:
Для удобства разработки и внедрения зависимостей проект имеет модульную структуру

* api (модуль для «общения» с внешними системами, здесь описаны DTO-объекты и интерфейсы контроллеров )
* db(содержит чейнчж сеты для liquibase)
* domain (содержит Entity)
* impl (содержит настройки приложения, реализации контроллеров, сервисов и т.д.) 

### Технологии:
* Java 17 
* Spring 
* Spring MVC 
* Hibernate 
* Postgres 
* Docker 
* Kafka

### Запуск проекта в dev-режиме:

- Для запуска kafka локально в docker выполните команды
    1. docker network create -d bridge kafka-net
    2. docker run -d --name kafka-server --network kafka-net -e ALLOW_PLAINTEXT_LISTENER=yes -e KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper-server:2181 -e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 -p 9092:9092 bitnami/kafka:latest
    3. docker run -d --name zookeeper-server -p 2181:2181 --network kafka-net -e ALLOW_ANONYMOUS_LOGIN=yes bitnami/zookeeper:latest

- Для запуска postgresSQL локально в docker выполните команду
    1. docker run --name postgres -d -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres postgres:alpine
