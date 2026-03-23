package ccv;
import java.sql.*;
public class dataconnection {
    static final String URL = "jdbc:mysql://localhost:3306/cv_db";
    static final String USER = "root";
    static final String PASS = "root123";
    
    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(URL, USER, PASS);
        return con;
    }
}