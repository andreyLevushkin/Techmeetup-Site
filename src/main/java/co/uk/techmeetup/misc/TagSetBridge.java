package co.uk.techmeetup.misc;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.search.bridge.StringBridge;

import co.uk.techmeetup.data.Tag;

public class TagSetBridge implements StringBridge {

	@Override
	public String objectToString(Object in) {
		Collection<Tag> tags =  (Collection<Tag>) in;
		String tagString="";
		for(Tag tag:tags){
			tagString+=tag.getTagString()+" ";
		}
		return tagString;
	}

}
