package team.mjk.agent.domain.agoda.application;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.agoda.presentation.exception.CityCsvFormatException;
import team.mjk.agent.domain.agoda.presentation.exception.CityNotFoundException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class AgodaCityService {

    private final Map<String, String> cityMap = new HashMap<>();

    @PostConstruct
    public void init() {
        loadCitiesFromCsv();
    }

    public String getCityId(String cityName) {
        String cityId = cityMap.get(cityName);
        if (cityId == null) {
            throw new CityNotFoundException();
        }
        return cityId;
    }

    private void loadCitiesFromCsv() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(getClass().getResourceAsStream("/hotels_city_country.csv")),
                        StandardCharsets.UTF_8))) {

            br.readLine();
            br.lines().forEach(this::parseAndAddCity);

        } catch (Exception e) {
            throw new CityCsvFormatException();
        }
    }

    private void parseAndAddCity(String line) {
        String[] parts = line.split(",");
        if (parts.length < 4) {
            throw new CityCsvFormatException();
        }
        String cityName = parts[2].trim();
        String cityId = parts[0].trim();
        cityMap.put(cityName, cityId);
    }

}
