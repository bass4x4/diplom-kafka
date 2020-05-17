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

    private final static String INSERT_RESULT_QUERY =
            "INSERT INTO test_results (dateTime, linger, batchSize, compression, ack, idempotent, numberOfRecords, duration) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

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
            statement.executeQuery();
        }
    }

    public void insertRecord(Result result) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_RESULT_QUERY)) {
            statement.setString(1, getCurrentDateTime());
            statement.setInt(2, result.getLingerMs());
            statement.setInt(3, result.getBatchSize());
            statement.setString(4, result.getCompressionType());
            statement.setString(5, result.getAck());
            statement.setInt(6, result.isIdempotent() ? 1 : 0);
            statement.setInt(7, result.getNumberOfRecords());
            statement.setFloat(8, result.getDuration());
            statement.executeQuery();
        }
    }

    private String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        return timeFormatter.format(now);
    }
}
