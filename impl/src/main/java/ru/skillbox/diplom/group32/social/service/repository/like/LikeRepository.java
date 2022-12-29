package ru.skillbox.diplom.group32.social.service.repository.like;

import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group32.social.service.model.like.Like;
import ru.skillbox.diplom.group32.social.service.model.like.LikeType;
import ru.skillbox.diplom.group32.social.service.repository.base.BaseRepository;

import java.util.Optional;

@Repository
public interface LikeRepository extends BaseRepository<Like> {
    Optional<Like> findByTypeAndItemIdAndAuthorId(LikeType type, Long itemId, Long authorId);
}
