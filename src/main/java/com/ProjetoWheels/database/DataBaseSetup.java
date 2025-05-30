package com.ProjetoWheels.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseSetup
{
    public static void criarTabelas()
    {
        String sql =
                """
                 CREATE TABLE IF NOT EXISTS bikes (
                 id INTEGER PRIMARY KEY AUTOINCREMENT,
                 modelo TEXT NOT NULL,
                 cor TEXT NOT NULL,
                 tipo TEXT NOT NULL,
                 status TEXT NOT NULL,
                 atributos_especificos TEXT
                 );
                """;

        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement())
        {
            stmt.execute(sql);
            System.out.println("Tabela 'bicicletas' verificada/criada com sucesso.");
        }
        catch (SQLException e)
        {
            System.err.println("Erro ao criar tabela: " + e.getMessage());
        }
    }
}
