package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Equipment;
import model.Room;

public class EquipmentUtil {

	public static List<Equipment> getEquipmentList(String selectedClub) {
		List<Equipment> equipmentList = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			int nif = ClubUtil.getClubNif(selectedClub);
			connection = DBConnectionManager.getConnection();
			String query = "SELECT * FROM equipamento WHERE NifClube = ?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, nif);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Equipment equipment = new Equipment();
				equipment.setId(resultSet.getInt("Id"));
				equipment.setNifClube(resultSet.getInt("NifClube"));
				equipment.setNome(resultSet.getString("Nome"));
				equipment.setDemonstracao(resultSet.getString("Demonstracao"));
				equipment.setEstado(resultSet.getInt("Estado"));
				equipment.setImagem(resultSet.getString("Imagem"));
				equipmentList.add(equipment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// Handle the exception appropriately
		} finally {
			DBConnectionManager.close(resultSet, statement, connection);
		}

		return equipmentList;
	}

	public static boolean addEquipment(Equipment equipment) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// Get a database connection
			connection = DBConnectionManager.getConnection();

			// Get the next available Id from the table
			String getIdQuery = "SELECT MAX(Id) + 1 AS NextId FROM equipamento";
			preparedStatement = connection.prepareStatement(getIdQuery);
			resultSet = preparedStatement.executeQuery();

			int nextId = 1; // Default value if the table is empty

			if (resultSet.next()) {
				nextId = resultSet.getInt("NextId");
			}

			// Define the SQL query for adding equipment
			String insertQuery = "INSERT INTO equipamento (Id, NifClube, Nome, Demonstracao, Estado, Imagem) VALUES (?, ?, ?, ?, ?, ?)";

			// Create a prepared statement
			preparedStatement = connection.prepareStatement(insertQuery);
			preparedStatement.setInt(1, nextId);
			preparedStatement.setInt(2, equipment.getNifClube());
			preparedStatement.setString(3, equipment.getNome());
			preparedStatement.setString(4, equipment.getDemonstracao());
			preparedStatement.setInt(5, equipment.getEstado());
			preparedStatement.setString(6, equipment.getImagem());

			// Execute the query
			int rowsAffected = preparedStatement.executeUpdate();

			// Check if the equipment was added successfully
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			// Close resources
			DBConnectionManager.closeResultSet(resultSet);
			DBConnectionManager.closeStatement(preparedStatement);
			DBConnectionManager.closeConnection(connection);
		}
	}

	public static boolean deleteEquipment(int nifClub, int equipmentId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			// Get a database connection
			connection = DBConnectionManager.getConnection();

			// Define the SQL query for deleting a room
			String query = "DELETE FROM equipamento WHERE NifClube = ? AND Id = ?";

			// Create a prepared statement
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, nifClub);
			preparedStatement.setInt(2, equipmentId);

			// Execute the query
			int rowsAffected = preparedStatement.executeUpdate();

			// Check if the room was deleted successfully
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			// Close resources
			DBConnectionManager.closeStatement(preparedStatement);
			DBConnectionManager.closeConnection(connection);
		}
	}

	public static Equipment getEquipment(int nifClube, int equipmentId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// Get a database connection
			connection = DBConnectionManager.getConnection();

			// Define the SQL query for retrieving room information
			String query = "SELECT * FROM equipamento WHERE NifClube = ? AND Id = ?";

			// Create a prepared statement
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, nifClube);
			preparedStatement.setInt(2, equipmentId);

			// Execute the query
			resultSet = preparedStatement.executeQuery();

			// Check if any result is returned
			if (resultSet.next()) {
				// Extract room information from the result set
				Equipment equipamento = new Equipment();
				equipamento.setId(resultSet.getInt("Id"));
				equipamento.setNome(resultSet.getString("Nome"));
				equipamento.setDemonstracao(resultSet.getString("Demonstracao"));
				equipamento.setEstado(resultSet.getInt("Estado"));

				// Add more fields as needed

				return equipamento;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Close resources
			DBConnectionManager.closeResultSet(resultSet);
			DBConnectionManager.closeStatement(preparedStatement);
			DBConnectionManager.closeConnection(connection);
		}

		return null; // Return null if room is not found or an error occurs
	}

	public static boolean updateEquipment(int NifClube, int equipmentId, String equipmentName, String Demo,
			int equipmentStatus) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			// Get a database connection
			connection = DBConnectionManager.getConnection();

			// Define the SQL query for updating room information
			String query = "UPDATE equipamento SET Nome = ?, Demonstracao = ?, Estado = ? WHERE (Id = ? AND NifClube = ?) ";

			// Create a prepared statement
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, equipmentName);
			preparedStatement.setString(2, Demo);
			preparedStatement.setInt(3, equipmentStatus);
			preparedStatement.setInt(4, equipmentId);
			preparedStatement.setInt(5, NifClube);

			// Execute the update query
			int rowsAffected = preparedStatement.executeUpdate();

			// Check if the update was successful
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			// Close resources
			DBConnectionManager.closeStatement(preparedStatement);
			DBConnectionManager.closeConnection(connection);
		}
	}

}
