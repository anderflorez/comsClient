<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Testing Web Services</h1>
	
	${contact.contactId}
	
	<table>
		<tr>
			<th>contactId</th>
			<th>email</th>
			<th>firstName</th>
		</tr>
		
		<c:forEach items="${contacts}" var="contact">
			<tr>
				<td>${contact.contactId}</td>
				<td>${contact.email}</td>
				<td>${contact.firstName}</td>
			</tr>
		</c:forEach>
		
	</table>
</body>
</html>