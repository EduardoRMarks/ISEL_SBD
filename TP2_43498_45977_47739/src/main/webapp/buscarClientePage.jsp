<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.Date"%>
<%@page import="java.io.Console"%>
<%@page import="model.Pt" %>
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
<title>Buscar Info Cliente</title>
</head>
<body>

<%
	Connection connection = null;
	PreparedStatement statement = null;
	List<String> listaClientes = new ArrayList<String>();
	String listaClientesJSON = null;
	StringBuilder jsArray = null;
	
	try {
        connection = DBConnectionManager.getConnection();

        String query = "SELECT Nome FROM sbd_tp1_43498_45977_47739.cliente";
        
        listaClientes = PtUtil.getClientes(connection, query);
        
        jsArray = new StringBuilder();
        jsArray.append("[");
        for (String cliente : listaClientes) {
            jsArray.append("\"").append(cliente).append("\",");
        }
        if (!listaClientes.isEmpty()) {
            jsArray.setLength(jsArray.length() - 1); // Remove the trailing comma
        }
        jsArray.append("]");
        
	} 
	catch (Exception e) { e.printStackTrace(); } 
	finally { DBConnectionManager.close(null, statement, connection); }
%>

<%

    
%>

<div>

    <input type="text" id="searchInput" placeholder="Search for a client">
    <div id="searchResults"></div>

    <button onclick="redirectToPage('pt.jsp')">Voltar</button>
	
	<script>
	    function redirectToPage(escolha) { window.location.href = encodeURIComponent(escolha); }
    </script>
    
	<script>
	    const listaClientes = <%= jsArray.toString() %>;
	
	    const searchInput = document.getElementById("searchInput");
	    const searchResults = document.getElementById("searchResults");
	
	    searchInput.addEventListener("input", function() {
	        const inputText = this.value.toLowerCase();
	        let matchingClients = [];
	        if (inputText.trim().length > 0) {
	            matchingClients = listaClientes.filter(client => client.toLowerCase().startsWith(inputText));
	        }
	
	        displaySearchResults(matchingClients);
	    });
	
	    function displaySearchResults(results) {
	        searchResults.innerHTML = "";
	        if (results.length > 0) {
	            results.forEach(result => {
	                const div = document.createElement("div");
	                div.textContent = result;
	                div.addEventListener("click", function() {
	                    searchInput.value = result;
	                    searchResults.style.display = "none";
	                });
	                searchResults.appendChild(div);
	            });
	            searchResults.style.display = "block";
	        } else {
	            searchResults.style.display = "none";
	        }
	    }
	
	    document.addEventListener("click", function(event) {
	        if (event.target !== searchInput) {
	            searchResults.style.display = "none";
	        }
	    });
	
	    function redirectToPage(escolha) {
	        window.location.href = encodeURIComponent(escolha);
	    }
	</script>


</div>

</body>
</html>