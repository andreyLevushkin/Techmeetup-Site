var defaultExpertTagsField = "What are your core skills?";
var defaultInterestTagsField = "What else are you interested in?";
var defaultAboutMeField = "Enter some information about yourself";

var initialClass = "input_initial";

function initJoinForm() {

	/* Setup expert tags field */
	if ($('expertTagsField').value == "") {
		$('expertTagsField').value = defaultExpertTagsField;
		$('expertTagsField').className = initialClass;
		Event.observe($('expertTagsField'), 'mousedown', function(event) {
			if ($('expertTagsField').value === defaultExpertTagsField) {
				$('expertTagsField').value = "";
				$('expertTagsField').focus();
				$('expertTagsField').className = '';
			}
		});
	}

	
	/* Setup interest tags field */
	if ($('interestTagsField').value == "") {
		$('interestTagsField').value = defaultInterestTagsField;
		$('interestTagsField').className = initialClass;
		Event.observe($('interestTagsField'), 'mousedown', function(event) {
			if ($('interestTagsField').value === defaultInterestTagsField) {
				$('interestTagsField').value = "";
				$('interestTagsField').focus();
				$('interestTagsField').className = '';
			}
		});
	}

	/* Setup about me field */
	if ($('aboutMeField').value == "") {
		$('aboutMeField').value = defaultAboutMeField;
		$('aboutMeField').className = initialClass;
		Event.observe($('aboutMeField'), 'mousedown', function(event) {
			if ($('aboutMeField').value === defaultAboutMeField) {
				$('aboutMeField').value = "";
				$('aboutMeField').focus();
				$('aboutMeField').className = '';
			}
		});
	}

	/* Make sure the default field values are not submited */
	Event.observe($('joinForm'), 'submit', function(event) {
		if ($('expertTagsField').value === defaultExpertTagsField) {
			$('expertTagsField').value = "";
		}
		if ($('interestTagsField').value === defaultInterestTagsField) {
			$('interestTagsField').value = "";
		}
		if ($('aboutMeField').value === defaultAboutMeField) {
			$('aboutMeField').value = "";
		}
	});
}