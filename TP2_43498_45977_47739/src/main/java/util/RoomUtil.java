package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Equipment;
import model.Room;

public class RoomUtil {

	public static List<Room> getRoomList(String selectedClub) {
		List<Room> roomList = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			int nif = ClubUtil.getClubNif(selectedClub);
			connection = DBConnectionManager.getConnection();
			String query = "SELECT * FROM sala WHERE NifClube = ?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, nif);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Room room = new Room();
				room.setId(resultSet.getInt("Id"));
				room.setNifClube(resultSet.getInt("NifClube"));
				room.setNome(resultSet.getString("Nome"));
				room.setOcupacaoMaxima(resultSet.getInt("OcupacaoMaxima"));
				room.setEstado(resultSet.getInt("Estado"));
				room.setImagem(resultSet.getString("Imagem"));
				roomList.add(room);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			// Handle the exception appropriately
		}
		finally {
			DBConnectionManager.close(resultSet, statement, connection);
		}

		return roomList;
	}
	
	public static boolean addRoom(Room room) {

	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;

	    try {
	        // Get a database connection
	        connection = DBConnectionManager.getConnection();

	        // Get the next available Id from the table
	        String getIdQuery = "SELECT MAX(Id) + 1 AS NextId FROM sala";
	        preparedStatement = connection.prepareStatement(getIdQuery);
	        resultSet = preparedStatement.executeQuery();

	        int nextId = 1; // Default value if the table is empty

	        if (resultSet.next()) {
	            nextId = resultSet.getInt("NextId");
	        }

	        // Define the SQL query for adding equipment
	        String insertQuery = "INSERT INTO sala (Id, NifClube, Nome, OcupacaoMaxima, Estado, Imagem) VALUES (?, ?, ?, ?, ?, ?)";

	        // Create a prepared statement
	        preparedStatement = connection.prepareStatement(insertQuery);
	        preparedStatement.setInt(1, nextId);
	        preparedStatement.setInt(2, room.getNifClube());
	        preparedStatement.setString(3, room.getNome());
	        preparedStatement.setInt(4, room.getOcupacaoMaxima());
	        preparedStatement.setInt(5, room.getEstado());
	        preparedStatement.setString(6, room.getImagem());

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
	
	public static boolean deleteRoom(int nifClub, int roomId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Get a database connection
            connection = DBConnectionManager.getConnection();

            // Define the SQL query for deleting a room
            String query = "DELETE FROM sala WHERE NifClube = ? AND Id = ?";

            // Create a prepared statement
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, nifClub);
            preparedStatement.setInt(2, roomId);

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
	
	public static Room getRoom(int nifClube, int roomId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Get a database connection
            connection = DBConnectionManager.getConnection();

            // Define the SQL query for retrieving room information
            String query = "SELECT * FROM sala WHERE NifClube = ? AND Id = ?";
            
            // Create a prepared statement
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, nifClube);
            preparedStatement.setInt(2, roomId);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // Check if any result is returned
            if (resultSet.next()) {
                // Extract room information from the result set
                Room room = new Room();
                room.setId(resultSet.getInt("Id"));
                room.setNome(resultSet.getString("Nome"));
                room.setOcupacaoMaxima(resultSet.getInt("OcupacaoMaxima"));
                room.setEstado(resultSet.getInt("Estado"));

                // Add more fields as needed

                return room;
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
	
	public static boolean updateRoom(int NifClube, int roomId, String roomName, int maxOccupancy, int roomStatus) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Get a database connection
            connection = DBConnectionManager.getConnection();

            // Define the SQL query for updating room information
            String query = "UPDATE sala SET Nome = ?, OcupacaoMaxima = ?, Estado = ? WHERE (Id = ? AND NifClube = ?) ";
            
            // Create a prepared statement
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, roomName);
            preparedStatement.setInt(2, maxOccupancy);
            preparedStatement.setInt(3, roomStatus);
            preparedStatement.setInt(4, roomId);
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

