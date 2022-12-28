package ru.skillbox.diplom.group32.social.service.model.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skillbox.diplom.group32.social.service.model.base.BaseSearchDto;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class PostSearchDto extends BaseSearchDto {

    private List<Long> ids;
    private List<Long> accountIds;
    private List<Long> blockedIds;
    private String author;
    private String title;
    private String postText;
    private Boolean withFriends;
    private Set<String> tags = new HashSet<>();
    private ZonedDateTime dateFrom;
    private ZonedDateTime dateTo;
}
