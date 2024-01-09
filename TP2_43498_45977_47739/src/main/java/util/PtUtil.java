package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PtUtil {

	static PreparedStatement statement = null;
	static ResultSet resultSet = null;

	public static ResultSet getPtInfo(Connection connection, String query) throws SQLException {
		statement = connection.prepareStatement(query);
		resultSet = statement.executeQuery();

		if (resultSet.next()) {
			return resultSet; } 
		else { return null; }
	}

    public static List<String> getClientes(Connection connection, String query) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<String> clientesStrings = new ArrayList<String>();

        try {
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
            	System.out.println();
                clientesStrings.add(resultSet.getString("Nome")); 
                System.out.println(clientesStrings);}
        } 
        finally {

            if (resultSet != null) { resultSet.close(); }
            if (statement != null) { statement.close(); }
        }


        return clientesStrings;
    }
	
    public static List<String> getNifClientes(Connection connection, String query) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<String> clientesStrings = new ArrayList<String>();

        try {
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                clientesStrings.add(resultSet.getString("NifCliente")); }
        } 
        finally {

            if (resultSet != null) { resultSet.close(); }
            if (statement != null) { statement.close(); }
        }

        return clientesStrings;
    }
    
    public static String getNomeCliente(Connection connection, String query) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
       
        String nomeCliente = null;
        
        try {
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
            	nomeCliente = resultSet.getString("Nome"); }
        } 
        finally {

            if (resultSet != null) { resultSet.close(); }
            if (statement != null) { statement.close(); }
        }
        
        return nomeCliente;
    }
    
    public static List<String> getEquipamentosDisponiveis(Connection connection, String query) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<String> availableEquipments = new ArrayList<>();

        try {
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                availableEquipments.add(resultSet.getString("Id"));
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        }

        return availableEquipments;
    }
    
    public static String getNomeEquipamento(Connection connection, String query) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String availableEquipments = null;

        try {
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
            	availableEquipments = resultSet.getString("Nome");
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        }

        return availableEquipments;
    }

    public static boolean addRecomendacao(Connection connection, String idPt, String nifCliente, String idEquipamento, String nifClube, String data, boolean usoSQL) {
        PreparedStatement statement = null;

        try {
        	String query = "INSERT INTO sbd_tp1_43498_45977_47739.pt_cliente_equipamento (`IdPt`, `NifCliente`, `IdEquipamento`, `NifClube`, `Data`, `Uso`)" +
        		    " VALUES (?, ?, ?, ?, ?, ?)" +
        		    " ON DUPLICATE KEY UPDATE `IdEquipamento` = VALUES(`IdEquipamento`), `Data` = VALUES(`Data`), `Uso` = VALUES(`Uso`)";

            statement = connection.prepareStatement(query);
            statement.setString(1, idPt);
            statement.setString(2, nifCliente);
            statement.setString(3, idEquipamento);
            statement.setString(4, nifClube);
            statement.setString(5, data);
            statement.setBoolean(6, usoSQL);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
}