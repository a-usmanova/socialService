package ru.skillbox.diplom.group32.social.service.controller.geo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group32.social.service.model.city.CityDto;
import ru.skillbox.diplom.group32.social.service.model.country.CountryDto;
import ru.skillbox.diplom.group32.social.service.resource.geo.GeoController;
import ru.skillbox.diplom.group32.social.service.service.geo.GeoService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GeoControllerImpl implements GeoController {
    private final GeoService geoService;

    @Override
    public ResponseEntity<List<CountryDto>> getAll() {
        return new ResponseEntity<>(geoService.getAllCountries(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<CityDto>> getCitiesByCountryId(Long countryId) {
        return new ResponseEntity<>(geoService.getAllCities(countryId), HttpStatus.OK);
    }

    @Override
    public void loadGeo() {
        geoService.updateCity();
        geoService.updateCountry();
    }

}
