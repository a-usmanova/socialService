//package ru.skillbox.diplom.group32.social.service.service.kafka;
//
//import lombok.Data;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//import ru.skillbox.diplom.group32.social.service.model.dialog.message.MessageDto;
//import ru.skillbox.diplom.group32.social.service.service.dialog.DialogService;
//
//@Slf4j
//@Data
//@Service
//@RequiredArgsConstructor
//public class KafkaService {
//
//    private final DialogService dialogService;
//    private KafkaTemplate<String, MessageDto> kafkaTemplate;
//
//    public void produce(MessageDto message) {
//        log.info("KafkaService get message " + message);
//        System.out.println("Producing the message: " + message);
//        kafkaTemplate.send("messages", message);
//
//    }
//
//    @KafkaListener(topics = "messages", groupId = "app.1")
//    public void consume(MessageDto message){
//
//        System.out.println("Consuming the message: " + message.getMessageText());
//
//    }
//}
