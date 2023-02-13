package ru.skillbox.diplom.group32.social.service.resource.storage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.group32.social.service.model.storage.StorageDto;
import ru.skillbox.diplom.group32.social.service.resource.utils.web.WebConstant;

import java.io.IOException;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Tag(name = "Storage service", description = "Работа с сохранением фото")
@RequestMapping(value = WebConstant.VERSION_URL)
public interface StorageController {

    @PostMapping(value = "/storage", consumes = {MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Создание фото к аккаунту")
    ResponseEntity<StorageDto> storeAccountPhoto(@Schema(description = "Файл фото") @RequestParam(value = "file", required = false) MultipartFile file) throws IOException;


}
