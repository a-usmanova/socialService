package ru.skillbox.diplom.group32.social.service.model.storage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Schema(description = "Дто фото из StorageServices")
public class StorageDto {
    @Schema(description = "Путь до фото", example = "http://res.cloudinary.com/duvaewonz/image/upload/v1677442010/xfaazue6lvk7ilrkkycl.jpg")
    private String photoPath;
    @Schema(description = "Имя фото", name = "аватарка v1")
    private String photoName;

}
