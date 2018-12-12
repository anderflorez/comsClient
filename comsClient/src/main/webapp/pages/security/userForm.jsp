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
			<a class="nav-item nav-link" href="<c:url value='/contacts'/>">Contacts</a>
			<a class="nav-item nav-link active" href="<c:url value='/users'/>">Users</a>
			<a class="nav-item nav-link" href="<c:url value='/roles'/>">Roles</a>
		</nav>

		<div id="userManagement">
			<div class="jumbotron">
				<c:if test="${user.userId != null}">
					<h2>Edit User</h2>
				</c:if>
				
				<c:if test="${user.userId == null}">
					<h2>Create New User</h2>
				</c:if>
				<hr>				
				
				<form:form modelAttribute="user" action="/coms/userManagement">
					<div class="form-group row">
						<label for="userUsername" class="col-12 col-md-3 col-lg-2 col-form-label"><strong>Username: </strong></label>
						<div class="col-12 col-md-9 col-lg-10">
							<form:input path="username" class="form-control"/>
						</div>
					</div>
					<div class="form-group row">
						<label for="userStatus" class="col-12 col-md-3 col-lg-2 col-form-label"><strong>Status: </strong></label>
						<div class="col-12 col-md-9 col-lg-10">
							<div class="radio">
								<div>
									<label>
										<c:if test="${user.userId != null}">
											<form:radiobutton path="enabled" value="false" /> Inactive
										</c:if>
										<c:if test="${user.userId == null}">
											<form:radiobutton path="enabled" value="false" checked="true"/> Inactive
										</c:if>
									</label>
								</div>
								<div>
									<label>
										<form:radiobutton path="enabled" value="true"/> Active
									</label>
								</div>
							</div>
						</div>
					</div>

					<c:if test="${user.userId == null}">
						<div class="form-group row">
							<label for="password" class="col-12 col-md-3 col-lg-2 col-form-label"><strong>Password: </strong></label>
							<div class="col-12 col-md-9 col-lg-10">
								<form:input path="password" type="password" class="form-control" placeholder="Password" />
							</div>
						</div>
						<div class="form-group row">
							<div class="col-12 offset-md-3 col-md-9 offset-lg-2 col-lg-10">
								<form:input path="passwordcheck" type="password" class="form-control" placeholder="Confirm Password" />
							</div>
						</div>
					</c:if>

					<form:input path="userId" class="d-none"/>
					<form:input path="contactId" class="d-none"/>

					<div class="form-group row">
						<div class="col-12">
							<input type="submit" class="btn btn-success float-right ml15" value="Save User">

							<c:if test="${user.userId != null}">
								<a href="<c:url value='/user?uid=${user.userId}'/>" class="btn btn-secondary float-right ml15">Cancel</a>
							</c:if>
							<c:if test="${user.userId == null}">
								<a href="<c:url value='/contact?cid=${user.contactId}'/>" class="btn btn-secondary float-right ml15">Cancel</a>
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

<c:if test="${success != null}">
	<div class="alert alert-success alert-dismissible fade show" role="alert">
		${success}
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