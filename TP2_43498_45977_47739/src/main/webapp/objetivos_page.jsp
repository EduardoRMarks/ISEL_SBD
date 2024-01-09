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
<title>Objetivos</title>

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
List<String> listaObjetivos = new ArrayList<String>();
Cliente user = null;
int userNif = 0; 

user = (Cliente) session.getAttribute("user");
userNif = user.getNIF();
//userNif = 229495027;

try {
    connection = DBConnectionManager.getConnection();

    String query = "SELECT * FROM sbd_tp1_43498_45977_47739.objetivo WHERE NifCliente = '" + userNif + "'";
    
    listaObjetivos = ClientUtil.getPatologiasOrObjetivos(connection, query);
    
    if(listaObjetivos.size() == 0){
    	%><h2>Atualmente não tem nenhum objetivo definido.</h2><%
    }
    else{
    	for(String o: listaObjetivos) {
        	%>

			<div class="container">
			    <h3><%= o %></h3>
			    <button type="button" onclick="redirectToPage('eliminar', '<%= o %>')">Eliminar</button>
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
    <label for="inputObjetivo">Adicionar objetivo:</label>
	<input type="text" id="inputObjetivo">
    <button type="button" onclick="redirectToPage('adicionar', '')">Adicionar</button>
</div>

<button onclick="goBack('cliente.jsp')">Voltar</button>

<script>
	//vai adicionar ou eliminar, dependendo da ação
    function redirectToPage(action, objetivo) {

    	var encodedTarget = encodeURIComponent(action);
    	if(action === 'eliminar'){
    		window.location.href = "patologia_objetivos_actions.jsp?which_table="+ encodeURIComponent("objetivo") + "&action=" + encodeURIComponent(action) + 
    				"&objetivo=" + encodeURIComponent(objetivo) + "&nif=" + encodeURIComponent(<%= userNif %>);
    	}
    	if(action === "adicionar"){
    		var inputElement = document.getElementById("inputObjetivo");
            var inputObjetivo = inputElement.value;
            
            if(inputObjetivo.length > 0){
            	window.location.href = "patologia_objetivos_actions.jsp?which_table="+ encodeURIComponent("objetivo") + "&action=" + encodeURIComponent(action) + 
				"&objetivo=" + encodeURIComponent(inputObjetivo) + "&nif=" + encodeURIComponent(<%= userNif %>);
            }
    	}
    }
	
    function goBack(escolha) {
    	window.location.href = encodeURIComponent(escolha);
    }
</script>

</body>
</html>


