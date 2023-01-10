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
import ru.skillbox.diplom.group32.social.service.utils.security.SecurityUtil;

import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeMapper likeMapper;
    public LikeDto createLike(Long itemId, LikeType type) {

        LikeDto likeDto = createDto(itemId, type);
        log.info("LikeService in createPostLike has like to save: " + likeDto);
        likeRepository.findByTypeAndItemIdAndAuthorId(likeDto.getType(), itemId, likeDto.getAuthorId()).ifPresent(like -> likeDto.setId(like.getId()));
        changeLikeAmount(likeDto.getItemId(), likeDto.getType(), 1);
        return likeMapper.convertToDto(likeRepository.save(likeMapper.convertToEntity(likeDto)));

    }

    public void deleteLike(Long itemId, LikeType type) {

        log.info("LikeService in deletePostLike: trying to del like with itemId: " + itemId);
        LikeDto likeDto = createDto(itemId, type);
        Like like = likeRepository.findByTypeAndItemIdAndAuthorId(likeDto.getType(), itemId, likeDto.getAuthorId()).orElseThrow(ObjectNotFoundException::new);
        changeLikeAmount(likeDto.getItemId(), likeDto.getType(), -1);
        likeRepository.deleteById(like.getId());

    }

    public Boolean getMyLike(Long itemId, LikeType type) {

        Like like = likeRepository.findByTypeAndItemIdAndAuthorId(type, itemId, SecurityUtil.getJwtUserIdFromSecurityContext()).orElse(null);
        if (like == null) {
            return false;
        }
        return !like.getIsDeleted();

    }
    private LikeDto createDto(Long itemId, LikeType type) {

        LikeDto likeDto = new LikeDto();
        likeDto.setIsDeleted(false);
        likeDto.setTime(ZonedDateTime.now());
        likeDto.setItemId(itemId);
        likeDto.setType(type);
        likeDto.setAuthorId(SecurityUtil.getJwtUserIdFromSecurityContext());
        return likeDto;

    }

    private void changeLikeAmount(Long itemId, LikeType type, int amount) {

        switch (type) {
            case POST -> {
                Post post = postRepository.findById(itemId).orElseThrow(ObjectNotFoundException::new);
                post.setLikeAmount(post.getLikeAmount() + amount);
                postRepository.save(post);
            }
            case COMMENT -> {
                Comment comment = commentRepository.findById(itemId).orElseThrow(ObjectNotFoundException::new);
                comment.setLikeAmount(comment.getLikeAmount() + amount);
                commentRepository.save(comment);
            }
        }

    }
}
