package co.uk.techmeetup.components;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;

public class ModalDialog {
//TODO I think this can be removed
	@InjectComponent
	private ExternalUserProfile externalUserProfile;
	
	@OnEvent(component="modalDialog")
	public Object onProfile(int id) {
		externalUserProfile.setId(id);
		return externalUserProfile;
	}
}
