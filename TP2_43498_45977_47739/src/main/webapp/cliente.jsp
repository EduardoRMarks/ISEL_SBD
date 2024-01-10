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
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.Period" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Client Page</title>
    <style>
        #myDiv {
            display: none; /* Initially hidden */
        }
    </style>
</head>
<body>

<%
	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;
	Cliente cliente = null;
	
	String userNIF = null;
	String userName = null;
	Date userBirthDate = null;
	String userSelfPhone = null;
	
	int numRecomendacoes = 0;
	String idPt = null;
	
	String userEmail = (String) session.getAttribute("userEmail");
	//String userEmail = "eduardo@aol.com";
	
	try {
        connection = DBConnectionManager.getConnection();

        String query_info = "SELECT * FROM sbd_tp1_43498_45977_47739.cliente WHERE Email = '" + userEmail + "'";
        
        resultSet = ClientUtil.getClientInfo(connection, query_info);
        
        userNIF = String.valueOf(resultSet.getInt("NIF"));
        userName = resultSet.getString("Nome");
        userBirthDate = resultSet.getDate("DataDeNascimento");
        userSelfPhone = resultSet.getString("Telemovel");      
        
        cliente = new Cliente(resultSet.getInt("NIF"), userEmail, UserRole.CLIENT);
        session.setAttribute("user", cliente);
        String client = "client" + userNIF;
        session.setAttribute("client", client);
        
        String query = "SELECT COUNT(*) AS num_recomendacoes FROM sbd_tp1_43498_45977_47739.pt_cliente_equipamento WHERE NifCliente='" + userNIF + "'";
    	
		statement = connection.prepareStatement(query);
		ResultSet countResultSet = statement.executeQuery();
		
        if (countResultSet.next()) {
        	numRecomendacoes = countResultSet.getInt("num_recomendacoes");
        }
        
        if (numRecomendacoes > 0){
        	String query_qual_pt = "SELECT IdPt FROM sbd_tp1_43498_45977_47739.pt_cliente_equipamento WHERE NifCliente = '" + userNIF + "';";
            
            resultSet = ClientUtil.getClientInfo(connection, query_qual_pt);
            idPt = resultSet.getString("IdPt");
        }
        
        LocalDate birthDate = LocalDate.parse(String.valueOf(userBirthDate));

        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Calculate the period between the birth date and the current date
        Period period = Period.between(birthDate, currentDate);
        cliente.setAge(String.valueOf(period.getYears()));
        
	} catch (Exception e) {
        e.printStackTrace();
    } finally {
        DBConnectionManager.close(resultSet, statement, connection);
    }
%>
	<button onclick="Logout()">LogOut</button>
	<div>
	
		<h1><%= cliente.toString() %></h1>
		<h3>Bem vindo, <%= userName %></h3>
		<button onclick="redirectToPage('update_cliente_info')">Update Info</button>
		<button onclick="redirectToPage('patologias_page')">Patologias</button>
		<button onclick="redirectToPage('objetivos_page')">Objetivos</button>    
	       
	</div>
	
	<div>
		<h3>Atividades no ginásio</h3>
		<%
			if (numRecomendacoes > 0){
				%><button onclick="redirectToPage('atividades_individuais')">Atividades individuais do seu Pt</button><%
			}
			else {
				%><button disabled>Atividades individuais do seu Pt</button><%
			}
		%>
		
		<button onclick="redirectToPage('atividades_coletivas')">Atividades de grupo</button>
	</div>
	
	<br>
	
	<%
		if (numRecomendacoes > 0){
			%>
			<div>
			<h3>Ver recomendações do seu Pt</h3>
			<button onclick="toggleVisibility()">Ver Recomendações</button>

			<div id="myDiv" display = "none">
		    <%	    
		    try {
		        connection = DBConnectionManager.getConnection();
		        String query = "SELECT * FROM sbd_tp1_43498_45977_47739.pt_cliente_equipamento WHERE NifCliente = '" + userNIF + "';";
		        statement = connection.prepareStatement(query);
		        resultSet = statement.executeQuery();
		               
		        while (resultSet.next()){
			    	int idEquipamento = resultSet.getInt("IdEquipamento");
			    	int nifClube = resultSet.getInt("NifClube");
			    	boolean uso = resultSet.getBoolean("Uso");
			    	
			    	String query_equipamento = "SELECT * FROM sbd_tp1_43498_45977_47739.equipamento WHERE Id='" + idEquipamento + "' AND NifClube='" + nifClube + "';";
			        statement = connection.prepareStatement(query_equipamento);
			        ResultSet resultEquipamento = statement.executeQuery();
			        
			        String nomeEquimento = null;
			        String demonstracao = null;
			        
			        if (resultEquipamento.next()){
			        	nomeEquimento = resultEquipamento.getString("Nome");
			        	demonstracao = resultEquipamento.getString("Demonstracao");
			        }
			    	
			    	if (uso){
			    		%>
			    		<h3>O seu PT aconselha o uso do equipamento <%=nomeEquimento %></h3>
			    		<a href="<%=demonstracao %>" target="_blank"><%=demonstracao %></a>
			    		<%
			    	}
			    	else{
			    		%><h3>O seu PT não aconselha o uso do equipamento <%=nomeEquimento %></h3><%
			    	}
			    }
		        
		    } catch (Exception e) {
		        e.printStackTrace();
		    } finally {
		        DBConnectionManager.close(resultSet, statement, connection);
		    }
		    %>
			</div>
			</div>
			<%
			}
			else {
				%>
				<div><h3>Não tem recomandações disponíveis</h3>
				<button disabled>Ver Recomendações</button></div>
				<%
			}
			%>

	<script>
	    function redirectToPage(escolha) {
	    	var encodedTarget = encodeURIComponent(escolha);
	    	
	    	switch (escolha) {
	        case "update_cliente_info":       	
	        	window.location.href = encodedTarget + ".jsp"; 
	            break;

	        case "patologias_page":
	        	window.location.href = encodedTarget + ".jsp"; 
	            break;

	        case "objetivos_page":
	        	window.location.href = encodedTarget + ".jsp"; 
	            break;

	        case "atividades_individuais":
	        	window.location.href = encodedTarget + ".jsp?idPt="+ encodeURIComponent(<%= idPt %>); 
	            break;
	            
	        case "atividades_coletivas":
	        	window.location.href = encodedTarget + ".jsp"; 
	            break;
	    }
	    	}
	    
	    function toggleVisibility() {
	        var div = document.getElementById("myDiv");

	        if (div.style.display === "block") {
	            div.style.display = "none";
	        } else {
	            div.style.display = "block";
	        }
	    }

	    function Logout() {
	    	window.location.href = "index.jsp";
	    }
    </script>
</body>
</html>



