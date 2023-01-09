package ru.skillbox.diplom.group32.social.service.model.country;

import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.skillbox.diplom.group32.social.service.model.base.BaseEntity;
import ru.skillbox.diplom.group32.social.service.model.city.City;
import ru.skillbox.diplom.group32.social.service.model.city.CityDto;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "country")
public class Country extends BaseEntity {

  @Column(nullable = false)
  private String title;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "countryId")
  private List<City> cities;
}
