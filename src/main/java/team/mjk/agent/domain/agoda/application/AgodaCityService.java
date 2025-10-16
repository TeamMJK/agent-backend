package team.mjk.agent.domain.agoda.application;

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

    public String getCityId(String name) {
        String query = "SELECT city_id FROM city WHERE city LIKE ? OR (dong IS NOT NULL AND dong LIKE ?) LIMIT 1";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             PreparedStatement ps = conn.prepareStatement(query)) {

            String likeQuery = "%" + name + "%";
            ps.setString(1, likeQuery);
            ps.setString(2, likeQuery);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("city_id");
                }
            }

            throw new CityNotFoundException();

        } catch (SQLException e) {
            throw new HotelInfoNotFoundException();
        }
    }

}

