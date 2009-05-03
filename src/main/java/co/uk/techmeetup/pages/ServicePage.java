package co.uk.techmeetup.pages;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;

import co.uk.techmeetup.components.ExternalUserProfile;

public class ServicePage extends BasePage {

	@InjectComponent
	private ExternalUserProfile externalUserProfile;
	
	
	@OnEvent(component="externalUserProfile")
	public Object onShowExternalUserProfile(int id){
		externalUserProfile.setId(id);
		return externalUserProfile;
	}
}
