<%@ page import="util.EquipmentUtil" %>
<%@ page import="model.Equipment" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update Equipment</title>
    <!-- Include necessary styles/scripts -->
</head>
<body>

<h1>Update Equipment</h1>

<%
    String selectedClub = (String)session.getAttribute("selectedClub");
	int nifClub = (int)session.getAttribute("nifClub");

    String equipmentIdParam = request.getParameter("equipmentId");
    int equipmentId = (equipmentIdParam != null && !equipmentIdParam.isEmpty()) ? Integer.parseInt(equipmentIdParam) : -1;

    if (selectedClub != null && !selectedClub.isEmpty() && equipmentId != -1) {
        if ("POST".equalsIgnoreCase(request.getMethod())) {

            String equipmentName = request.getParameter("equipmentName");
            String equipmentDemo = request.getParameter("equipmentDemo");
            int equipmentStatus = Integer.parseInt(request.getParameter("equipmentStatus"));

            boolean success = EquipmentUtil.updateEquipment(nifClub, equipmentId, equipmentName, equipmentDemo, equipmentStatus);

            if (success) {
%>
                <p>Equipment updated successfully!</p>
<%
            } else {
%>
                <p>Error updating Equipment. Please try again.</p>
<%
            }
        } else {

            Equipment equipment = EquipmentUtil.getEquipment(nifClub, equipmentId);

            if (equipment != null) {
%>

                <form method="post" action="updateEquipment.jsp">
                    <input type="hidden" name="selectedClub" value="<%= selectedClub %>"/>
                    <input type="hidden" name="equipmentId" value="<%= equipment.getId() %>"/>

                    <label for="equipmentName">Equipment Name:</label>
                    <input type="text" name="equipmentName" value="<%= equipment.getNome() %>" required><br>

                    <label for="equipmentDemo">Equipment Demo URL:</label>
                    <input type="text" name="equipmentDemo" value="<%= equipment.getDemonstracao() %>" required><br>

                    <label for="equipmentStatus">Equipment Status (0 or 1):</label>
                    <input type="number" name="equipmentStatus" min="0" max="1" value="<%= equipment.getEstado() %>" required><br>

                    <input type="submit" value="Update Equipment">
                </form>

<%
            } else {
%>
                <p>Equipment</p>
<%
            }
        }
    } else {
%>
    <p>Invalid parameters. Please provide a valid club and Equipment ID.</p>
<%
    }
%>

<a href="configureEquipmentAndRooms.jsp">Back to Configuration</a>

</body>
</html>
