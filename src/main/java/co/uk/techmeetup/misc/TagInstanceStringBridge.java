package co.uk.techmeetup.misc;

import org.hibernate.search.bridge.StringBridge;

import co.uk.techmeetup.data.TagInstance;

public class TagInstanceStringBridge implements StringBridge {

	@Override
	public String objectToString(Object tagInstaceObject) {
		TagInstance tagInstance = (TagInstance) tagInstaceObject;
		return tagInstance.getTag().getTagString();
	}

}
