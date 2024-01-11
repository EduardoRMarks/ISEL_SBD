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
<%@page import= "java.util.Calendar" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Atividades coletivas</title>
</head>
<body>

<%
	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;
	String nomePt = null;
	
	Cliente user = (Cliente) session.getAttribute("user");
	int userNif = user.getNIF();
	String userAge = user.getAge();
		
	try{
		connection = DBConnectionManager.getConnection();

		%>
			<h2>Atividades coletivas atuais</h2>
		<%
	    
	    String query_pontuais = "SELECT * FROM sbd_tp1_43498_45977_47739.atividade WHERE MinParticipantes > 1 AND Tipo = 'pontual';";
	    statement = connection.prepareStatement(query_pontuais);
	    resultSet = statement.executeQuery();

	    %>
		<h4>Atividades pontuais</h4>
		<%
	    
	    while (resultSet.next()){
	    	String idAtividade = resultSet.getString("Id");
	    	String idPt = resultSet.getString("IdPt");
	    	
	    	Date dataAtividade = resultSet.getDate("Data");
	    	Calendar calendar = Calendar.getInstance();
	        calendar.setTime(dataAtividade);
	        
	        String idadeMenor = "0";
	        String idadeMaior = "200";
	        
	        String escalaoEtario = resultSet.getString("EscalaoEtario");
	        if (escalaoEtario.contains("-")) {
	        	String[] parts = null;
	        	parts = escalaoEtario.split("-");
	        	idadeMenor = parts[0];
	        	idadeMaior = parts[1];
	        }
	        else{
	        	int plusIndex = escalaoEtario.indexOf("+");
	        	idadeMenor = escalaoEtario.substring(0, plusIndex);
	        }
	    	
	    	String estadoAtividade = resultSet.getString("Estado");
	    	
	    	boolean inscrito = ClientUtil.inscrito(connection ,idAtividade, userNif);
	    	
	    	%>
	    	<div>
	    		<tr>
                <td><%=ClientUtil.getDayOfWeekString(calendar.DAY_OF_WEEK)%>, dia <%=dataAtividade.getDate() %>/<%=dataAtividade.getMonth()+1 %> </td>
                <td><%=estadoAtividade %></td>
	    	<%
	    	if(Integer.parseInt(userAge) < Integer.parseInt(idadeMenor) || Integer.parseInt(userAge) > Integer.parseInt(idadeMaior)){
	    		%>
	    		<td>
	    			<button disabled>Inscrever</button>
	    		</td>
	    		<td>A sua idade não consta no escalão etário <%=escalaoEtario %></td>
	    		<%
	    	}
	    	else{
	    		switch (estadoAtividade) {
		        case "InscricoesAbertas":        	
		        	if(inscrito){
		        	%>
	                <td>
	                	<button onclick="inscricao('desinscrever_coletivas', '<%=idAtividade%>', '<%=userNif%>', '<%=idPt%>')">Cancelar</button>                    
	                </td>
	            	<%
		        	}
		        	else{
		        		%>
		                <td>
		                    <button onclick="inscricao('inscrever_coletivas', '<%=idAtividade%>', '<%=userNif%>', '<%=idPt%>')">Inscrever</button>
		                </td>
		            	<%
		        	}      	
		            break;

		        case "InscricoesFechadas":
		        	if(inscrito){
		        		%>
		                <td>
		                    <button onclick="inscricao('desinscrever_coletivas', '<%=idAtividade%>', '<%=userNif%>', '<%=idPt%>')">Cancelar</button>
		                </td>
		            	<%
		        	}
		        	else{
		        		%>
		                <td>
		                    <button disabled>Inscrever</button>
		                </td>
		            	<%
		        	}      	
		            break;

		        case "Cancelado":
		        	%>
	                <td>
	                    <button disabled>Inscrever</button>
	                </td>
	            	<%
		            break;
		            
		        default:
		        	%>
		        	<h3>Não existem atividades com <%=nomePt %></h3>
		        	<%
		        	break;
		    	}   	
	    	}    	
	    	%>
				</tr>
			</div>
			<br>
	    	<%
	    }
	    
		String query_semanais = "SELECT * FROM sbd_tp1_43498_45977_47739.atividade WHERE MinParticipantes > 1 AND Tipo = 'semanal';";
	    statement = connection.prepareStatement(query_semanais);
	    resultSet = statement.executeQuery();

	    %>
		<h4>Atividades semanais</h4>
		<%
	    
	    while (resultSet.next()){
	    	String idAtividade = resultSet.getString("Id");
	    	String idPt = resultSet.getString("IdPt");
	    	
	    	String diaDaSemana = resultSet.getString("DiaDeSemana");
	    	
	    	String idadeMenor = "0";
	        String idadeMaior = "200";
	        
	        String escalaoEtario = resultSet.getString("EscalaoEtario");
	        if (escalaoEtario.contains("-")) {
	        	String[] parts = null;
	        	parts = escalaoEtario.split("-");
	        	idadeMenor = parts[0];
	        	idadeMaior = parts[1];
	        }
	        else{
	        	int plusIndex = escalaoEtario.indexOf("+");
	        	idadeMenor = escalaoEtario.substring(0, plusIndex);
	        }
	    	
	    	String estadoAtividade = resultSet.getString("Estado");
	    	
	    	boolean inscrito = ClientUtil.inscrito(connection ,idAtividade, userNif);
	    	
	    	%>
	    	<div>
	    		<tr>
                <td><%=diaDaSemana%>s, </td>
                <td><%=estadoAtividade %></td>
	    	<%
	    	if(Integer.parseInt(userAge) < Integer.parseInt(idadeMenor) || Integer.parseInt(userAge) > Integer.parseInt(idadeMaior)){
	    		%>
	    		<td>
	    			<button disabled>Inscrever</button>
	    		</td>
	    		<td>A sua idade não consta no escalão etário <%=escalaoEtario %></td>
	    		<%
	    	}
	    	else{
	    		switch (estadoAtividade) {
		        case "InscricoesAbertas":  
		        	if(inscrito){
		        	%>
	                <td>
	                	<button onclick="inscricao('desinscrever_coletivas', '<%=idAtividade%>', '<%=userNif%>', '<%=idPt%>')">Cancelar</button>                    
	                </td>
	            	<%
		        	}
		        	else{
		        		%>
		                <td>
		                    <button onclick="inscricao('inscrever_coletivas', '<%=idAtividade%>', '<%=userNif%>', '<%=idPt%>')">Inscrever</button>
		                </td>
		            	<%
		        	}      	
			    	break;

		        case "InscricoesFechadas":
		        	if(inscrito){
		        		%>
		                <td>
		                    <button onclick="inscricao('desinscrever_coletivas', '<%=idAtividade%>', '<%=userNif%>', '<%=idPt%>')">Cancelar</button>
		                </td>
		            	<%
		        	}
		        	else{
		        		%>
		                <td>
		                    <button disabled>Inscrever</button>
		                </td>
		            	<%
		        	}      	
		            break;

		        case "Cancelado":
		        	%>
	                <td>
	                    <button disabled>Inscrever</button>
	                </td>
	            	<%
		            break;
		            
		        default:
		        	%>
		        	<h3>Não existem atividades com <%=nomePt %></h3>
		        	<%
		        	break;
		    	}   	
	    	}    	
	    	%>
				</tr>
			</div>
			<br>
	    	<%
	    }
	    
	} catch (Exception e) {
        e.printStackTrace();
    } finally {
        DBConnectionManager.close(resultSet, statement, connection);
    }
	
	

%>

<button onclick="goBack('cliente.jsp')">Voltar</button>

<script>

function inscricao(action, idAtividade, userNif, idPt) {

	var encodedTarget = encodeURIComponent(action);
	console.log(encodedTarget)
	if(action === 'inscrever_coletivas'){
		window.location.href = "inscrever_cancelar.jsp?idAtividade="+ encodeURIComponent(idAtividade) + "&userNif=" + encodeURIComponent(userNif) + 
				"&idPt=" + encodeURIComponent(idPt) + "&tipoAtividade=grupo&inscrever_cancelar=inscrever";
	}
	if(action === "desinscrever_coletivas"){        
       	window.location.href = "inscrever_cancelar.jsp?idAtividade="+ encodeURIComponent(idAtividade) + "&userNif=" + encodeURIComponent(userNif) + 
				"&idPt=" + encodeURIComponent(idPt) + "&tipoAtividade=grupo&inscrever_cancelar=cancelar";
	}
}

function goBack(escolha) {
	window.location.href = encodeURIComponent(escolha);
}

</script>

</body>
</html>


