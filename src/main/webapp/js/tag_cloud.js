String.prototype.trim = function() {
	return this.replace(/^\s+|\s+$/g, "");
}
String.prototype.ltrim = function() {
	return this.replace(/^\s+/, "");
}
String.prototype.rtrim = function() {
	return this.replace(/\s+$/, "");
}
var delayedSearchSubmit;
var eventListenersInited = false;

function submitSearch() {
	if (delayedSearchSubmit != null) {
		clearTimeout(delayedSearchSubmit);
	}
	var clickevent = document.createEvent("Event");
	clickevent.initEvent("submit", true, true);
	$('tagSearchForm').dispatchEvent(clickevent);
}

function initTagCloud() {
	if (eventListenersInited) {
		return;
	}
	eventListenersInited = true;

	var tagCloud = $("fullPageZone");
	Event
			.observe(
					tagCloud,
					'click',
					function(event) {
						var element = Event.element(event);
						if (element.tagName == 'A'
								&& element.className == "tag") {

							var value = $('searchString').value;
							var newTag = element.text.trim();
							if (value.match(newTag) == null) {
								if (value.charAt(value.length - 1) == ' ') {
									$('searchString').value += element.text
											.trim() + ' ';
								} else {
									$('searchString').value += ' ' + element.text
											.trim() + ' ';
								}

								submitSearch();
							}
							Event.stop(event);
						} else if (element.tagName == 'A'
								&& element.className == "tag meta_tag") {
							$('searchString').value = element.text.trim();
							submitSearch();
							Event.stop(event);
						} else if (element.tagName == 'A'
								&& element.className == "tag all") {
							$('searchString').value = '';
							submitSearch();
							Event.stop(event);
						} else if (element.tagName == 'A'
								&& element.className == "clear_tags") {
							$('searchString').value = '';
							submitSearch();
							Event.stop(event);
						}
					});

	Event.observe($('searchString'), 'keyup', function(event) {
		if (delayedSearchSubmit == null) {
			delayedSearchSubmit = setTimeout("submitSearch();", 700);
		} else {
			clearTimeout(delayedSearchSubmit);
			delayedSearchSubmit = setTimeout("submitSearch();", 700);
		}
	});

	Event.observe($('tagSearchForm'), 'submit', function(event) {
		clearTimeout(delayedSearchSubmit);
	});

}
/*
 * Event.observe($('all_questions'), 'click', function(event) {
 * clearSearchField(); });
 * 
 * Event.observe($('users_questions'), 'click', function(event) {
 * searchForUserQuestions(); }); }
 */

function searchForUserQuestions() {
	$('searchString').value = "";
	$('searchString').value = $('logged_in_username').innerHTML;
	submitSearch();
	return false;
}

function clearSearchField() {
	$('searchString').value = "";
	submitSearch();
	return false;
}