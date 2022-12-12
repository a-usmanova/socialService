package ru.skillbox.diplom.group32.social.service.controller.post;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.group32.social.service.model.post.PostDto;
import ru.skillbox.diplom.group32.social.service.model.post.PostSearchDto;
import ru.skillbox.diplom.group32.social.service.model.post.comment.CommentDto;
import ru.skillbox.diplom.group32.social.service.resource.post.PostController;
import ru.skillbox.diplom.group32.social.service.service.comment.CommentService;
import ru.skillbox.diplom.group32.social.service.service.post.PostService;

import java.io.IOException;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Slf4j
@RestController
@AllArgsConstructor
public class PostControllerImpl implements PostController {

    final PostService postService;

    final CommentService commentService;

    @Override
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<PostDto> create(PostDto dto) {
        return ResponseEntity.ok(postService.createPost(dto));
    }

    @Override
    public ResponseEntity<PostDto> getById(Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @Override
    public ResponseEntity<Page<PostDto>> getAll(PostSearchDto searchDto, Pageable page) {
        return ResponseEntity.ok(postService.getAllPosts(searchDto, page));
    }

    @Override
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<PostDto> update(PostDto dto) {
        return ResponseEntity.ok(postService.updatePost(dto));
    }

    @Override
    public ResponseEntity deleteById(Long id) {
        postService.deletePostById(id);
        return ResponseEntity.ok().body("POST DELETED");
    }

    @PostMapping(value = "/storagePostPhoto", consumes = {MULTIPART_FORM_DATA_VALUE})
    public String storagePostPhoto(@RequestParam(value = "request", required = false) MultipartFile request) throws IOException {

        return postService.savePhoto(request);

    }

    @Override
    public ResponseEntity createComment(Long id, CommentDto commentDto) {
        return ResponseEntity.ok(commentService.createComment(commentDto, id));
    }

    @Override
    public ResponseEntity getComment(Long id, Pageable page) {
        return ResponseEntity.ok(commentService.getAllComments(id, page));
    }

    @Override
    public ResponseEntity getSubcomment(Long id, Long commentId, Pageable page) {
        return ResponseEntity.ok(commentService.getSubcomments(id, commentId, page));
    }


    @Override
    public ResponseEntity updateComment(Long id, CommentDto commentDto, Long commentId) {
        return ResponseEntity.ok(commentService.updateComment(commentDto, id, commentId));
    }

    @Override
    public ResponseEntity deleteComment(Long id, Long commentId) {

        commentService.deleteComment(id, commentId);
        return ResponseEntity.ok().build();
    }
}
