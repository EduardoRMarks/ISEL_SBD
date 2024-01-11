<%@page import="java.sql.Date"%>
<%@page import="java.io.Console"%>
<%@page import="model.Pt" %>
<%@page import="model.UserRole" %>
<%@page import="util.DBConnectionManager"%>
<%@page import="util.PtUtil"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@ page import="java.sql.PreparedStatement" %>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>PT Page</title>
</head>
<body>

<%
	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;
	
	Pt user = null;
	String idPT = null;
	String ptName = null;
	String userSelfPhone = null;
	
	String userEmail = (String) session.getAttribute("userEmail");
	String userPhoto = (String) session.getAttribute("userPhoto");

	
	try {
        connection = DBConnectionManager.getConnection();

        String query = "SELECT * FROM sbd_tp1_43498_45977_47739.pt WHERE Email = '" + userEmail + "'";
        
        resultSet = PtUtil.getPtInfo(connection, query);
        
        idPT = resultSet.getString("Id");
        ptName = resultSet.getString("Nome");
        userSelfPhone = resultSet.getString("Telemovel");
        
        user = new Pt(resultSet.getInt("ID"), userEmail, UserRole.PERSONAL_TRAINER);
        session.setAttribute("user", user);
        request.getSession().setAttribute("nomePt", ptName);
        request.getSession().setAttribute("idPT", idPT);
       
	} 
	catch (Exception e) { e.printStackTrace(); } 
	finally { DBConnectionManager.close(resultSet, statement, connection); }
	
%>

<div>
    <h1><%= user.toString() %></h1>
    <h3>Bem vindo, <%= ptName %></h3>

    <button onclick="redirectToPage('manchasDeDisponibilidade.jsp')" style="display: block; margin-bottom: 10px;">Manchas de disponibilidade</button>
    
    <button onclick="redirectToPage('buscarClientePage.jsp')" style="display: block; margin-bottom: 10px;">Procurar Cliente</button>
    
    <button onclick="redirectToPage('recomendarClientesPage.jsp')" style="display: block; margin-bottom: 10px;">Recomendar Clientes</button>
    
    <button onclick="logout()" style="display: block; margin-bottom: 10px;">Log out</button>

    <script>
        function redirectToPage(escolha) {
            window.location.href = encodeURIComponent(escolha);
        }
        
        function logout() {
          window.location.href = "index.jsp";
        }

    </script>
    
</div>

</body>
</html>