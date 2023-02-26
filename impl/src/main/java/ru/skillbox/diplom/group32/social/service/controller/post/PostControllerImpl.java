package ru.skillbox.diplom.group32.social.service.controller.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group32.social.service.model.like.LikeDto;
import ru.skillbox.diplom.group32.social.service.model.like.LikeType;
import ru.skillbox.diplom.group32.social.service.model.post.PostDto;
import ru.skillbox.diplom.group32.social.service.model.post.PostSearchDto;
import ru.skillbox.diplom.group32.social.service.model.post.comment.CommentDto;
import ru.skillbox.diplom.group32.social.service.resource.post.PostController;
import ru.skillbox.diplom.group32.social.service.service.comment.CommentService;
import ru.skillbox.diplom.group32.social.service.service.like.LikeService;
import ru.skillbox.diplom.group32.social.service.service.post.PostService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostControllerImpl implements PostController {

    final PostService postService;
    final CommentService commentService;
    final LikeService likeService;

    @Override
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<PostDto> create(PostDto dto) {
        return ResponseEntity.ok(postService.create(dto));
    }

    @Override
    public ResponseEntity<PostDto> update(PostDto dto) {
        return ResponseEntity.ok(postService.update(dto));
    }

    @Override
    public ResponseEntity<PostDto> getById(Long id) {
        return ResponseEntity.ok(postService.getById(id));
    }

    @Override
    public ResponseEntity<Page<PostDto>> getAll(PostSearchDto searchDto, Pageable page) {
        return ResponseEntity.ok(postService.getAll(searchDto, page));
    }

    @Override
    public ResponseEntity deleteById(Long id) {
        postService.deleteById(id);
        return ResponseEntity.ok().body("POST DELETED");
    }

    @Override
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<CommentDto> createComment(Long id, CommentDto commentDto) {
        return ResponseEntity.ok(commentService.createComment(commentDto, id));
    }

    @Override
    public ResponseEntity<Page<CommentDto>> getComment(Long id, Pageable page) {
        return ResponseEntity.ok(commentService.getAllComments(id, page));
    }

    @Override
    public ResponseEntity<Page<CommentDto>> getSubcomment(Long id, Long commentId, Pageable page) {
        return ResponseEntity.ok(commentService.getSubcomments(id, commentId, page));
    }


    @Override
    public ResponseEntity<CommentDto> updateComment(Long id, CommentDto commentDto, Long commentId) {
        return ResponseEntity.ok(commentService.updateComment(commentDto, id, commentId));
    }

    @Override
    public ResponseEntity deleteComment(Long id, Long commentId) {

        commentService.deleteComment(id, commentId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<LikeDto> createPostLike(Long id) {
        return ResponseEntity.ok(likeService.createLike(id, LikeType.POST));
    }

    @Override
    public ResponseEntity deletePostLike(Long id) {
        likeService.deleteLike(id, LikeType.POST);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<LikeDto> createCommentLike(Long id, Long commentId) {
        return ResponseEntity.ok(likeService.createLike(commentId, LikeType.COMMENT));
    }

    @Override
    public ResponseEntity deleteCommentLike(Long id, Long commentId) {
        likeService.deleteLike(commentId, LikeType.COMMENT);
        return ResponseEntity.ok().build();
    }
}
