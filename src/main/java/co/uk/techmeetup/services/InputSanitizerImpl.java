package co.uk.techmeetup.services;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;

import co.uk.techmeetup.misc.ControlVars;

public class InputSanitizerImpl implements InputSanitizer {

	private static Policy policy;

	public InputSanitizerImpl() throws PolicyException {
		this.policy = Policy.getInstance(ControlVars.SANITIZER_POLICY_FILE);
	}

	@Override
	public String sanitize(String in)  {
		AntiSamy as = new AntiSamy();
		CleanResults cr = null;
		try {
			cr = as.scan(in, policy);
		} catch (ScanException e) {
			e.printStackTrace();
		} catch (PolicyException e) {
			e.printStackTrace();
		}
		return cr.getCleanHTML();
	}

	@Override
	public String sanitizeWithBreaks(String in)  {
		String out =sanitize(in.replaceAll("\n", "<br/>"));
		return out;
	}

}
