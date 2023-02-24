package ru.skillbox.diplom.group32.social.service.model.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ErrorDto {

    @JsonProperty("error_description")
    public String errorDescription;
    public ZonedDateTime timestamp;
    public Integer status;
    public String error;
}
