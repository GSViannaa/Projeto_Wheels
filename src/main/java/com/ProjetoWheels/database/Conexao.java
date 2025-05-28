package com.ProjetoWheels.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao
{

    private static final String URLDataBase = "jdbc:sqlite:C:\\Users\\bernardo.cytryn\\IdeaProjects\\Projeto_Wheels\\wheels.sqlite";

    public static Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(URLDataBase);
    }


}
