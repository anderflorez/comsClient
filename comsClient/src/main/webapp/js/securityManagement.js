"use strict"

$(document).ready(function() {
	
	$(".clickable").click(function() {
		window.location = $(this).data("href");
	});
});