package ru.skillbox.diplom.group32.social.service.resource.post;

import org.springframework.web.bind.annotation.RequestMapping;
import ru.skillbox.diplom.group32.social.service.model.post.PostDto;
import ru.skillbox.diplom.group32.social.service.model.post.PostSearchDto;
import ru.skillbox.diplom.group32.social.service.resource.base.BaseController;
@RequestMapping("api/v1/post")
public interface PostController extends BaseController<PostDto, PostSearchDto> {
}
