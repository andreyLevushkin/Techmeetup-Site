function showUserProfileDialog(id) {
	var body = document.body;

	var dialogDiv = $('modal_dialog');
	if (dialogDiv == null) {
		dialogDiv = document.createElement('div');
		dialogDiv.id = "modal_dialog";
	}

	body.appendChild(dialogDiv);
	var request;
	var onFetch = function() {
		var returned = eval('(' + request.transport.responseText + ')');
		dialogDiv.innerHTML = returned.content;
	};
	request = Tapestry.ajaxRequest("/servicepage.externalUserProfile/"+id,
			onFetch);
}

/**
 * Shows the profile of the specified user
 * 
 * @param userId
 *            User ID of ther user whos profile is shown
 * @return
 */
function showUserDialog(userId) {

}

/**
 * Brings up a dialog that allows to send a message to the specified user
 * 
 * @param userId
 *            User ID of the user to whom the message will be sent
 * @return
 */
function sendMessage(userId) {

}

function showLoginDialog() {

}

function closeDialog() {
	document.body.removeChild($('modal_dialog'));
}
