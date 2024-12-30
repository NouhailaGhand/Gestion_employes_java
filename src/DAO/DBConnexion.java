package DAO;

import java.sql.*;

class DBConnexion {
    public static final String url = "jdbc:mysql://localhost:3306/tp";
    public static final String user = "root";
    public static final String password = "";
    public static Connection conn = null;

    public static Connection getConnexion() throws ClassNotFoundException {
        if(conn != null){
            return conn;
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("Error de connexion");
        }

        return conn;
    }
}

