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
<title>Atividades individuais</title>
</head>
<body>

<%
	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;
	String nomePt = null;
	
	String idPt = request.getParameter("idPt");
	
	Cliente user = (Cliente) session.getAttribute("user");
	int userNif = user.getNIF();
	
	
	
	try{
		connection = DBConnectionManager.getConnection();
		
		String query_pt_name = "SELECT * FROM sbd_tp1_43498_45977_47739.pt WHERE Id = '" + idPt + "';";
	    statement = connection.prepareStatement(query_pt_name);

	    resultSet = statement.executeQuery();	    
	    if (resultSet.next()){
	    	nomePt = resultSet.getString("Nome");
		%>
			<h2>Atividades individuais com <%=nomePt %></h2>
		<%
	    }
	    
	    String query = "SELECT * FROM sbd_tp1_43498_45977_47739.atividade WHERE Tipo = 'Pontual' AND IdPt = '"+ idPt +"' AND MaxParticipantes = '1';";
	    statement = connection.prepareStatement(query);

	    resultSet = statement.executeQuery();
	    while (resultSet.next()){
	    	String idAtividade = resultSet.getString("Id");
	    	
	    	Date dataAtividade = resultSet.getDate("Data");
	    	Calendar calendar = Calendar.getInstance();
	        calendar.setTime(dataAtividade);
	    	
	    	String estadoAtividade = resultSet.getString("Estado");
	    	
	    	boolean inscrito = ClientUtil.inscrito(connection ,idAtividade, userNif);
	    	
	    	%>
	    	<div>
	    		<tr>
                <td><%=ClientUtil.getDayOfWeekString(calendar.DAY_OF_WEEK)%>, dia <%=dataAtividade.getDate() %>/<%=dataAtividade.getMonth() %> </td>
                <td><%=estadoAtividade %></td>
	    	<%
	    	
	    	switch (estadoAtividade) {
	        case "InscricoesAbertas":        	
	        	%>
                <td>
                    <button onclick="inscricao('inscrever_individual', '<%=idAtividade%>', '<%=userNif%>', '<%=idPt%>')">Inscrever</button>
                </td>
            	<%
	            break;

	        case "InscricoesFechadas":
	        	if(inscrito){
	        		%>
	                <td>
	                    <button onclick="inscricao('desinscrever_individual', '<%=idAtividade%>', '<%=userNif%>', '<%=idPt%>')">Cancelar</button>
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
	if(action === 'inscrever_individual'){
		window.location.href = "inscrever_cancelar.jsp?idAtividade="+ encodeURIComponent(idAtividade) + "&userNif=" + encodeURIComponent(userNif) + 
				"&idPt=" + encodeURIComponent(idPt) + "&tipoAtividade=individuais&inscrever_cancelar=inscrever";
	}
	if(action === "desinscrever_individual"){        
       	window.location.href = "inscrever_cancelar.jsp?idAtividade="+ encodeURIComponent(idAtividade) + "&userNif=" + encodeURIComponent(userNif) + 
				"&idPt=" + encodeURIComponent(idPt) + "&tipoAtividade=individuais&inscrever_cancelar=cancelar";
	}
}

function goBack(escolha) {
	window.location.href = encodeURIComponent(escolha);
}

</script>

</body>
</html>


