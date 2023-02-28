package ru.skillbox.diplom.group32.social.service.service.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.exception.ObjectNotFoundException;
import ru.skillbox.diplom.group32.social.service.mapper.like.LikeMapper;
import ru.skillbox.diplom.group32.social.service.model.like.*;
import ru.skillbox.diplom.group32.social.service.model.post.Post;
import ru.skillbox.diplom.group32.social.service.model.post.comment.Comment;
import ru.skillbox.diplom.group32.social.service.repository.like.LikeRepository;
import ru.skillbox.diplom.group32.social.service.repository.post.PostRepository;
import ru.skillbox.diplom.group32.social.service.repository.post.comment.CommentRepository;

import java.time.ZonedDateTime;

import static ru.skillbox.diplom.group32.social.service.utils.security.SecurityUtil.getJwtUserIdFromSecurityContext;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeMapper likeMapper;
    public LikeDto createLike(Long itemId, LikeType type) {

        Like like = likeRepository.findByTypeAndItemIdAndAuthorId(type, itemId, getJwtUserIdFromSecurityContext()).orElse(new Like());
        if(like.getId() == null || like.getIsDeleted()) {
            like.setAuthorId(getJwtUserIdFromSecurityContext());
            like.setType(type);
            like.setItemId(itemId);
            like.setIsDeleted(false);
            like.setTime(ZonedDateTime.now());
            changeLikeAmount(itemId, type, 1);
        }
        log.info("LikeService in createPostLike has like to save: " + like);
        return likeMapper.convertToDto(likeRepository.save(like));

    }

    public void deleteLike(Long itemId, LikeType type) {

        log.info("LikeService in deletePostLike: trying to del like with itemId: " + itemId);
        Like like = likeRepository.findByTypeAndItemIdAndAuthorId(type, itemId, getJwtUserIdFromSecurityContext()).orElseThrow(ObjectNotFoundException::new);
        if(like.getId() != null) {
            changeLikeAmount(itemId, type, -1);
            likeRepository.deleteById(like.getId());
        }

    }

    public Boolean getMyLike(Long itemId, LikeType type) {

        Like like = likeRepository.findByTypeAndItemIdAndAuthorId(type, itemId, getJwtUserIdFromSecurityContext()).orElse(null);
        if (like == null) {
            return false;
        }
        return !like.getIsDeleted();

    }

    private void changeLikeAmount(Long itemId, LikeType type, int amount) {

        switch (type) {
            case POST -> {
                Post post = postRepository.findById(itemId).orElseThrow(ObjectNotFoundException::new);
                post.setLikeAmount(post.getLikeAmount() + amount);
                postRepository.save(post);
                log.info("LikeService in changeLikeAmount: likeAmount changed for " + type + " id - " + post.getId());
            }
            case COMMENT -> {
                Comment comment = commentRepository.findById(itemId).orElseThrow(ObjectNotFoundException::new);
                comment.setLikeAmount(comment.getLikeAmount() + amount);
                commentRepository.save(comment);
                log.info("LikeService in changeLikeAmount: likeAmount changed for " + type + " id - " + comment.getId());
            }
        }

    }
}
