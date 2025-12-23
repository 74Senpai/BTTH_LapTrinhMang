package homestay.Server.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class DBConnection {

    private static final Dotenv dotenv = 
        Dotenv.configure().directory("./homestay").load();

    private static final String URL =
            "jdbc:mysql://" + dotenv.get("DB_HOST") + ":" +
                    dotenv.get("DB_PORT") + "/" +
                    dotenv.get("DB_NAME") +
                    "?useSSL=false&serverTimezone=UTC";

    private static final String USER = dotenv.get("DB_USER");
    private static final String PASS = dotenv.get("DB_PASS");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
