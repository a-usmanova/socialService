package ru.skillbox.diplom.group32.social.service.service.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.group32.social.service.config.Properties;
import ru.skillbox.diplom.group32.social.service.model.account.AccountDto;
import ru.skillbox.diplom.group32.social.service.service.account.AccountService;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    private final Properties properties;


    public Map store(MultipartFile image) throws IOException {

        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", properties.getCloudName(),
                "api_key", properties.getApiKey(),
                "api_secret", properties.getApiSecret()));

        Map params = ObjectUtils.asMap("transformation",
                new Transformation()
                        .height(400).width(400).crop("pad"),
                "filename_override", image.getOriginalFilename());

        Map uploadResult = cloudinary.uploader().upload(image.getBytes(), params);
        log.info("StorageService storePhoto: successfully uploaded the file: " + uploadResult.get("original_filename"));

        return uploadResult;
    }


}
