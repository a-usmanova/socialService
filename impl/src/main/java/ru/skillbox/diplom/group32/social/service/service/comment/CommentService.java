package ru.skillbox.diplom.group32.social.service.service.comment;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.exception.ObjectNotFoundException;
import ru.skillbox.diplom.group32.social.service.mapper.comment.CommentMapper;
import ru.skillbox.diplom.group32.social.service.mapper.post.PostMapper;
import ru.skillbox.diplom.group32.social.service.model.like.LikeType;
import ru.skillbox.diplom.group32.social.service.model.notification.EventNotification;
import ru.skillbox.diplom.group32.social.service.model.notification.NotificationType;
import ru.skillbox.diplom.group32.social.service.model.post.PostDto;
import ru.skillbox.diplom.group32.social.service.model.post.comment.*;
import ru.skillbox.diplom.group32.social.service.repository.post.comment.CommentRepository;
import ru.skillbox.diplom.group32.social.service.service.like.LikeService;
import ru.skillbox.diplom.group32.social.service.service.post.PostService;
import ru.skillbox.diplom.group32.social.service.service.tag.TagService;

import java.time.ZonedDateTime;

import static ru.skillbox.diplom.group32.social.service.utils.specification.SpecificationUtil.equal;
import static ru.skillbox.diplom.group32.social.service.utils.specification.SpecificationUtil.getBaseSpecification;

@Slf4j
@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final LikeService likeService;
    private final PostService postService;
    private final PostMapper postMapper;
    private final TagService tagService;
    private final KafkaTemplate<String, EventNotification> eventNotificationKafkaTemplate;


    public CommentDto createComment(CommentDto commentDto, Long id) {

        log.info("CommentService in createComment: for the post with id - {} Comment to save - {}", id, commentDto);
        commentDto.setPostId(id);
        if (commentDto.getParentId() == null) {
            PostDto postDto = postService.getById(id);
            postDto.setCommentsCount(postDto.getCommentsCount() + 1L);
            postDto.setTags(tagService.getNames(postMapper.convertToEntity(postDto).getTags()));
            postService.update(postDto);
            commentDto.setCommentType(CommentType.POST);
        } else {
            commentDto.setCommentType(CommentType.COMMENT);
            Comment parentComment = commentRepository.findById(commentDto.getParentId()).orElseThrow(ObjectNotFoundException::new);
            parentComment.setCommentsCount(parentComment.getCommentsCount() + 1L);
            commentRepository.save(parentComment);
        }

        Comment comment = commentRepository.save(commentMapper.convertToEntity(commentDto));
        sendNotification(comment, "НОВЫЙ КОММЕНТ");
        log.info("CommentService in createComment:" + commentDto);

        return commentMapper.convertToDto(comment);
    }

    public Page<CommentDto> getAllComments(Long id, Pageable page) {

        log.info("CommentService in getAllComments: tried to find all comments for the post with id: {} and pageable: {}", id, page);

        Page<Comment> commentPage = commentRepository.findAll(getSpecification(new CommentSearchDto(id, CommentType.POST)), page);
        log.info("CommentService in getAllComments: find comments: " + commentPage);
        return commentPage.map(e->{
            CommentDto commentDto = commentMapper.convertToDto(e);
            commentDto.setMyLike(likeService.getMyLike(commentDto.getId(), LikeType.COMMENT));
            return commentDto;
        });
    }

    public CommentDto updateComment(CommentDto commentDto, Long id, Long commentId) {

        log.info("PostService in updateComment: post with id: {} comment with id: {} commentToUpdate: {}", id, commentId, commentDto);
        Comment temp = commentRepository.findById(commentId).orElseThrow(ObjectNotFoundException::new);
        temp.setPostId(id);
        temp.setCommentText(commentDto.getCommentText());
        temp.setTimeChanged(ZonedDateTime.now());
        log.info("PostService in updateComment: Comment updated. New Comment: " + temp);
        return commentMapper.convertToDto(commentRepository.save(temp));

    }

    public void deleteComment(Long id, Long commentId) {

        log.info("CommentService in deleteComment: try to delete at the post with id: {} comment with id: {}", id, commentId);

        Long parentId = commentRepository.findById(commentId).orElseThrow(ObjectNotFoundException::new).getParentId();

        if (parentId != null) {

            Comment parentComment = commentRepository.findById(parentId).orElseThrow(ObjectNotFoundException::new);
            parentComment.setCommentsCount(parentComment.getCommentsCount() - 1L);
            commentRepository.save(parentComment);

        } else {
            PostDto postDto = postService.getById(id);
            postDto.setCommentsCount(postDto.getCommentsCount() - 1L);
            postDto.setTags(tagService.getNames(postMapper.convertToEntity(postDto).getTags()));
            postService.update(postDto);
        }

        commentRepository.delete(commentRepository.findById(commentId).orElseThrow(ObjectNotFoundException::new));

    }

    public Page<CommentDto> getSubcomments(Long id, Long commentId, Pageable page) {

        log.info("CommentService in getSubcomments: tried to find all subcomments for the post with id: {} " +
                "and comment with id: {} and pageable: {}", id, commentId, page);
        Page<Comment> commentPage = commentRepository.findAll(getSpecification(new CommentSearchDto(id, commentId, CommentType.COMMENT)), page);
        log.info("CommentService in getSubcomments: find comments: " + commentPage);
        return commentPage.map(commentMapper::convertToDto);

    }

    private void sendNotification(Comment comment, String text) {

        text = comment.getCommentText();
        EventNotification eventNotification = new EventNotification(comment.getAuthorId(), postService.getById(comment.getPostId()).getAuthorId(), NotificationType.POST_COMMENT, text.length() < 20 ? text : text.substring(0, 20) + "...");
        eventNotificationKafkaTemplate.send("event-notification", eventNotification);

    }

    private Specification<Comment> getSpecification(CommentSearchDto searchDto) {
        return getBaseSpecification(searchDto)
                .and(equal(Comment_.postId, searchDto.getPostId(), true)
                        .and(equal(Comment_.commentType, searchDto.getCommentType(), true))
                        .and(equal(Comment_.parentId, searchDto.getParentId(), true)));
    }
}
