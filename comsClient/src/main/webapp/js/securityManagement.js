"use strict"

$(document).ready(function() {
	
	$(".clickable").click(function() {
		window.location = $(this).data("href");
	});
	
	$(".memberCheck").click(function() {
		if ($(this).is(":checked"))
		{
			let id = $(this).parent("label").attr("data-member");
			$("#removeMembersSubmit").before("<input id='membertoremove_" + id + "' class='d-none' type='text' name='userId' value='" + id + "'>");
		}
		else
		{
			let id = $(this).parent("label").attr("data-member");
			$("#membertoremove_" + id).remove();
		}
	});
	
	$(".non-memberCheck").click(function() {
		if ($(this).is(":checked"))
		{
			let id = $(this).parent("label").attr("data-nonmember");
			$("#addMembersSubmit").before("<input id='usertoadd_" + id + "' class='d-none' type='text' name='userId' value='" + id + "'>");
		}
		else
		{
			let id = $(this).parent("label").attr("data-nonmember");
			$("#usertoadd_" + id).remove();
		}
	});
});