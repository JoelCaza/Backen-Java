package com.webdev.clase2eventos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DatabaseConnectionTest { // ¡Es una class!

    @Autowired
    private DataSource dataSource;

    @Test
    void probarConexion() {
        try (Connection connection = dataSource.getConnection()) {

            assertNotNull(connection);
            System.out.println("¡Conexión exitosa!");
            System.out.println("Base de datos conectada: " + connection.getCatalog());

        } catch (SQLException e) {
            System.err.println("Error al conectar: " + e.getMessage());
        }
    }
}