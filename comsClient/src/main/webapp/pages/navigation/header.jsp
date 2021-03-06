<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="header">
	<div id="nav-menu">
		<button type="button">
			<i class="fas fa-bars"></i>
		</button>
	</div>
	<div id="nav-right">
		<button>
			<i class="fas fa-tasks"></i>
		</button>
		<div class="dropdown">
			<a href="" id="user-menu" data-toggle="dropdown" class="dropdown-toggle" role="button" aria-haspopup="true" aria-expanded="false">
				<span>${loggedUser}</span>
			</a>
			<div class="dropdown-menu" aria-labelledby="user-menu">
				<a href="#" class="dropdown-item"><i class="ti-settings"></i>Settings</a>
				<a href="#" class="dropdown-item"><i class="ti-user"></i>Profile</a>
				<a href="<c:url value='/policies'/>" class="dropdown-item"><i class="fas fa-users-cog"></i>Management</a>
				<div class="dropdown-divider"></div>
				<a href="http://localhost:8080/comsws/logout" class="dropdown-item"><i class="ti-power-off"></i>Logout</a>
			</div>
		</div>
	</div>
</div>
