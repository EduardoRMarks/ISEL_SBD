<%@ page import="util.RoomUtil" %>
<%@ page import="model.Room" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update Room</title>
    <!-- Include necessary styles/scripts -->
</head>
<body>

<h1>Update Room</h1>

<%
    String selectedClub = (String)session.getAttribute("selectedClub");
	int nifClub = (int)session.getAttribute("nifClub");
    // Retrieve room id from the request parameter
    String roomIdParam = request.getParameter("roomId");
    int roomId = (roomIdParam != null && !roomIdParam.isEmpty()) ? Integer.parseInt(roomIdParam) : -1;

    if (selectedClub != null && !selectedClub.isEmpty() && roomId != -1) {
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            // Handle the form submission for updating the room
            String roomName = request.getParameter("roomName");
            int maxOccupancy = Integer.parseInt(request.getParameter("maxOccupancy"));
            int roomStatus = Integer.parseInt(request.getParameter("roomStatus"));

            // Perform the room update
            boolean success = RoomUtil.updateRoom(nifClub, roomId, roomName, maxOccupancy, roomStatus);

            if (success) {
%>
                <p>Room updated successfully!</p>
<%
            } else {
%>
                <p>Error updating room. Please try again.</p>
<%
            }
        } else {
            // Retrieve room information for displaying the form
            Room room = RoomUtil.getRoom(nifClub, roomId);

            if (room != null) {
%>

                <!-- Display room information -->
                <form method="post" action="updateRoom.jsp">
                    <input type="hidden" name="selectedClub" value="<%= selectedClub %>"/>
                    <input type="hidden" name="roomId" value="<%= room.getId() %>"/>

                    <label for="roomName">Room Name:</label>
                    <input type="text" name="roomName" value="<%= room.getNome() %>" required><br>

                    <label for="maxOccupancy">Max Occupancy:</label>
                    <input type="number" name="maxOccupancy" value="<%= room.getOcupacaoMaxima() %>" required><br>

                    <label for="roomStatus">Room Status (0 or 1):</label>
                    <input type="number" name="roomStatus" min="0" max="1" value="<%= room.getEstado() %>" required><br>

                    <!-- Add more fields as needed -->

                    <input type="submit" value="Update Room">
                </form>

<%
            } else {
%>
                <p>Room not found.</p>
<%
            }
        }
    } else {
%>
    <p>Invalid parameters. Please provide a valid club and room ID.</p>
<%
    }
%>

<!-- Back button -->
<a href="configureEquipmentAndRooms.jsp">Back to Configuration</a>

</body>
</html>
