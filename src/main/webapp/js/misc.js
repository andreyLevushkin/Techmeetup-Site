function showLocalNav(){
	$('local_home_nav').style.display="block";
}

function hideLocalNav(){
	$('local_home_nav').style.display="none";
}

function showAdminOptions(){
	$('admin_options').style.display="block";
}

function hideAdminOptions(){
	$('admin_options').style.display="none";
}

function refreshHack(){
	var url=window.document.URL;
	var lastChar=url.charAt(url.length-1);
	if(lastChar=="#"){
		 window.location.replace( url.substring(0,url.length-2) );
	}
}
