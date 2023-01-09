package ru.skillbox.diplom.group32.social.service.mapper.geo;

import java.util.List;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group32.social.service.model.city.City;
import ru.skillbox.diplom.group32.social.service.model.city.CityDto;
import ru.skillbox.diplom.group32.social.service.model.country.Country;
import ru.skillbox.diplom.group32.social.service.model.country.CountryDto;

@Mapper(componentModel = "spring")
public interface GeoMapper {

  @Mapping(target = "id", source = "id")
  CityDto convertToDto(City city);

  @Mapping(target = "id", source = "id")
  CountryDto convertToDto(Country country);

  @InheritInverseConfiguration
  City convertToEntity(CityDto cityDto);

  @InheritInverseConfiguration
  Country convertToEntity(CountryDto countryDtoDto);

  List<CountryDto> convertToCountryDtoList(List<Country> countries);

  List<CityDto> convertToCityDtoList(List<City> cities);

}

