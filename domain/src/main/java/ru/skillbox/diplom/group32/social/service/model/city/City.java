package ru.skillbox.diplom.group32.social.service.model.city;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.skillbox.diplom.group32.social.service.model.base.BaseEntity;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "city")
public class City extends BaseEntity {

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private Long countryId;

}
