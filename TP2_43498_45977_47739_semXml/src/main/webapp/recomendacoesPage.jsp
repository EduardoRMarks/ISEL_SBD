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
<title>Recomendacoes page</title>
</head>
<body>

<%
	Connection connection = null;
	boolean success = false;
	String recomendacao = null;

    // Retrieve parameters from the request
    String idPt = request.getParameter("idPt");
    String nifCliente = request.getParameter("nifCliente");
    String idEquipamento = request.getParameter("idEquipamento");
    String nifClube = request.getParameter("nifClube");
    String data = request.getParameter("data");
    String uso = request.getParameter("uso");
    String action = request.getParameter("action");
    
    boolean usoSQL;
    
    System.out.println("\nid: " +idPt);
    System.out.println("nifCliente: " +nifCliente);
    System.out.println("idEquip: " +idEquipamento);
    System.out.println("nifClube: " +nifClube);
    System.out.println("data: " + data);
    System.out.println("Uso: " + uso +"testeasdas");
    System.out.println("action" + action);
    
    if (uso.equals("1"))
    	usoSQL = true;
    else {
    	usoSQL = false;
    }
    
    System.out.println("USO SQL" + usoSQL);
    
    try {
    	connection = DBConnectionManager.getConnection();

    	String query = "INSERT INTO sbd_tp1_43498_45977_47739.pt_cliente_equipamento (`IdPt`, `NifCliente`, `IdEquipamento`, `NifClube`, `Data`, `Uso`)" 
    	+ " VALUES ('" + idPt + "', '" + nifCliente + "', '" + idEquipamento + "', '" + nifClube + "', '" + data + "', '" + usoSQL + "');";
        
        System.out.println("ENTREI");
        success = PtUtil.addRecomendacao(connection, idPt, nifCliente, idEquipamento, nifClube, data, usoSQL);
        System.out.println("SAI");
 
	} 
    catch (Exception e) { e.printStackTrace(); } 
    finally { DBConnectionManager.close(null, null, connection); }
    
    if(success){
    	if(action.equals("eliminar")){
    		%>
        	<h2>Recomendacao eliminada com sucesso</h2>
        	<%
    	}
    	else{
    		%>
        	<h2>Recomendacao adicionada com sucesso</h2>
        	<%
    	}
    }
    else{
    	%>
    	<h2>Ocorreu um erro e não foi possível efetuar a operação.</h2>
    	<%
    }
    
%>

	<br>

	<button onclick="goBack('recomendarClientesPage.jsp')">Voltar</button>
	
	<script>
	    function goBack(escolha) {
	    	window.location.href = encodeURIComponent(escolha);
	    }
    </script>

</body>
</html>

