package ru.skillbox.diplom.group32.social.service.model.streaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreamingMessageDto<T>{

    private String type;
    private Long accountId;
    private T data;

}
