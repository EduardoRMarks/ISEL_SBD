<%@page import="java.sql.Date"%>
<%@page import="java.io.Console"%>
<%@page import="model.User" %>
<%@page import="model.UserRole" %>
<%@page import="util.DBConnectionManager"%>
<%@page import="util.ClientUtil"%>
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
    <title>Client Page</title>
    <style>
        .form-container {
            display: none;
        }
    </style>
</head>
<body>

<%
	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;
	User user = null;
	
	String userNIF = null;
	String userName = null;
	Date userBirthDate = null;
	String userSelfPhone = null;
	
	String userEmail = (String) session.getAttribute("userEmail");
	//System.out.println(userName);
	
	try {
        connection = DBConnectionManager.getConnection();

        String query = "SELECT * FROM sbd_tp1_43498_45977_47739.cliente WHERE Email = '" + userEmail + "'";
        
        resultSet = ClientUtil.getClientInfo(connection, query);
        
        userNIF = String.valueOf(resultSet.getInt("NIF"));
        userName = resultSet.getString("Nome");
        userBirthDate = resultSet.getDate("DataDeNascimento");
        userSelfPhone = resultSet.getString("Telemovel");
        
        user = new User(resultSet.getInt("NIF"), userEmail, UserRole.CLIENT);
        session.setAttribute("user", user);
        String client = "client" + userNIF;
        session.setAttribute("client", client);
        
        
	} catch (Exception e) {
        e.printStackTrace();
    } finally {
        DBConnectionManager.close(resultSet, statement, connection);
    }
%>

<div>
	<h1><%= user.toString() %></h1>
	<h3>Bem vindo <%= userName %></h3>
	<button onclick="redirectToPage('update_cliente_info.jsp')">Update Info</button>
	<button onclick="redirectToPage('patologias_page.jsp')">Patologias</button>
	<button onclick="redirectToPage('objetivos_page.jsp')">Objetivos</button>
    


    <script>
	    function redirectToPage(escolha) {
	    	var encodedTarget = encodeURIComponent(escolha);
	    	window.location.href = encodedTarget;
	    }
    </script>
</div>

</body>
</html>