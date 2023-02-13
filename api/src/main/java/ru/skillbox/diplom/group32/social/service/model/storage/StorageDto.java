package ru.skillbox.diplom.group32.social.service.model.storage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Schema(description = "Дто фото из StorageServices")
public class StorageDto {
    @Schema(description = "Путь до фото")
    private String photoPath;
    @Schema(description = "Имя фото")
    private String photoName;

}
