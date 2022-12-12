package ru.skillbox.diplom.group32.social.service.repository.post.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group32.social.service.model.post.comment.Comment;
import ru.skillbox.diplom.group32.social.service.repository.base.BaseRepository;

@Repository
public interface CommentRepository extends BaseRepository<Comment> {

    Page<Comment> findAllByPostId(Long id, Pageable pageable);

}
