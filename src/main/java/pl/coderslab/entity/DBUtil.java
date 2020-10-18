package pl.coderslab.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    public static Connection connectToDB() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/workshop2?serverTimezone=UTC" +
                        "&useSSL=false&characterEncoding=UTF8",
                "root", "coderslab");
    }
}