package ru.skillbox.diplom.group32.social.service.model.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group32.social.service.model.base.BaseSearchDto;

import java.time.ZonedDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostSearchDto extends BaseSearchDto {

    private Long[] ids;
    private Long[] accountIds;
    private Long[] blockedIds;
    private String author;
    private String title;
    private String postText;
    private Boolean withFriends;
    private Boolean isDeleted;
    private String[] tags;
    private ZonedDateTime dateFrom;
    private ZonedDateTime dateTo;

}
