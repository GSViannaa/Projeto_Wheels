package com.ProjetoWheels.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexao {

    private static final Properties config = new Properties();

    static {
        try (InputStream input = Conexao.class.getClassLoader().getResourceAsStream("database.properties"))
        {
            if (input == null)
            {
                throw new RuntimeException("Arquivo não encontrado!");
            }

            config.load(input);

        }
        catch (IOException e)
        {
            throw new RuntimeException("Erro ao carregar configurações do banco", e);
        }
    }

    public static Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(
                config.getProperty("db.url"),
                config.getProperty("db.usuario"),
                config.getProperty("db.senha")
        );
    }
}
