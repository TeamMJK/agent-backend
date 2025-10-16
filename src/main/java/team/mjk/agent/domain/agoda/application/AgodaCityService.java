package team.mjk.agent.domain.agoda.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.agoda.infrastructure.SQLiteDataSourceProvider;
import team.mjk.agent.domain.agoda.presentation.exception.CityNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
@Service
public class AgodaCityService {

    private final SQLiteDataSourceProvider dataSourceProvider;

    public String getCityId(String name) {
        String queryText = name + "*";
        String sql = "SELECT rowid FROM city_fts WHERE city MATCH ? OR dong MATCH ? LIMIT 1";

        try (Connection conn = dataSourceProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, queryText);
            ps.setString(2, queryText);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("rowid");
                }
            }

            throw new CityNotFoundException();

        } catch (SQLException e) {
            throw new RuntimeException("DB 쿼리 실행 중 오류 발생", e);
        }
    }

}
