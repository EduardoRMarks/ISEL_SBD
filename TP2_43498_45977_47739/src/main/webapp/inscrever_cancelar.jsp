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
<title>inscrever_cancelar</title>
</head>
<body>

<%
	Connection connection = null;
	boolean success = false;
	String patologia_objetivo = null;

    String idAtividade = request.getParameter("idAtividade");
    String userNif = request.getParameter("userNif");
    String idPt = request.getParameter("idPt");
    String tipoAtividade = request.getParameter("tipoAtividade");
    String inscrever_cancelar = request.getParameter("inscrever_cancelar");
    
    try {
    	connection = DBConnectionManager.getConnection();
    	
    	if (tipoAtividade.equals("individuais")){
    		if(inscrever_cancelar.equals("inscrever")){
    			success = ClientUtil.inscrever(connection, tipoAtividade, idAtividade, userNif, idPt);
    		}
    		else{
    			success = ClientUtil.cancelar(connection, tipoAtividade, idAtividade, userNif, idPt);
    		}	
    	}
    	else{
    		if(inscrever_cancelar.equals("inscrever")){
    			success = ClientUtil.inscrever(connection, tipoAtividade, idAtividade, userNif, idPt);
    		}
    		else{
    			success = ClientUtil.cancelar(connection, tipoAtividade, idAtividade, userNif, idPt);
    		}
    	}        
	} catch (Exception e) {
        e.printStackTrace();
    } finally {
        DBConnectionManager.close(null, null, connection);
    }
    
%>
	
	<%
	
	if(success){
		if(inscrever_cancelar.equals("inscrever")){
			%>
			<h2>Inscrição efetuada com sucesso</h2>
			<%
		}
		else{
			%>
			<h2>Cancelamento efetuado com sucesso</h2>
			<%
		}		
	}
	else{
		if(inscrever_cancelar.equals("inscrever")){
			%>
			<h2>Inscrição não foi efetuada com sucesso</h2>
			<%
		}
		else{
			%>
			<h2>Cancelamento não foi efetuado com sucesso</h2>
			<%
		}
	}
	
	%>

	<button onclick="goBack('<%=tipoAtividade %>')">Voltar</button>
	
	<script>
	    function goBack(atividade) {
	    	var encodedTarget = encodeURIComponent(atividade);
	    	if(atividade === "individuais"){
	    		window.location.href = "atividades_individuais.jsp?idPt=" + encodeURIComponent(<%=idPt%>);
	    	}
	    	else{
	    		window.location.href = "atividades_coletivas.jsp";
	    	}
	    	
	    }
    </script>

</body>
</html>

