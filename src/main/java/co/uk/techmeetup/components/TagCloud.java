package co.uk.techmeetup.components;

import java.util.Collection;

import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;

import co.uk.techmeetup.misc.WeightedString;

/**
 * Renders a tag cloud from the collection of {@link WeightedString}s specified
 * in {@link #tags}
 */
@IncludeJavaScriptLibrary("context:/js/tag_cloud.js")
public class TagCloud extends BaseComponent {

	@Parameter(required = true)
	private Collection<WeightedString> tags;

	@Environmental
	private RenderSupport renderSupport;

	@SetupRender
	/*
	 * Make sure the tag cloud javascript is initialised on page load.
	 */
	public void setupRender() {
		renderSupport.addScript("initTagCloud();");
	}

	@Property
	private WeightedString thisTag;

	/**
	 * Return the collection of WeightedStrings that represent tags.
	 * 
	 * @return
	 */
	public Collection<WeightedString> getTagStrings() {
		return tags;
	}

	/**
	 * Returns the CSS class to apply to the tag currently being rendered. This
	 * is based on the tag's weight
	 * 
	 * @return
	 */
	public String getTagClass() {
		int size = thisTag.getWeight();
		switch (size) {
		case 1:
			return "tag_weight1";
		case 2:
			return "tag_weight2";
		case 3:
			return "tag_weight3";
		case 4:
			return "tag_weight4";
		case 5:
			return "tag_weight5";
		case 6:
			return "tag_weight6";
		case 7:
			return "tag_weight7";
		default:
			return "tag_weight8";

		}

	}

	/**
	 * 
	 * @return true if no tags were specified
	 */
	public boolean getHasNoTags() {
		return tags.size() == 0;
	}

}
