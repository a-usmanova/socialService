package ru.skillbox.diplom.group32.social.service.resource.tag;

import org.springframework.web.bind.annotation.RequestMapping;
import ru.skillbox.diplom.group32.social.service.model.tag.TagDto;
import ru.skillbox.diplom.group32.social.service.model.tag.TagSearchDto;
import ru.skillbox.diplom.group32.social.service.resource.base.BaseController;
@RequestMapping("api/v1/tag")
public interface TagController extends BaseController<TagDto, TagSearchDto> {
}
