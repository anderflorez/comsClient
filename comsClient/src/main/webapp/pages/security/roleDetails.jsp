<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<c:import url="/pages/navigation/head.jsp"/>
<body>
	
<c:import url="/pages/navigation/sidebar.jsp" />

<div class="page-container">
	<!-- Styles -->
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/securityManagement.css'/>">
	<c:import url="/pages/navigation/header.jsp" />

	
	<div class="page-content">

		<nav class="nav nav-tabs">
			<a class="nav-item nav-link" href="<c:url value='/contacts'/>">Contacts</a>
			<a class="nav-item nav-link" href="<c:url value='/users'/>">Users</a>
			<a class="nav-item nav-link active" href="<c:url value='/roles'/>">Roles</a>
		</nav>

		<div>
			<div class="jumbotron">
			
				<!-- Role details title and action buttons -->
				<div class="row mb15">
					<div class="col-12">
						<h2 id="manageDetailsTitle" class="d-inline">Role Details</h2>
						<div class="dropdown float-right">
							<a href="#" class="btn btn-sm btn-success dropdown-toggle" role="button" id="actionMenu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
								Actions
							</a>
							<div class="dropdown-menu" aria-labelledby="actionMenu">
								<a href="<c:url value='/roleManagement?rid=${role.roleId}'/>" class="dropdown-item">Edit Role</a>
								<button type="button" class="dropdown-item" data-toggle="modal" data-target="#deleteRoleConfirmation">
									Delete
								</button>
							</div>
						</div>
					</div>
					<div class="col-12"><hr></div>
				</div>
				
				<!-- Role details display -->
				<form:form modelAttribute="role">
					<div class="form-group row mb15">
						<label for="roleName" class="col-12 col-md-3 col-lg-2 col-form-label"><strong>Role Name: </strong></label>
						<div class="col-12 col-md-9 col-lg-10 inputDisplay">
							<form:input id="roleName" path="roleName" readonly="true" class="form-control-plaintext"/>
						</div>
					</div>

					<form:input path="roleId" class="d-none"/>
				</form:form>
			</div>
			
			<div class="row mb25">
			
				<!-- Role member list -->
				<div class="col-6">
					<div class="jumbotron">
						<div class="row">
							<div class="col-12">
								<h2 class="d-inline">Members</h2>
								<div class="float-right">
									<button type="button" class="btn btn-outline-success" data-toggle="modal" data-target="#removeRoleMembersConfirmation">
										Remove Members
									</button>
								</div>
							</div>
							<div class="col-12"><hr></div>
							<div class="col-12">
								<form>
									<c:forEach items="${members}" var="member">
										<div class="checkbox">
											<label data-member="${member.userId}">
												<input class="memberCheck" type="checkbox"> ${member.firstName} ${member.lastName}
											</label>
										</div>
									</c:forEach>
								</form>
							</div>
						</div>
					</div>
				</div>
				
				<!-- Role non-member list -->
				<div class="col-6">
					<div class="jumbotron">
						<div class="row">
							<div class="col-12">
								<h2 class="d-inline">Other Users</h2>
								<div class="float-right">
									<button type="button" class="btn btn-outline-success" data-toggle="modal" data-target="#addRoleMembersConfirmation">
										Add Members
									</button>
								</div>
							</div>
							<div class="col-12"><hr></div>
							<div class="col-12">
								<form>
									<c:forEach items="${nonMembers}" var="nonMember">
										<div class="checkbox">
											<label data-nonmember="${nonMember.userId}">
												<input class="non-memberCheck" type="checkbox"> ${nonMember.firstName} ${nonMember.lastName}
											</label>
										</div>
									</c:forEach>
								</form>
							</div>
						</div>
					</div>
				</div>
				
			</div>
		</div>
	</div>	
</div>

<c:import url="/pages/navigation/scriptDefinitions.jsp"/>

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
<!-- Delete confirmation modal -->
<div class="modal fade" id="deleteRoleConfirmation" tabindex="-1" role="dialog" aria-labelledby="deleteRoleLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="deleteRoleLabel">Delete Confirmation</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div>Are you sure you want to delete the role ${role.roleName}?</div><br>
				<div>Warning: This action will also remove any permissions for users that belong to this role</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
				<form method="POST" action="<c:url value='/deleteRole'/>">
					<input type="text" name="roleId" value="${role.roleId}" class="d-none">
					<input type="submit" name="submit" value="Delete" class="btn btn-danger">
				</form>
			</div>
		</div>
	</div>
</div>

<!-- Remove role member confirmation modal -->
<div class="modal fade" id="removeRoleMembersConfirmation" tabindex="-1" role="dialog" aria-labelledby="removeMemberLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="removeMemberLabel">Remove Member Confirmation</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div>Are you sure you want to remove the selected members from the role ${role.roleName}?</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
				<form method="POST" action="<c:url value='${role_removemember}'/>">
					<input class="d-none" type="text" name="roleId" value="${role.roleId}">
					<input id="removeMembersSubmit" type="submit" name="submit" value="Remove Members" class="btn btn-danger">
				</form>
			</div>
		</div>
	</div>
</div>

<!-- Add role member confirmation modal -->
<div class="modal fade" id="addRoleMembersConfirmation" tabindex="-1" role="dialog" aria-labelledby="addMemberLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="addMemberLabel">Add Member Confirmation</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div>Are you sure you want to add the selected users to the members of the role ${role.roleName}?</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
				<form method="POST" action="<c:url value='${role_addmember}'/>">
					<input class="d-none" type="text" name="roleId" value="${role.roleId}">
					<input id="addMembersSubmit" type="submit" name="submit" value="Add Users" class="btn btn-danger">
				</form>
			</div>
		</div>
	</div>
</div>
<!-- Scripts -->
<script src="<c:url value='/js/securityManagement.js'/>" type="text/javascript" charset="utf-8" async defer></script>

</body>
</html>