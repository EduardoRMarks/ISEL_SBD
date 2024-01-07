<%@page import="java.util.ArrayList"%>
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
<%@ page import="java.text.SimpleDateFormat" %>
<%@page import= "java.util.ArrayList"%>
<%@page import= "java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Objetivos</title>
</head>
<body>

<%
	Connection connection = null;
	PreparedStatement statement = null;
	List<String> listaPatologias = new ArrayList<String>();
	User user = null;
	int userNif = 0; 
	
	user = (User) session.getAttribute("user");
	userNif = user.getNIF();
	
	try {
        connection = DBConnectionManager.getConnection();

        String query = "SELECT * FROM sbd_tp1_43498_45977_47739.objetivo WHERE NifCliente = '" + userNif + "'";
        
        listaPatologias = ClientUtil.getPatologiasOrObjetivos(connection, query);
        
        for(String p: listaPatologias) {
        	%> 
        	<h1><%= p %></h1>
        	<%
			System.out.println(p);
		}
        
	} catch (Exception e) {
        e.printStackTrace();
    } finally {
        DBConnectionManager.close(null, statement, connection);
    }
%>

	<button onclick="redirectToPage('cliente.jsp')">Voltar</button>
	
	<script>
	    function redirectToPage(escolha) {
	    	window.location.href = encodeURIComponent(escolha);
	    }
    </script>

</body>
</html>


