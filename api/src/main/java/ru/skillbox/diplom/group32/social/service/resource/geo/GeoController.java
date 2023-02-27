package ru.skillbox.diplom.group32.social.service.resource.geo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.skillbox.diplom.group32.social.service.model.city.CityDto;
import ru.skillbox.diplom.group32.social.service.model.country.CountryDto;
import ru.skillbox.diplom.group32.social.service.resource.utils.web.WebConstant;

import java.util.List;

@Tag(name = "Geo service", description = "Работа со странами и городами")
@RequestMapping(WebConstant.VERSION_URL + "/geo")
public interface GeoController {
    @GetMapping("/country")
    @Operation(summary = "Получение всех стран")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Список стран получен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {List.class, CountryDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    ResponseEntity<List<CountryDto>> getAll();

    @GetMapping("/country/{countryId}/city")
    @Operation(summary = "Получение списка городов по id страны")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Список городов получен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {List.class, CityDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Страна не найдена", content = @Content(mediaType = ""))})
    ResponseEntity<List<CityDto>> getCitiesByCountryId(@PathVariable Long countryId);

    @PutMapping("/load")
    @Operation(summary = "Загрузка стран и городов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Страны и города загружены"),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = ""))})
    void loadGeo();

}
