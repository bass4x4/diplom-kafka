package Backend;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class ClickHouseDao {
    private static final String DB_URL = "jdbc:clickhouse://localhost:8123/default";

    private Connection connection;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //language=ClickHouse
    private final static String INSERT_QUERY =
            "INSERT INTO test (dateTime, message) VALUES (?, ?)";

    public ClickHouseDao() {
        try {
            Properties properties = new Properties();
            properties.put("compress", "false");
            connection = DriverManager.getConnection(DB_URL, properties);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void insertRecord(String message) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            statement.setString(1, getCurrentDateTime());
            statement.setString(2, message);
            try (ResultSet rs = statement.executeQuery()) {
            }
        }
    }

    private String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        return timeFormatter.format(now);
    }
}
