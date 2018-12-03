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
				<c:if test="${contact.contactId != null}">
					<h2>Edit Contact</h2>
				</c:if>
				
				<c:if test="${contact.contactId == null}">
					<h2>Create New Contact</h2>
				</c:if>
				<hr>
				
				<form:form modelAttribute="contact">
					<div class="form-group row">
						<label for="contactFName" class="col-12 col-md-3 col-lg-2 col-form-label"><strong>First Name: </strong></label>
						<div class="col-12 col-md-9 col-lg-10 inputDisplay">
							<form:input id="contactFName" path="firstName" class="form-control"/>
						</div>
					</div>
					<div class="form-group row">
						<label for="contactMName" class="col-12 col-md-3 col-lg-2 col-form-label"><strong>Middle Name: </strong></label>
						<div class="ccol-12 col-md-9 col-lg-10 inputDisplay">
							<form:input id="contactMName" path="middleName" class="form-control"/>
						</div>
					</div>
					<div class="form-group row">
						<label for="contactLName" class="col-12 col-md-3 col-lg-2 col-form-label"><strong>Last Name: </strong></label>
						<div class="col-12 col-md-9 col-lg-10 inputDisplay">
							<form:input id="contactLName" path="lastName" class="form-control"/>
						</div>
					</div>
					<div class="form-group row">
						<label for="contactEMail" class="col-12 col-md-3 col-lg-2 col-form-label"><strong>E-Mail: </strong></label>
						<div class="col-12 col-md-9 col-lg-10 inputDisplay">
							<form:input id="contactEMail" path="email" class="form-control"/>
						</div>
					</div>
					<form:input id="objectIdIndicator" path="contactId" class="d-none"/>
					<div class="row">
						<div class="col-12">
							<input type="submit" class="btn btn-success float-right ml15" value="Save Contact">
							<c:if test="${contact.contactId != null}">
								<a href="<c:url value='/contact?cid=${contact.contactId}'/>" class="btn btn-secondary float-right ml15">Cancel</a>
							</c:if>
							<c:if test="${contact.contactId == null}">
								<a href="<c:url value='/contacts'/>" class="btn btn-secondary float-right ml15">Cancel</a>
							</c:if>
						</div>
					</div>
				</form:form>
			</div>			
		</div>
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

<!-- Scripts -->
<script src="<c:url value='/js/securityManagement.js'/>" type="text/javascript" charset="utf-8" async defer></script>

</body>
</html>