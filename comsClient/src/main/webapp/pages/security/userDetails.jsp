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

		<div>
			<div class="jumbotron">
				<h2 id="manageDetailsTitle">User Details</h2>
				<hr>
				
				<div class="row mb15">
					<div class="col-12">
						<c:if test="${error == null}">
							<div class="dropdown float-right ml15">
								<a href="#" class="btn btn-sm btn-success dropdown-toggle" role="button" id="actionMenu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
									Actions
								</a>
								<div class="dropdown-menu" aria-labelledby="actionMenu">
									<a href="<c:url value='/userManagement?uid=${user.userId}'/>" class="dropdown-item">Edit User</a>
									<button type="button" class="dropdown-item" data-toggle="modal" data-target="#changeUserPassword">
										Change Password
									</button>
									<button type="button" class="dropdown-item" data-toggle="modal" data-target="#deleteUserConfirmation">
										Delete
									</button>
								</div>
							</div>
						</c:if>
						<a href="<c:url value='/users'/>" class="float-right btn btn-sm btn-outline-primary ml15">
							Back
						</a>
					</div>
				</div>
				
				<form:form modelAttribute="user">
 					<div class="form-group row">
						<label for="userContactName" class="col-12 col-md-3 col-lg-2 col-form-label"><strong>Name: </strong></label>
						<div class="col-12 col-md-9 col-lg-10">
							<input id="userContactName" value="${contact.firstName} ${contact.lastName}" readonly class="form-control-plaintext"/>
						</div>
					</div>
					<div class="form-group row">
						<label for="userUsername" class="col-12 col-md-3 col-lg-2 col-form-label"><strong>Username: </strong></label>
						<div class="col-12 col-md-9 col-lg-10">
							<form:input id="userUsername" path="username" readonly="true" class="form-control-plaintext"/>
						</div>
					</div>
					<div class="form-group row">
						<label for="userStatus" class="col-12 col-md-3 col-lg-2 col-form-label"><strong>Status: </strong></label>
						<div class="col-12 col-md-9 col-lg-10">
							<form:input id="userStatus" path="enabledStatus" readonly="true" class="form-control-plaintext"/>
						</div>
					</div>
					<div class="form-group row">
						<label for="userDateAdded" class="col-12 col-md-3 col-lg-2 col-form-label"><strong>Date Added: </strong></label>
						<div class="col-12 col-md-9 col-lg-10">
							<form:input id="userDateAdded" path="dateAdded" readonly="true" class="form-control-plaintext"/>
						</div>
					</div>
					<div class="form-group row">
						<label for="userLastAccess" class="col-12 col-md-3 col-lg-2 col-form-label"><strong>Last Access: </strong></label>
						<div class="col-12 col-md-9 col-lg-10">
							<form:input id="userLastAccess" path="lastAccess" readonly="true" class="form-control-plaintext"/>
						</div>
					</div>
					<form:input path="userId" class="d-none"/>
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

<!-- Modals -->
<div class="modal fade" id="deleteUserConfirmation" tabindex="-1" role="dialog" aria-labelledby="deleteUserLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="deleteUserLabel">Delete Confirmation</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				Are you sure you want to delete this user?
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
				<form method="POST" action="<c:url value='/deleteUser'/>">
					<input type="text" name="userId" value="${user.userId}" class="d-none">
					<input type="submit" name="submit" value="Delete" class="btn btn-danger">
				</form>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="changeUserPassword" tabindex="-1" role="dialog" aria-labelledby="changePasswordLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="changePasswordLabel">Change Password</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<form method="POST" action="<c:url value='/userPasswordChange'/>">
					<div class="form-group row">
						<input type="text" name="userId" class="d-none" value="${userPassword.userId}">
					</div>
					<div class="form-group row">
						<input type="password" name="oldPassword" class="form-control mx-3" value="${userPassword.oldPassword}" placeholder="Password">
					</div>
					<div class="form-group row">
						<input type="password" name="newPassword" class="form-control mx-3" value="${userPassword.newPassword}" placeholder="New Password">
					</div>
					<div class="form-group row">
						<input type="password" name="confirmPassword" class="form-control mx-3" value="${userPassword.confirmPassword}" placeholder="Confirm Password">
					</div>
					<div class="modal-footer">
						<div class="form-group row">
							<button type="button" class="btn btn-secondary mr-2" data-dismiss="modal">Cancel</button>
							<input type="submit" name="submit" value="Submit" class="btn btn-success mr-2">
						</div>					
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<c:import url="/pages/navigation/scriptDefinitions.jsp"/>

<!-- Scripts -->
<script src="<c:url value='/js/securityManagement.js'/>" type="text/javascript" charset="utf-8" async defer></script>

</body>
</html>
