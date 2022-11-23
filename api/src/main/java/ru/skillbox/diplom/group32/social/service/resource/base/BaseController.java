package ru.skillbox.diplom.group32.social.service.resource.base;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group32.social.service.model.base.BaseDto;
import ru.skillbox.diplom.group32.social.service.model.base.BaseSearchDto;

public interface BaseController<Dto extends BaseDto, SearchDto extends BaseSearchDto> {

    @PostMapping(value = "/create")
    ResponseEntity<Dto> create(@RequestBody Dto dto);

    @GetMapping(value = "/{id}")
    ResponseEntity<Dto> getById(@PathVariable Long id);

    @GetMapping(value = "/")
    ResponseEntity<Page<Dto>> getAll(SearchDto searchDto);

    @PostMapping(value = "/update")
    ResponseEntity<Dto> update(@RequestBody Dto dto);

    @DeleteMapping(value = "/{id}")
    void deleteById(@PathVariable Long id);

}
