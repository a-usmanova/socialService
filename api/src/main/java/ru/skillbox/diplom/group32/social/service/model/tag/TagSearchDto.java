package ru.skillbox.diplom.group32.social.service.model.tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group32.social.service.model.base.BaseSearchDto;
import ru.skillbox.diplom.group32.social.service.model.post.PostDto;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagSearchDto extends BaseSearchDto {
    private String name;
    private Set<PostDto> posts = new HashSet<>();
}
