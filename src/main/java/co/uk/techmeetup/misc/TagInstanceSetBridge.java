package co.uk.techmeetup.misc;

import java.util.List;
import java.util.Set;

import org.hibernate.search.bridge.StringBridge;
import co.uk.techmeetup.data.*;

public class TagInstanceSetBridge implements StringBridge {

	@Override
	public String objectToString(Object arg0) {
		String out = "";
		Set<TagInstance> tagInstances = (Set<TagInstance>) arg0;
		for(TagInstance tagInstance:tagInstances){
			out+=tagInstance.getTag().getTagString()+" ";
		}
		return out;
	}

}
