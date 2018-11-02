<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Testing Web Services</h1>
	
	<form action="http://localhost:8080/comsws/oauth/authorize">
		<p>Response type: <input type="text" name="response_type" value="code"/></p>
		<p>Client Id: <input type="text" name="client_id" value="comsClient"/></p>
		<p>Redirect URI: <input type="text" name="redirect_uri" value="http://localhost:8080/coms/contacts"/></p>
		<p>Scope: <input type="text" name="scope" value="read"/></p>
		<p><input type="submit"/></p>
	</form>
</body>
</html>