package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {

    private static String urlConexion = "jdbc:postgresql://localhost:5432/videoclub";
    private static String usuario = "postgres";
    private static String password = "postgres";

    public static Connection conectarServidor() throws SQLException {
        Connection connection = DriverManager.getConnection(urlConexion,usuario,password);

        return connection;
    }
}



