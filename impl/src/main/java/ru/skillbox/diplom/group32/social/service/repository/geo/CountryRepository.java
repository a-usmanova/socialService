package ru.skillbox.diplom.group32.social.service.repository.geo;

import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group32.social.service.model.country.Country;
import ru.skillbox.diplom.group32.social.service.repository.base.BaseRepository;

@Repository
public interface CountryRepository extends BaseRepository<Country> {

    Boolean existsByTitle(String countryTitle);

    Country getCountryByTitle(String countryTitle);

}
