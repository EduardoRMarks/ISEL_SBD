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
			connection = DBConnectionManager.getConnection();

			String getIdQuery = "SELECT MAX(Id) + 1 AS NextId FROM equipamento";
			preparedStatement = connection.prepareStatement(getIdQuery);
			resultSet = preparedStatement.executeQuery();

			int nextId = 1;

			if (resultSet.next()) {
				nextId = resultSet.getInt("NextId");
			}

			String insertQuery = "INSERT INTO equipamento (Id, NifClube, Nome, Demonstracao, Estado, Imagem) VALUES (?, ?, ?, ?, ?, ?)";

			preparedStatement = connection.prepareStatement(insertQuery);
			preparedStatement.setInt(1, nextId);
			preparedStatement.setInt(2, equipment.getNifClube());
			preparedStatement.setString(3, equipment.getNome());
			preparedStatement.setString(4, equipment.getDemonstracao());
			preparedStatement.setInt(5, equipment.getEstado());
			preparedStatement.setString(6, equipment.getImagem());

			int rowsAffected = preparedStatement.executeUpdate();

			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			DBConnectionManager.closeResultSet(resultSet);
			DBConnectionManager.closeStatement(preparedStatement);
			DBConnectionManager.closeConnection(connection);
		}
	}

	public static boolean deleteEquipment(int nifClub, int equipmentId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = DBConnectionManager.getConnection();

			String query = "DELETE FROM equipamento WHERE NifClube = ? AND Id = ?";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, nifClub);
			preparedStatement.setInt(2, equipmentId);

			int rowsAffected = preparedStatement.executeUpdate();

			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			DBConnectionManager.closeStatement(preparedStatement);
			DBConnectionManager.closeConnection(connection);
		}
	}

	public static Equipment getEquipment(int nifClube, int equipmentId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = DBConnectionManager.getConnection();

			String query = "SELECT * FROM equipamento WHERE NifClube = ? AND Id = ?";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, nifClube);
			preparedStatement.setInt(2, equipmentId);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				Equipment equipamento = new Equipment();
				equipamento.setId(resultSet.getInt("Id"));
				equipamento.setNome(resultSet.getString("Nome"));
				equipamento.setDemonstracao(resultSet.getString("Demonstracao"));
				equipamento.setEstado(resultSet.getInt("Estado"));

				return equipamento;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnectionManager.closeResultSet(resultSet);
			DBConnectionManager.closeStatement(preparedStatement);
			DBConnectionManager.closeConnection(connection);
		}

		return null;
	}

	public static boolean updateEquipment(int NifClube, int equipmentId, String equipmentName, String Demo,
			int equipmentStatus) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = DBConnectionManager.getConnection();

			String query = "UPDATE equipamento SET Nome = ?, Demonstracao = ?, Estado = ? WHERE (Id = ? AND NifClube = ?) ";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, equipmentName);
			preparedStatement.setString(2, Demo);
			preparedStatement.setInt(3, equipmentStatus);
			preparedStatement.setInt(4, equipmentId);
			preparedStatement.setInt(5, NifClube);

			int rowsAffected = preparedStatement.executeUpdate();
			
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			DBConnectionManager.closeStatement(preparedStatement);
			DBConnectionManager.closeConnection(connection);
		}
	}

}
