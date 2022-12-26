package ru.skillbox.diplom.group32.social.service.resource.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.group32.social.service.model.post.PostDto;
import ru.skillbox.diplom.group32.social.service.model.post.PostSearchDto;
import ru.skillbox.diplom.group32.social.service.model.post.comment.CommentDto;
import ru.skillbox.diplom.group32.social.service.resource.base.BaseController;
import ru.skillbox.diplom.group32.social.service.resource.utils.web.WebConstant;

import java.io.IOException;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RequestMapping(value = WebConstant.VERSION_URL  + "/post")
public interface PostController extends BaseController<PostDto, PostSearchDto> {


    @PostMapping(value = "{id}/comment")
    @ResponseStatus(code = HttpStatus.CREATED)
    ResponseEntity createComment(@PathVariable Long id, @RequestBody CommentDto commentDto);

    @GetMapping(value = "{id}/comment")
    ResponseEntity getComment(@PathVariable Long id, @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable page);

    @GetMapping(value = "{id}/comment/{commentId}/subcomment")
    ResponseEntity getSubcomment(@PathVariable Long id, @PathVariable Long commentId, @PageableDefault(sort = {"parentId"}, direction = Sort.Direction.DESC) Pageable page);

    @PutMapping(value = "{id}/comment/{commentId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    ResponseEntity updateComment(@PathVariable Long id, @RequestBody CommentDto commentDto, @PathVariable Long commentId);

    @DeleteMapping(value = "{id}/comment/{commentId}")
    ResponseEntity deleteComment(@PathVariable Long id, @PathVariable Long commentId);

    @PostMapping(value = "/storagePostPhoto", consumes = {MULTIPART_FORM_DATA_VALUE})
    String storagePostPhoto(@RequestParam(value = "request", required = false) MultipartFile request) throws IOException;


}
