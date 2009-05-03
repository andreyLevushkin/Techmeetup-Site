package co.uk.techmeetup.components;

import java.util.Collection;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import co.uk.techmeetup.misc.WeightedString;
import co.uk.techmeetup.services.TagService;

/**
 * Renders the list of most popular tags. This is expected to be rendered in the
 * sidebar. The {@link #type} parameter specifies what type of object these tags
 * are for (BlogPost,Video,User,Question).
 * 
 * @author andrey
 * 
 */
public class PopularTagList extends BaseComponent {

	@SuppressWarnings("unchecked")
	@Parameter(required = true)
	private Class type;

	@SuppressWarnings("unused")
	@Property
	private String thisTag;

	@Inject
	private TagService tagService;

	public Collection<WeightedString> getTags() {
		return tagService.getPopularTags(type);
	}
}
