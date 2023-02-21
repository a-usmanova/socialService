package ru.skillbox.diplom.group32.social.service.repository.geo;

import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group32.social.service.model.city.City;
import ru.skillbox.diplom.group32.social.service.repository.base.BaseRepository;

import java.util.List;

@Repository
public interface CityRepository extends BaseRepository<City> {
    List<City> findAllByCountryId(Long id);

    Boolean existsByTitle(String title);
}
