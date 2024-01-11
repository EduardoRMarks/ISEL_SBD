<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.Date"%>
<%@page import="java.io.Console"%>
<%@page import="model.Cliente" %>
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
<title>Insert title here</title>
</head>
<body>

<%
	Connection connection = null;
	boolean success = false;
	String patologia_objetivo = null;

    String which_table = request.getParameter("which_table");
    String action = request.getParameter("action");
    String nif = request.getParameter("nif");
    
    try {
    	connection = DBConnectionManager.getConnection();
    	if (which_table.equals("patologia")){
    		patologia_objetivo = request.getParameter("patologia");
    		success = ClientUtil.addOrDelete(connection, which_table, action, patologia_objetivo, nif);
    	}
        
        if (which_table.equals("objetivo")){
        	patologia_objetivo = request.getParameter("objetivo");
    		success = ClientUtil.addOrDelete(connection, which_table, action, patologia_objetivo, nif);
        }        
        
	} catch (Exception e) {
        e.printStackTrace();
    } finally {
        DBConnectionManager.close(null, null, connection);
    }
    
    if(success){
    	String capitalized = which_table.substring(0, 1).toUpperCase() + which_table.substring(1);
    	if(action.equals("eliminar")){
    		%>
        	<h2><%=capitalized %> "<%=patologia_objetivo %>" eliminada com sucesso</h2>
        	<%
    	}
    	else{
    		%>
        	<h2><%=capitalized %> "<%=patologia_objetivo %>" adicionada com sucesso</h2>
        	<%
    	}
    }
    else{
    	%>
    	<h2>Ocorreu um erro e não foi possível efetuar a operação.</h2>
    	<%
    }
    
%>

	<button onclick="goBack('<%=which_table %>s_page.jsp')">Voltar</button>
	
	<script>
	    function goBack(escolha) {
	    	window.location.href = encodeURIComponent(escolha);
	    }
    </script>

</body>
</html>

