package ru.skillbox.diplom.group32.social.service.service.comment;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.exception.ObjectNotFoundException;
import ru.skillbox.diplom.group32.social.service.mapper.comment.CommentMapper;
import ru.skillbox.diplom.group32.social.service.model.post.comment.Comment;
import ru.skillbox.diplom.group32.social.service.model.post.comment.CommentDto;
import ru.skillbox.diplom.group32.social.service.model.post.comment.CommentSearchDto;
import ru.skillbox.diplom.group32.social.service.model.post.comment.Comment_;
import ru.skillbox.diplom.group32.social.service.repository.post.comment.CommentRepository;

import static ru.skillbox.diplom.group32.social.service.utils.specification.SpecificationUtil.equal;
import static ru.skillbox.diplom.group32.social.service.utils.specification.SpecificationUtil.getBaseSpecification;

@Slf4j
@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;


    public CommentDto createComment(CommentDto commentDto, Long id) {
        log.info("for the post with id - {} Comment to save - {}", id, commentDto);

        commentDto.setPostId(id);

        Comment comment = commentRepository.save(commentMapper.convertToEntity(commentDto));
        log.info("Comment saved to db - " + commentDto);

        return commentMapper.convertToDto(comment);
    }

    public Page<CommentDto> getAllComments(Long id, Pageable page) {

        CommentSearchDto commentSearchDto = new CommentSearchDto();
        commentSearchDto.setPostId(id);
        Page<Comment> commentPage = commentRepository.findAll(getSpecification(commentSearchDto), page);
//--> без getSpecification Page<Comment> commentPage = commentRepository.findAllByPostId(id, page);
        return commentPage.map(commentMapper::convertToDto);

    }



    public CommentDto updateComment(CommentDto commentDto, Long id, Long commentId) {

        commentDto.setPostId(id);
        commentDto.setId(commentId);

        Comment comment = commentRepository.save(commentMapper.convertToEntity(commentDto));
        log.info("PostService in updateComment: Comment updated. New Comment: " + commentDto);

        return commentMapper.convertToDto(comment);

    }

    public void deleteComment(Long id, Long commentId) {

        commentRepository.delete(commentRepository.findById(commentId).orElseThrow(ObjectNotFoundException::new));
        log.info("PostService in deleteComment: At the post with id: {} comment with id: {} is deleted.", id, commentId);

    }

    public Page<CommentDto> getSubcomments(Long id, Long commentId, Pageable page) {

        CommentSearchDto commentSearchDto = new CommentSearchDto();
        commentSearchDto.setPostId(id);
        commentSearchDto.setParentId(commentId);
        Page<Comment> commentPage = commentRepository.findAll(getSpecification(commentSearchDto), page);
        return commentPage.map(commentMapper::convertToDto);

    }

    private Specification<Comment> getSpecification(CommentSearchDto searchDto) {
        return getBaseSpecification(searchDto)
                .and(equal(Comment_.postId, searchDto.getPostId(), true)
                        .and(equal(Comment_.parentId, searchDto.getParentId(), true)));
    }
}
