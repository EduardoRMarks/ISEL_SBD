<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.io.*"%>
<%@ page import="util.ClubUtil" %>
<%@ page import="model.Equipment" %>
<%@ page import="util.EquipmentUtil" %>

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
            String equipmentDemo = request.getParameter("equipmentDemo");
            String equipmentStatusParam = request.getParameter("equipmentStatus");
            int equipmentStatus = (equipmentStatusParam != null && !equipmentStatusParam.isEmpty()) ? Integer.parseInt(equipmentStatusParam) : 0;
			
            System.out.println(selectedClub);
            System.out.println(equipmentName);
            System.out.println(equipmentDemo);
            System.out.println(equipmentStatusParam);
            System.out.println(equipmentStatus);

            if (selectedClub != null && equipmentName != null && equipmentDemo != null) {
                // Get NIF based on the selected club
                int nif = ClubUtil.getClubNif(selectedClub);

                // Create Equipment object
                Equipment newEquipment = new Equipment();
                newEquipment.setNifClube(nif);
                newEquipment.setNome(equipmentName);
                newEquipment.setDemonstracao(equipmentDemo);
                newEquipment.setEstado(equipmentStatus);
                System.out.println(newEquipment);

                // Add equipment to the database
                boolean success = EquipmentUtil.addEquipment(newEquipment);

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

<!-- HTML form for user inputs -->
<form action="addEquipment.jsp" method="get" enctype="multipart/form-data">

    <label for="equipmentName">Equipment Name:</label>
    <input type="text" name="equipmentName" required><br>

    <label for="equipmentDemo">Equipment Demo URL:</label>
    <input type="text" name="equipmentDemo" required><br>

    <label for="equipmentStatus">Equipment Status (0 or 1):</label>
    <input type="number" name="equipmentStatus" min="0" max="1" required><br>

    <!-- Remove the file input for image -->

    <input type="submit" value="Submit">
</form>

<!-- Include a link to go back to the main configuration page -->
<a href="configureEquipmentAndRooms.jsp">Back to Configuration</a>

</body>
</html>
