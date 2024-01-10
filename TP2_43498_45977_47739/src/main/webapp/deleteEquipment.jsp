<%@ page import="util.EquipmentUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.*,java.util.*,java.sql.*" %>

<%
    String selectedClub = (String)session.getAttribute("selectedClub");
	int nifClub = (int)session.getAttribute("nifClub");
    String roomIdParam = request.getParameter("equipmentId");
    
    if (selectedClub != null && !selectedClub.isEmpty() && roomIdParam != null && !roomIdParam.isEmpty()) {
        int roomId = Integer.parseInt(roomIdParam);

        // Delete the room from the database
        boolean success = EquipmentUtil.deleteEquipment(nifClub, roomId);

        if (success) {
%>
            <p>Room deleted successfully!</p>
<%
        } else {
%>
            <p>Error deleting room. Please try again.</p>
<%
        }
    } else {
%>
        <p>Invalid parameters. Please provide a valid club and room ID.</p>
<%
    }
%>

<a href="configureEquipmentAndRooms.jsp">Back to Configuration</a>
