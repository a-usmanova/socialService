package ru.skillbox.diplom.group32.social.service.model.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.apache.bcel.generic.Tag;
import ru.skillbox.diplom.group32.social.service.model.base.BaseSearchDto;
import ru.skillbox.diplom.group32.social.service.model.tag.TagDto;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import java.util.List;
import java.util.Set;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostSearchDto extends BaseSearchDto {

    private List<Long> ids;
    private List<Long> accountIds;
    private List<Long> blockedIds;
    private String author;
    private String title;
    private String postText;
    private Boolean withFriends;
    private Boolean isDeleted;
    private Set<String> tags = new HashSet<>();
    private ZonedDateTime dateFrom;
    private ZonedDateTime dateTo;
}
