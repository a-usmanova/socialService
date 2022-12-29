package ru.skillbox.diplom.group32.social.service.service.comment;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.exception.ObjectNotFoundException;
import ru.skillbox.diplom.group32.social.service.mapper.comment.CommentMapper;
import ru.skillbox.diplom.group32.social.service.model.like.LikeType;
import ru.skillbox.diplom.group32.social.service.model.post.comment.*;
import ru.skillbox.diplom.group32.social.service.repository.post.comment.CommentRepository;
import ru.skillbox.diplom.group32.social.service.service.like.LikeService;

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


    public CommentDto createComment(CommentDto commentDto, Long id) {

        log.info("CommentService in createComment: for the post with id - {} Comment to save - {}", id, commentDto);
        commentDto.setPostId(id);
        if (commentDto.getParentId() == null) {
            commentDto.setCommentType(CommentType.POST);
        } else {
            commentDto.setCommentType(CommentType.COMMENT);
            Comment parentComment = commentRepository.findById(commentDto.getParentId()).orElseThrow(ObjectNotFoundException::new);
            parentComment.setCommentsCount(parentComment.getCommentsCount() + 1L);
            commentRepository.save(parentComment);
        }

        Comment comment = commentRepository.save(commentMapper.convertToEntity(commentDto));
        log.info("CommentService in createComment:" + commentDto);

        return commentMapper.convertToDto(comment);
    }

    public Page<CommentDto> getAllComments(Long id, Pageable page) {

        log.info("CommentService in getAllComments: tried to find all comments for the post with id: {} and pageable: {}", id, page);

        CommentSearchDto commentSearchDto = new CommentSearchDto();
        commentSearchDto.setPostId(id);
        commentSearchDto.setCommentType(CommentType.POST);
        commentSearchDto.setIsDeleted(false);
        Page<Comment> commentPage = commentRepository.findAll(getSpecification(commentSearchDto), page);
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

        }

        commentRepository.delete(commentRepository.findById(commentId).orElseThrow(ObjectNotFoundException::new));

    }

    public Page<CommentDto> getSubcomments(Long id, Long commentId, Pageable page) {

        log.info("CommentService in getSubcomments: tried to find all subcomments for the post with id: {} " +
                "and comment with id: {} and pageable: {}", id, commentId, page);
        CommentSearchDto commentSearchDto = new CommentSearchDto();
        commentSearchDto.setPostId(id);
        commentSearchDto.setParentId(commentId);
        commentSearchDto.setCommentType(CommentType.COMMENT);
        commentSearchDto.setIsDeleted(false);
        Page<Comment> commentPage = commentRepository.findAll(getSpecification(commentSearchDto), page);
        log.info("CommentService in getSubcomments: find comments: " + commentPage);
        return commentPage.map(commentMapper::convertToDto);

    }

    private Specification<Comment> getSpecification(CommentSearchDto searchDto) {
        return getBaseSpecification(searchDto)
                .and(equal(Comment_.postId, searchDto.getPostId(), true)
                        .and(equal(Comment_.commentType, searchDto.getCommentType(), true))
                        .and(equal(Comment_.parentId, searchDto.getParentId(), true)));
    }
}
