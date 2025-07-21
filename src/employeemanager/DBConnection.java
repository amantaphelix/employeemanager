package employeemanager;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.*;

public class DBConnection {

    private static final Dotenv dotenv = Dotenv.configure()
                                               .directory("src")
                                               .load();

    public static Connection getConnection() throws SQLException {
        String url = dotenv.get("DB_URL");
        String user = dotenv.get("DB_USER");
        String password = dotenv.get("DB_PASSWORD");
        return DriverManager.getConnection(url, user, password);
    }

    public static void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed())
                conn.close();
        } catch (SQLException e) {
            System.err.println("Error while closing connection: " + e.getMessage());
        }
    }
}
