<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<c:import url="/pages/navigation/head.jsp"/>
<body>
	
<c:import url="/pages/navigation/sidebar.jsp" />

<div class="page-container">
	<c:import url="/pages/navigation/header.jsp" />

	<!-- Styles -->
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/securityManagement.css'/>">
	
	<div class="page-content">

		<nav class="nav nav-tabs">
			<a class="nav-item nav-link active" href="<c:url value='/contacts'/>">Contacts</a>
			<a class="nav-item nav-link" href="<c:url value='/users'/>">Users</a>
			<a class="nav-item nav-link" href="<c:url value='/roles'/>">Roles</a>
		</nav>

		<div>
			<div class="jumbotron">
				<div id="newContact" class="row mb15">
					<div class="col-12">
						<button type="button" class="float-right btn btn-sm btn-outline-success clickable" data-href="<c:url value='/manageContact?c=0'/>">
							New Contact
						</button>
					</div>
				</div>
				
				<div class="row">
					<div class="col-12 table-responsive">
						<table class="table table-hover">
							<thead>
								<tr class="table-success">
									<th scope="col">Contact Name</th>
									<th scope="col">E-Mail</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${contacts}" var="contact">
									<tr class="clickable" data-href="<c:url value='/contact?cid=${contact.contactId}'/>">
										<td>${contact.firstName} ${contact.lastName}</td>
										<td>${contact.email}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>			
		</div>
	</div>
</div>

<c:if test="${error != null}">
	<div class="alert alert-danger alert-dismissible fade show" role="alert">
		${error}
		<button type="button" class="close" data-dismiss="alert" aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
	</div>
</c:if>

<c:import url="/pages/navigation/scriptDefinitions.jsp"/>

<!-- Scripts -->
<script src="<c:url value='/js/securityManagement.js'/>" type="text/javascript" charset="utf-8" async defer></script>

</body>
</html>