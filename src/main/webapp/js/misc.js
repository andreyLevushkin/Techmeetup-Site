function showLocalNav() {
	$('local_home_nav').style.display = "block";
}

function hideLocalNav() {
	$('local_home_nav').style.display = "none";
}

function showAdminOptions() {
	$('admin_options').style.display = "block";
}

function hideAdminOptions() {
	$('admin_options').style.display = "none";
}

/**
 * This functions should be called in <body onUnload=""/> it makes sure that
 * when the user hists the back button he hoes not see the cached version of the
 * page. This ensures that the users search of page is persisted when the user
 * clicks back out of the details page. Its a bit of a hack.
 * 
 * @return
 */
function refreshHack() {
	try {
		var headElement = document.getElementsByTagName("head")[0];
		if (headElement && headElement.innerHTML)
			headElement.innerHTML += "<meta http-equiv=\"refresh\" content=\"1\">";
	} catch (e) {
	}
}