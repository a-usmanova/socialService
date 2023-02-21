package ru.skillbox.diplom.group32.social.service.service.geo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.mapper.geo.GeoMapper;
import ru.skillbox.diplom.group32.social.service.model.city.City;
import ru.skillbox.diplom.group32.social.service.model.city.CityDto;
import ru.skillbox.diplom.group32.social.service.model.country.AreaJsonDto;
import ru.skillbox.diplom.group32.social.service.model.country.Country;
import ru.skillbox.diplom.group32.social.service.model.country.CountryDto;
import ru.skillbox.diplom.group32.social.service.repository.geo.CityRepository;
import ru.skillbox.diplom.group32.social.service.repository.geo.CountryRepository;
import ru.skillbox.diplom.group32.social.service.resource.geo.GeoRefreshController;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeoService {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final GeoMapper geoMapper;
    private final GeoRefreshController geoRefreshController;

    public List<CountryDto> getAllCountries() {
        List<CountryDto> result = geoMapper.convertToCountryDtoList(countryRepository.findAll());
        result.sort(Comparator.comparing(CountryDto::getTitle));
        return result;
    }

    public List<CityDto> getAllCities(Long countryId) {
        List<CityDto> cities = geoMapper.convertToCityDtoList(cityRepository.findAllByCountryId(countryId));
        cities.sort(Comparator.comparing(CityDto::getTitle));
        return cities;
    }

    @Scheduled(cron = "@monthly")
    public void updateCountry() {
        List<AreaJsonDto> areas = Collections.emptyList();
        areas = geoRefreshController.getAreas();

        if (areas.isEmpty()) {
            return;
        }
        areas.stream().forEach(area -> {
            if (!area.getName().equals("Другие регионы") && !countryRepository.existsByTitle(area.getName()) && area.getParentId() == null) {
                Country country = new Country();
                country.setTitle(area.getName());
                country.setIsDeleted(false);
                countryRepository.save(country);
            }
        });
    }

    @Scheduled(cron = "@monthly")
    public void updateCity() {
        List<AreaJsonDto> areas = Collections.emptyList();
        areas = geoRefreshController.getAreas();

        areas.stream()
                .filter(areaCountry -> !areaCountry.getName().equals("Другие регионы") && countryRepository.existsByTitle(areaCountry.getName()) && areaCountry.getParentId() == null)
                .forEach(areaCountry -> {
                    Long countryId = countryRepository.getCountryByTitle(areaCountry.getName()).getId();

                    Arrays.stream(areaCountry.getAreas())
                            .forEach(areaCity -> {
                                if (areaCity.getAreas().length == 0 && !cityRepository.existsByTitle(areaCity.getName())) {
                                    City city = new City();
                                    city.setTitle(areaCity.getName());
                                    city.setCountryId(countryId);
                                    city.setIsDeleted(false);
                                    cityRepository.save(city);
                                } else {
                                    Arrays.stream(areaCity.getAreas())
                                            .filter(innerAreaCity -> !cityRepository.existsByTitle(innerAreaCity.getName()))
                                            .forEach(innerAreaCity -> {
                                                City city = new City();
                                                city.setTitle(innerAreaCity.getName());
                                                city.setCountryId(countryId);
                                                city.setIsDeleted(false);
                                                cityRepository.save(city);
                                            });
                                }
                            });
                });
    }

}
