package team.mjk.agent.domain.agoda.application;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.agoda.presentation.exception.CityNotFoundException;
import team.mjk.agent.domain.agoda.presentation.exception.HotelInfoNotFoundException;

import java.sql.*;

@RequiredArgsConstructor
@Service
public class AgodaCityService {

    @Value("${hotel.db-path}")
    private String dbPath;

    private PreparedStatement ps;

    @PostConstruct
    public void init() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

            ps = conn.prepareStatement(
                    "SELECT rowid FROM city_fts WHERE city MATCH ? OR dong MATCH ? LIMIT 1"
            );

        } catch (SQLException e) {
            throw new RuntimeException("DB 초기화 실패", e);
        }
    }

    public String getCityId(String name) {
        try {
            String queryText = name + "*";
            ps.setString(1, queryText);
            ps.setString(2, queryText);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("rowid");
                }
            }

            throw new CityNotFoundException();

        } catch (SQLException e) {
            throw new HotelInfoNotFoundException();
        }
    }

}
