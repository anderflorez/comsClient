<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<c:import url="/pages/navigation/head.jsp"/>
<body>

<c:import url="/pages/navigation/sidebar.jsp"/>

<div class="page-container">
	<c:import url="/pages/navigation/header.jsp"/>
	
	<div class="page-content">
		
		<h1>Hello World!</h1>
	
	</div>

</div>

<c:if test="${errors != null}">
	<c:forEach items="${errors}" var="error">
		<div class="alert alert-danger alert-dismissible fade show" role="alert">
			${error}
			<button type="button" class="close" data-dismiss="alert" aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
		</div>	
	</c:forEach>
</c:if>
	
<c:import url="/pages/navigation/scriptDefinitions.jsp"/>

</body>
</html>