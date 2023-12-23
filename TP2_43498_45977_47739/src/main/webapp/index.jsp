<%@page import="util.DBConnectionManager"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login Page</title>
</head>
<body>
<%

DBConnectionManager dbConnection =  new DBConnectionManager();

Connection connection = dbConnection.getConnection();

String QUERY = "SELECT * FROM sbd_tp1_43498_45977_47739.utilizador;";
		
//ResultSet queryResult = dbConnection.getQueryResult(QUERY, connection);


%>
<h1>Login</h1>

<h2>Fill in both fields to enable the button</h2>
    <label for="field1">Field 1:</label>
    <input type="text" id="field1" name="field1"><br>

    <label for="field2">Field 2:</label>
    <input type="text" id="field2" name="field2"><br>

    <button onclick="callJavaMethod()" id="submitButton" disabled>Call Java Method</button>

    <script>
        // Add an event listener to enable/disable the button based on field values
        document.getElementById("field1").addEventListener("input", enableDisableButton);
        document.getElementById("field2").addEventListener("input", enableDisableButton);

        function enableDisableButton() {
            var field1Value = document.getElementById("field1").value.trim();
            var field2Value = document.getElementById("field2").value.trim();
            var submitButton = document.getElementById("submitButton");

            submitButton.disabled = field1Value === "" || field2Value === "";
        }
    </script>
</body>
</html>