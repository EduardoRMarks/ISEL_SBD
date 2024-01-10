<%@page import="java.sql.Date"%>
<%@page import="java.io.Console"%>
<%@page import="model.Cliente" %>
<%@page import="model.UserRole" %>
<%@page import="util.DBConnectionManager"%>
<%@page import="util.PtUtil"%>
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
	String confirmarCancelar = request.getParameter("action");
	String idAtividade = request.getParameter("activityId");

	System.out.println("XD: " + confirmarCancelar);
	
    try {
    	connection = DBConnectionManager.getConnection();
    	
    	success = PtUtil.confirmarOuCancelar(connection, confirmarCancelar, idAtividade);
    	
    	System.out.println("SUCESS: " + success);
    	
        if(success){
        	if(confirmarCancelar.equals("confirmar")){
        		%>
            	<h2>Atividade confirmada com sucesso</h2>
            	<%
        	}
        	else{
        		%>
            	<h2>Atividade cancelada com sucesso</h2>
            	<%
        	}
        }
        else{
        	%>
        	<h2>Ocorreu um erro e não foi possível efetuar a operação.</h2>
        	<%
        }
    
    }
	catch (Exception e) {
        e.printStackTrace();
    } finally {
        DBConnectionManager.close(null, null, connection);
    }
    
%>

	<button onclick="goBack('manchasDeDisponibilidade.jsp')">Voltar</button>
	
	<script>
	    function goBack(escolha) {
	    	window.location.href = encodeURIComponent(escolha);
	    }
    </script>

</body>
</html>

