import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() {
        Connection con = null;
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to database
           con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/lab_management?useSSL=false&allowPublicKeyRetrieval=true",
    "root",
    "root"
);


        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        return con;
    }
}
