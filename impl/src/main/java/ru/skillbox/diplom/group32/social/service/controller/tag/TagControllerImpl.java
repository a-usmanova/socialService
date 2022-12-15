package ru.skillbox.diplom.group32.social.service.controller.tag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group32.social.service.model.tag.TagDto;
import ru.skillbox.diplom.group32.social.service.model.tag.TagSearchDto;
import ru.skillbox.diplom.group32.social.service.resource.tag.TagController;
import ru.skillbox.diplom.group32.social.service.service.tag.TagService;

import java.util.List;

@Slf4j
@RestController
public class TagControllerImpl implements TagController {
    final TagService tagService;

    TagControllerImpl(TagService tagService) {
        this.tagService = tagService;
    }
    @Override
    public ResponseEntity<TagDto> getById(Long id) {
        return ResponseEntity.ok(tagService.getById(id));
    }

    @Override
    public ResponseEntity<Page<TagDto>> getAll(TagSearchDto searchDto, Pageable page) {
        return new ResponseEntity(tagService.getAll(searchDto), HttpStatus.OK);
    }

    @Override
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<TagDto> create(TagDto dto) {
        return ResponseEntity.ok(tagService.create(dto));
    }

    @Override
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<TagDto> update(TagDto dto) {
        return ResponseEntity.ok(tagService.update(dto));
    }

    @Override
    public ResponseEntity deleteById(Long id) {
        return null;
    }
}
