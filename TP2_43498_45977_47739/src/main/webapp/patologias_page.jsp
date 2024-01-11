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
<title>Patologias</title>

	<style>
        .container {
            display: flex;
            align-items: center;
        }
        h3 {
            margin-right: 10px;
        }
    </style>
</head>
<body>

<%
	Connection connection = null;
	PreparedStatement statement = null;
	List<String> listaPatologias = new ArrayList<String>();
	Cliente user = null;
	int userNif = 0; 
	
	user = (Cliente) session.getAttribute("user");
	userNif = user.getNIF();
	
	try {
        connection = DBConnectionManager.getConnection();

        String query = "SELECT * FROM sbd_tp1_43498_45977_47739.patologia WHERE NifCliente = '" + userNif + "'";
        
        listaPatologias = ClientUtil.getPatologiasOrObjetivos(connection, query);
        
        if(listaPatologias.size() == 0){
        	%><h2>Atualmente não tem nenhuma patologia adicionada.</h2><%
        }
        else{
        	for(String p: listaPatologias) {
            	%>

    			<div class="container">
    			    <h3><%= p %></h3>
    			    <button type="button" onclick="redirectToPage('eliminar', '<%= p %>')">Eliminar</button>
    			</div>
            	<%
    		}
        }        
	} catch (Exception e) {
        e.printStackTrace();
    } finally {
        DBConnectionManager.close(null, statement, connection);
    }
%>

	<div class="container">
	    <label for="inputPatologia">Adicionar patologia:</label>
		<input type="text" id="inputPatologia">
	    <button type="button" onclick="redirectToPage('adicionar', '')">Adicionar</button>
	</div>

	<button onclick="goBack('cliente.jsp')">Voltar</button>
	
	<script>
		//vai adicionar ou eliminar, dependendo da ação
	    function redirectToPage(action, patologia) {

	    	var encodedTarget = encodeURIComponent(action);
	    	if(action === 'eliminar'){
	    		window.location.href = "patologia_objetivos_actions.jsp?which_table="+ encodeURIComponent("patologia") + "&action=" + encodeURIComponent(action) + 
	    				"&patologia=" + encodeURIComponent(patologia) + "&nif=" + encodeURIComponent(<%= userNif %>);
	    	}
	    	if(action === "adicionar"){
	    		var inputElement = document.getElementById("inputPatologia");
	            var inputPatologia = inputElement.value;
	            
	            if(inputPatologia.length > 0){
	            	window.location.href = "patologia_objetivos_actions.jsp?which_table="+ encodeURIComponent("patologia") + "&action=" + encodeURIComponent(action) + 
    				"&patologia=" + encodeURIComponent(inputPatologia) + "&nif=" + encodeURIComponent(<%= userNif %>);
	            }
	    	}
	    }
		
	    function goBack(escolha) {
	    	window.location.href = encodeURIComponent(escolha);
	    }
    </script>

</body>
</html>


