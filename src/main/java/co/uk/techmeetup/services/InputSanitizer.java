package co.uk.techmeetup.services;

import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;

public interface InputSanitizer {

	public String sanitize(String in);

	public String sanitizeWithBreaks(String in);
}
