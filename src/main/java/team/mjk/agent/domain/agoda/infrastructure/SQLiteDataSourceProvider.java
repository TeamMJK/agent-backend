package team.mjk.agent.domain.agoda.infrastructure;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public class SQLiteDataSourceProvider {

    @Value("${hotel.db-path}")
    private String dbPath;

    private HikariDataSource dataSource;

    @PostConstruct
    public void init() {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:sqlite:" + dbPath);
            config.setMaximumPoolSize(10);
            config.setPoolName("AgodaSQLitePool");

            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            throw new RuntimeException("DB 초기화 실패", e);
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @PreDestroy
    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

}
