package ru.skillbox.diplom.group32.social.service.model.friend;


import lombok.Data;
import ru.skillbox.diplom.group32.social.service.model.base.BaseDto;
@Data
public class RecommendationDto extends BaseDto {

    private Boolean isDeleted;

    private String photo;
    private String firstName;
    private String lastName;

    public RecommendationDto(String photo, String firstName, String lastName) {
        this.photo = photo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isDeleted = false;
    }
}
