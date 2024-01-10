<%@page import="java.util.ArrayList"%>
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
<title>Publicar atividade page</title>
</head>
<body>

<%
	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;
	boolean success = false;

    // Retrieve parameters from the request
    String atividadeId = request.getParameter("atividadeId");
    String idPt = request.getParameter("idPt");
    String nomeAtividade = request.getParameter("nomeAtividade");
    String estado = request.getParameter("estado");
    String duracao = request.getParameter("duracao");
    String tipo = request.getParameter("tipo");
    String horaInicio = request.getParameter("horaInicio");
    String diaDeSemana = request.getParameter("diaDeSemana");
    String data = request.getParameter("data");
    String minParticipantes = request.getParameter("minParticipantes");
    String maxParticipantes = request.getParameter("maxParticipantes");
    String confirmacao = request.getParameter("confirmacao");
    String escalaoEtario = request.getParameter("escalaoEtario");
    String nomeClube = request.getParameter("nifClube");
    
    String nifClube = null;

    boolean confirmacaoAtividade;
    
    if (data.equals("")) data = null;
    else if (diaDeSemana.equals("")) diaDeSemana = null;
    
    if (confirmacao.equals("1"))
    	confirmacaoAtividade = true;
    else {
    	confirmacaoAtividade = false;
    }
    
    try {
    	connection = DBConnectionManager.getConnection();
    	
    	String query = "SELECT Nif FROM sbd_tp1_43498_45977_47739.clube WHERE DesignacaoComercial = '" + nomeClube + "'";
    	
    	resultSet = PtUtil.getPtInfo(connection, query);
    	nifClube = String.valueOf(resultSet.getInt("Nif"));
    	System.out.println(nifClube);
    	
    	query = "INSERT INTO sbd_tp1_43498_45977_47739.atividade (`Id`, `IdPt`, " 
    			+ "`Nome`, `Estado`, `Duracao`, `DiaDeSemana`, " 
    			+ "`HoraDeInicio`, `Tipo`, `Data`, `MinParticipantes`, " 
    			+ "`MaxParticipantes`, `Confirmacao`, `EscalaoEtario`, `NifClube`)" 
    	    	+ " VALUES ('" + atividadeId + "', '" + idPt + "', '" + nomeAtividade + "', '" 
    			+ estado + "', '" + duracao + "', '" + diaDeSemana + "', '" + horaInicio + "', '" 
    	    	+ tipo + "', '" + data + "', '" + minParticipantes + "', '" + maxParticipantes + "', '"
    	    	+ confirmacao + "', '" + escalaoEtario + "', '" + nifClube + "');";
    	success = PtUtil.addAtividade(connection, atividadeId, idPt, nomeAtividade, estado, 
    			duracao, diaDeSemana, horaInicio, tipo, data, minParticipantes, maxParticipantes,
    			confirmacaoAtividade, escalaoEtario, nifClube);
		
	} 
    catch (Exception e) { e.printStackTrace(); } 
    finally { DBConnectionManager.close(null, null, connection); }
    
    if(success){
   		%>
       	<h2>Atividade publicada com sucesso</h2>
       	<%
    }
    else{
    	%>
    	<h2>Ocorreu um erro e não foi possível efetuar a operação.</h2>
    	<%
    }
    
%>

	<br>

	<button onclick="goBack('novaAtividadePage.jsp.jsp')">Voltar</button>
	
	<script>
	    function goBack(escolha) {
	    	window.location.href = encodeURIComponent(escolha);
	    }
    </script>

</body>
</html>
