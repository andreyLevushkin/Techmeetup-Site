var DEFAULT_PASS = "qwertyuiopasdfghjkl";
var hasBeenCleard = false;
function initFillPassword() {
	$('passwordField').value = DEFAULT_PASS;
	$('passwordConfirmField').value = DEFAULT_PASS;

	Event.observe($('passwordConfirmField'), 'click', function(event) {
		if (!hasBeenCleard) {
			$('passwordConfirmField').value = "";
			$('passwordField').value = "";
			hasBeenCleard = true;
		}
	});

	Event.observe($('passwordField'), 'click', function(event) {
		if (!hasBeenCleard) {
			$('passwordConfirmField').value = "";
			$('passwordField').value = "";
			hasBeenCleard = true;
		}
	});

}