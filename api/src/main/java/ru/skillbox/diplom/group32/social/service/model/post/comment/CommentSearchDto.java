package ru.skillbox.diplom.group32.social.service.model.post.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diplom.group32.social.service.model.base.BaseSearchDto;

@Data
@NoArgsConstructor
@Schema(description = "Дто для поиска комментов")
public class CommentSearchDto extends BaseSearchDto {

    @Schema(description = "Тип коммента: коммент к посту или субкоммент")
    private CommentType commentType;
    @Schema(description = "Айди автора")
    private Long authorId;
    @Schema(description = "Айди коммента")
    private Long parentId;
    @Schema(description = "айди поста")
    private Long postId;

    public CommentSearchDto(Long postId, CommentType commentType) {
        setIsDeleted(false);
        this.commentType = commentType;
        this.postId = postId;
    }

    public CommentSearchDto(Long postId, Long parentId, CommentType commentType ) {
        setIsDeleted(false);
        this.commentType = commentType;
        this.parentId = parentId;
        this.postId = postId;
    }
}
