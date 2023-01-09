package ru.skillbox.diplom.group32.social.service.service.geo;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group32.social.service.mapper.geo.GeoMapper;
import ru.skillbox.diplom.group32.social.service.model.city.City;
import ru.skillbox.diplom.group32.social.service.model.city.CityDto;
import ru.skillbox.diplom.group32.social.service.model.country.Country;
import ru.skillbox.diplom.group32.social.service.model.country.CountryDto;
import ru.skillbox.diplom.group32.social.service.repository.geo.CityRepository;
import ru.skillbox.diplom.group32.social.service.repository.geo.CountryRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeoService {

  private final CityRepository cityRepository;
  private final CountryRepository countryRepository;
  private final GeoMapper geoMapper;

  public List<CountryDto> getAllCountries() {
    return geoMapper.convertToCountryDtoList(countryRepository.findAll());
  }

  public List<CityDto> getAllCities(Long countryId) {
    List<CityDto> cities = geoMapper.convertToCityDtoList(cityRepository.findAllByCountryId(countryId));
    return cities;
  }

}
