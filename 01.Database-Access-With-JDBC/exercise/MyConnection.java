import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MyConnection {
    private static final String CONNECTION_STRING =
            "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "minions_db";

    private static Connection connection;

    private MyConnection() {

    }

    private static void createConnection() throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "");

        connection = DriverManager
                .getConnection(CONNECTION_STRING + DB_NAME, props);
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            createConnection();
        }

        return connection;
    }
}
