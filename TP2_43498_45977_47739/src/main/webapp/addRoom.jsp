<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.io.*"%>
<%@ page import="util.ClubUtil" %>
<%@ page import="model.Room" %>
<%@ page import="util.RoomUtil" %>

<!DOCTYPE html>
<html>
<head>
    <title>Add Equipment</title>
</head>
<body>

<%
    //if ("POST".equalsIgnoreCase(request.getMethod())) {
        try {
            String selectedClub = (String)session.getAttribute("selectedClub");
            String equipmentName = request.getParameter("equipmentName");
            String MaxOccupationStr = request.getParameter("MaxOcuppation");
            int MaxOccupation = (MaxOccupationStr != null && !MaxOccupationStr.isEmpty()) ? Integer.parseInt(MaxOccupationStr) : 0;
            String equipmentStatusParam = request.getParameter("equipmentStatus");
            int equipmentStatus = (equipmentStatusParam != null && !equipmentStatusParam.isEmpty()) ? Integer.parseInt(equipmentStatusParam) : 0;
			
            System.out.println(selectedClub);
            System.out.println(equipmentName);
            System.out.println(MaxOccupation);
            System.out.println(equipmentStatusParam);
            System.out.println(equipmentStatus);

            if (selectedClub != null && equipmentName != null) {
                // Get NIF based on the selected club
                int nif = ClubUtil.getClubNif(selectedClub);

                // Create Equipment object
                Room newRoom = new Room();
                newRoom.setNifClube(nif);
                newRoom.setNome(equipmentName);
                newRoom.setOcupacaoMaxima(MaxOccupation);
                newRoom.setEstado(equipmentStatus);
                System.out.println(newRoom);

                // Add equipment to the database
                boolean success = RoomUtil.addRoom(newRoom);

                if (success) {
%>
                    <p>Equipment added successfully!</p>
<%
                } else {
%>
                    <p>Error adding equipment. Please try again.</p>
<%
                }
            } else {
%>
                <p>Invalid parameters. Please fill in all required fields.</p>
<%
            }
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            throw new ServletException(e);
        }
    //}
%>

<form action="addRoom.jsp" method="get" enctype="multipart/form-data">

    <label for="equipmentName">Room Name:</label>
    <input type="text" name="equipmentName" required><br>

    <label for="MaxOcuppation">Max Occupation:</label>
    <input type="number" name="MaxOcuppation" required><br>

    <label for="equipmentStatus">Equipment Status (0 or 1):</label>
    <input type="number" name="equipmentStatus" min="0" max="1" required><br>


    <input type="submit" value="Submit">
</form>

<a href="configureEquipmentAndRooms.jsp">Back to Configuration</a>

</body>
</html>
