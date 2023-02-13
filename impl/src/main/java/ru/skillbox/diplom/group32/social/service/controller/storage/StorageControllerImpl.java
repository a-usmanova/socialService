package ru.skillbox.diplom.group32.social.service.controller.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.group32.social.service.model.storage.StorageDto;
import ru.skillbox.diplom.group32.social.service.resource.storage.StorageController;
import ru.skillbox.diplom.group32.social.service.service.storage.StorageService;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StorageControllerImpl implements StorageController {

    private final StorageService storageService;

    @Override
    public ResponseEntity<StorageDto> storeAccountPhoto(MultipartFile file) throws IOException {
        return new ResponseEntity<>(storageService.store(file), HttpStatus.OK);
    }
}
