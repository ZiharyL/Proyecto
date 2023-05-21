package org.example.clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Coneccion {
        private static String dbUrl = "jdbc:mysql://localhost:3306/usuarios";
        private static String username = "root";
        private static String password = "12345";
        private static Connection connection;

        public Connection establishConnection() throws SQLException {
            if (connection == null) {
                connection = DriverManager.getConnection(dbUrl, username, password);
            }
            return connection;
        }




}

