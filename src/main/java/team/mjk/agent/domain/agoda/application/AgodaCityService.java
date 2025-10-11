package team.mjk.agent.domain.agoda.application;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.agoda.presentation.exception.CityCsvFormatException;
import team.mjk.agent.domain.agoda.presentation.exception.CityNotFoundException;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AgodaCityService {

    private final Map<String, String> cityMap = new HashMap<>();
    private final Map<String, String> dongMap = new HashMap<>();
    private static final String CSV_PATH ="/app/resources/hotels_city_country_dong.csv";
    //private static final String CSV_PATH = "/Users/choemyeongjae/agent-backend/src/main/resources/hotels_city_country_dong.csv";

    @PostConstruct
    public void init() {
        loadCitiesFromCsv();
    }

    public String getCityId(String cityName) {
        if (cityMap.containsKey(cityName)) {
            return cityMap.get(cityName);
        }

        for (Map.Entry<String, String> entry : cityMap.entrySet()) {
            if (entry.getKey().contains(cityName)) {
                return entry.getValue();
            }
        }

        for (Map.Entry<String, String> entry : dongMap.entrySet()) {
            if (entry.getKey().contains(cityName)) {
                return entry.getValue();
            }
        }

        throw new CityNotFoundException();
    }


    private void loadCitiesFromCsv() {
        try (BufferedReader br = Files.newBufferedReader(Path.of(CSV_PATH), StandardCharsets.UTF_8)) {
            br.readLine();
            br.lines().forEach(this::parseAndAddCity);
        } catch (Exception e) {
            throw new CityCsvFormatException();
        }
    }

    private void parseAndAddCity(String line) {
        if (line == null || line.isBlank()) return;

        String[] parts = line.split(",");
        if (parts.length < 4) {
            throw new CityCsvFormatException();
        }

        String cityId = parts[0].trim();
        String cityName = parts[2].trim();

        cityMap.put(cityName, cityId);

        if (parts.length > 4 && !parts[4].isBlank()) {
            String dong = parts[4].trim();
            dongMap.put(dong, cityId);
        }
    }

}