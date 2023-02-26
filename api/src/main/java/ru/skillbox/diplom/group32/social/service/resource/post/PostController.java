package ru.skillbox.diplom.group32.social.service.resource.post;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group32.social.service.model.like.LikeDto;
import ru.skillbox.diplom.group32.social.service.model.post.PostDto;
import ru.skillbox.diplom.group32.social.service.model.post.PostSearchDto;
import ru.skillbox.diplom.group32.social.service.model.post.comment.CommentDto;
import ru.skillbox.diplom.group32.social.service.resource.base.BaseController;
import ru.skillbox.diplom.group32.social.service.resource.utils.web.WebConstant;
@Tag(name = "Post service", description = "Работа с постами и комментариями")
@RequestMapping(value = WebConstant.VERSION_URL  + "/post")
public interface PostController extends BaseController<PostDto, PostSearchDto> {

    @Override
    @GetMapping(value = "/{id}")
    @Operation(summary = "Получение поста")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Пост получен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {PostDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Пост не найден", content = @Content(mediaType = ""))})
    ResponseEntity<PostDto> getById(@Schema(description = "id поста") @PathVariable Long id);

    @Override
    @GetMapping
    @Operation(summary = "Получение постов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Посты получены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {Pageable.class, PostDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Посты не найдены", content = @Content(mediaType = ""))})
    ResponseEntity<Page<PostDto>> getAll(PostSearchDto searchDto, Pageable page);

    @Override
    @PostMapping
    @Operation(summary = "Создание поста")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Пост создан",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {PostDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = ""))})
    ResponseEntity<PostDto> create(@RequestBody PostDto dto);

    @PutMapping
    @Operation(summary = "Обновление поста")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Пост обновлен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {PostDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Посты не найдены", content = @Content(mediaType = ""))})
    ResponseEntity<PostDto> update(@Schema(description = "id Поста для обновления") @RequestBody PostDto dto);

    @Override
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удаление поста")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Пост удален",
                    content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Посты не найдены", content = @Content(mediaType = ""))})
    ResponseEntity<PostDto> deleteById(@Schema(description = "id Поста для удаления") @PathVariable Long id);

//
//    -----------***COMMENTS***-----------
//

    @GetMapping(value = "{id}/comment")
    @Operation(summary = "Получение комментов к посту")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Комменты получены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {Pageable.class, CommentDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Комменты не найдены", content = @Content(mediaType = ""))})
    ResponseEntity<Page<CommentDto>> getComment(@Schema(description = "id поста")
                              @PathVariable Long id, Pageable page);

    @GetMapping(value = "{id}/comment/{commentId}/subcomment")
    @Operation(summary = "Получение субкомментов к комменту")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Субкомменты получены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {Pageable.class, CommentDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Субкомменты не найдены", content = @Content(mediaType = ""))})
    ResponseEntity<Page<CommentDto>> getSubcomment(@Schema(description = "id поста") @PathVariable Long id,
                                 @Schema(description = "id коммента") @PathVariable Long commentId,
                                 @PageableDefault(sort = {"parentId"}, direction = Sort.Direction.DESC) Pageable page);

    @PostMapping(value = "{id}/comment")
    @Operation(summary = "Создание коммента к посту")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Комент создан",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {CommentDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Пост не найден", content = @Content(mediaType = ""))})
    ResponseEntity<CommentDto> createComment(@Schema(description = "id поста") @PathVariable Long id, @RequestBody CommentDto commentDto);

    @PutMapping(value = "{id}/comment/{commentId}")
    @Operation(summary = "Создание субкоммента к комменту")
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Субкомент создан",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {CommentDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Коммент не найден", content = @Content(mediaType = ""))})
    ResponseEntity<CommentDto> updateComment(@Schema(description = "id поста") @PathVariable Long id,
                                 @Schema(description = "id комментария")
                                 @RequestBody CommentDto commentDto,
                                 @PathVariable Long commentId);

    @DeleteMapping(value = "{id}/comment/{commentId}")
    @Operation(summary = "Удаление комментария")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Коммент удален",
                    content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Коммент не найден", content = @Content(mediaType = ""))})
    ResponseEntity<CommentDto> deleteComment(@Schema(description = "id Поста") @PathVariable Long id, @Schema(description = "id комментария для удаления") @PathVariable Long commentId);

//
//    -----------***LIKES***-----------
//
  @PostMapping(value = "{id}/like")
  @Operation(summary = "Создание лайка к посту")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201",
                  description = "Лайк к посту создан",
                  content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                          schema = @Schema(allOf = {LikeDto.class}))),
          @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
          @ApiResponse(responseCode = "404", content = @Content(mediaType = ""))})
  ResponseEntity<LikeDto> createPostLike(@PathVariable Long id);

  @DeleteMapping(value = "{id}/like")
  @Operation(summary = "Удаление лайка поста")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200",
                  description = "Лайк поста удален",
                  content = @Content(mediaType = "")),
          @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
          @ApiResponse(responseCode = "404", content = @Content(mediaType = ""))})
  ResponseEntity deletePostLike(@PathVariable Long id);

  @PostMapping(value = "{id}/comment/{commentId}/like")
  @Operation(summary = "Создание лайка к комментарию")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201",
                  description = "Лайк комментария создан",
                  content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                          schema = @Schema(allOf = {LikeDto.class}))),
          @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
          @ApiResponse(responseCode = "404", content = @Content(mediaType = ""))})
  ResponseEntity<LikeDto> createCommentLike(@PathVariable Long id, @PathVariable Long commentId);

  @DeleteMapping(value = "{id}/comment/{commentId}/like")
  @Operation(summary = "Удаление лайка комментария")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200",
                  description = "Лайк комментария удален",
                  content = @Content(mediaType = "")),
          @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
          @ApiResponse(responseCode = "404", content = @Content(mediaType = ""))})
   ResponseEntity deleteCommentLike(@PathVariable Long id, @PathVariable Long commentId);

}
