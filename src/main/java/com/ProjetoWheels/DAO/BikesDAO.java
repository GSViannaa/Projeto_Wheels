package com.ProjetoWheels.DAO;

import com.ProjetoWheels.database.Conexao;
import com.ProjetoWheels.enums.bikes.StatusBikes;
import com.ProjetoWheels.enums.bikes.TamanhoQuadro;
import com.ProjetoWheels.enums.bikes.TemRodinhas;
import com.ProjetoWheels.enums.bikes.TipoPneu;
import com.ProjetoWheels.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BikesDAO {
    public static void salvarNoBancoDeDados(Bikes b) {
        String sql = "INSERT INTO Bikes (modelo, cor, tipo, status, atributos_especificos) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = Conexao.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, b.getModelo());
            stmt.setString(2, b.getCor());
            stmt.setString(3, b.getClass().getSimpleName());
            stmt.setString(4, b.getStatusDisponibilidade().name());

            if (b instanceof MountainBikes mountainBikes) {
                stmt.setString(5, mountainBikes.getBikeTipoPneu().name());
            } else if (b instanceof SpeedBikes speedBike) {
                stmt.setString(5, speedBike.getBikeTamanhoQuadro().name());
            } else if (b instanceof ChildrensBikes childrensBike) {
                stmt.setString(5, childrensBike.getBikeTemRodinhas().name());
            } else {
                stmt.setString(5, null);
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar bicicleta no banco de dados: " + e.getMessage());
        }

    }

    public static List<Bikes> listarBikes() {
        List<Bikes> listaBikes = new ArrayList<>();

        String sql = "SELECT * FROM bikes";

        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String modelo = rs.getString("modelo");
                String cor = rs.getString("cor");
                String status = rs.getString("status");
                String atributo = rs.getString("atributos_especificos");
                String tipo = rs.getString("tipo");

                StatusBikes statusBikes = StatusBikes.valueOf(status);

                Bikes bike = null;

                if ("MountainBike".equalsIgnoreCase(tipo)) {
                    bike = new MountainBikes(id, modelo, cor, TipoPneu.valueOf(atributo));
                } else if ("SpeedBikes".equalsIgnoreCase(tipo)) {
                    bike = new SpeedBikes(id, modelo, cor, TamanhoQuadro.valueOf(atributo));
                } else if ("ChildrensBikes".equalsIgnoreCase(tipo)) {
                    bike = new ChildrensBikes(id, modelo, cor, TemRodinhas.valueOf(atributo));
                } else {
                    bike = new DefaultBikes(id, modelo, cor);
                }

                if (bike != null) {
                    bike.setStatusDisponibilidade(statusBikes);
                    listaBikes.add(bike);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao listar bicicletas: " + e.getMessage());
        }

        return listaBikes;
    }

    public static List<Bikes> ListarBikesPorTipo(String tipo) {
        List<Bikes> listaBikes = new ArrayList<>();

        String sql = "SELECT * FROM bikes WHERE tipo = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setString(1, tipo);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String modelo = rs.getString("modelo");
                String cor = rs.getString("cor");
                String atributo = rs.getString("atributos_especificos");
                String statusStr = rs.getString("status");

                StatusBikes status = StatusBikes.valueOf(statusStr);

                Bikes bike = null;

                if ("MountainBikes".equalsIgnoreCase(tipo)) {
                    bike = new MountainBikes(id, modelo, cor, TipoPneu.valueOf(atributo));
                } else if ("SpeedBikes".equalsIgnoreCase(tipo)) {
                    bike = new SpeedBikes(id, modelo, cor, TamanhoQuadro.valueOf(atributo));
                } else if ("ChildrensBikes".equalsIgnoreCase(tipo)) {
                    bike = new ChildrensBikes(id, modelo, cor, TemRodinhas.valueOf(atributo));
                } else {
                    bike = new DefaultBikes(id, modelo, cor);
                }

                if (bike != null) {
                    bike.setStatusDisponibilidade(status);
                    listaBikes.add(bike);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao listar bicicletas: " + e.getMessage());
        }

        return listaBikes;
    }

    public static List<Bikes> buscarBikesPorModelo(String modelo)
    {
        List<Bikes> listaBikes = new ArrayList<>();
        String sql = "SELECT * FROM bikes WHERE modelo = ? and status= ? ";

        try (Connection conexao = Conexao.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql))
        {
            stmt.setString(1, modelo);
            stmt.setString(2, StatusBikes.DISPONIVEL.name());

            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                int id = rs.getInt("id");
                String tipo= rs.getString("tipo");
                String cor = rs.getString("cor");
                String atributo = rs.getString("atributos_especificos");
                String statusStr = rs.getString("status");

                StatusBikes status = StatusBikes.valueOf(statusStr);

                Bikes bike = null;


                if ("MountainBikes".equalsIgnoreCase(tipo))
                {
                    bike = new MountainBikes(id, modelo, cor, TipoPneu.valueOf(atributo));
                }
                else if ("SpeedBikes".equalsIgnoreCase(tipo))
                {
                    bike = new SpeedBikes(id, modelo, cor, TamanhoQuadro.valueOf(atributo));
                }
                else if ("ChildrensBikes".equalsIgnoreCase(tipo))
                {
                    bike = new ChildrensBikes(id, modelo, cor, TemRodinhas.valueOf(atributo));
                }
                else
                {
                    bike = new DefaultBikes(id, modelo, cor);
                }

                  listaBikes.add(bike);
                }

            }
             catch (SQLException e)
             {
             e.printStackTrace();
             }

             return listaBikes;

        }


    }


