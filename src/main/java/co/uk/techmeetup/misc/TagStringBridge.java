package co.uk.techmeetup.misc;

import org.hibernate.search.bridge.StringBridge;

import co.uk.techmeetup.data.Tag;

public class TagStringBridge implements StringBridge {

	@Override
	public String objectToString(Object arg0) {
		Tag tag = (Tag) arg0;
		return tag.getTagString();
	}

}
