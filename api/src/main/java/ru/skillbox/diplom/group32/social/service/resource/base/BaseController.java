package ru.skillbox.diplom.group32.social.service.resource.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group32.social.service.model.base.BaseDto;
import ru.skillbox.diplom.group32.social.service.model.base.BaseSearchDto;

public interface BaseController<Dto extends BaseDto, SearchDto extends BaseSearchDto> {

    @GetMapping(value = "/{id}")
    ResponseEntity<Dto> getById(@PathVariable Long id);

    @GetMapping(value = "/")
    ResponseEntity<Page<Dto>> getAll(@RequestBody SearchDto searchDto,
                                           @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable page);

    @PostMapping(value = "/create")
    ResponseEntity<Dto> create(@RequestBody Dto dto);

    @PutMapping(value = "/update")
    ResponseEntity<Dto> update(@RequestBody Dto dto);

    @DeleteMapping(value = "/{id}")
    ResponseEntity deleteById(@PathVariable Long id);


}
