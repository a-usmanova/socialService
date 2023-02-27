package ru.skillbox.diplom.group32.social.service.model.country;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skillbox.diplom.group32.social.service.model.base.BaseDto;
import ru.skillbox.diplom.group32.social.service.model.city.CityDto;

@Data
@Schema(description = "Дто страны")
public class CountryDto extends BaseDto {
  @Schema(description = "Наименование страны", example = "Россия")
  private String title;
  @Schema(description = "Города страны", example = "Россия: [Москва, Санкт-Петербург, Волгоград]" )
  private List<CityDto> cities;
}
