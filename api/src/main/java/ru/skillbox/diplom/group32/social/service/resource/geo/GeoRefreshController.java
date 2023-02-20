package ru.skillbox.diplom.group32.social.service.resource.geo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.skillbox.diplom.group32.social.service.model.country.AreaJsonDto;

import java.util.List;

@FeignClient(name = "GeoApi", url = "https://api.hh.ru")
public interface GeoRefreshController {

    @GetMapping(value = "/areas", produces = "application/json")
    List<AreaJsonDto> getAreas();


}
