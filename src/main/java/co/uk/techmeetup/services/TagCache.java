package co.uk.techmeetup.services;

import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import co.uk.techmeetup.misc.WeightedString;

public interface TagCache {

	public SortedSet<WeightedString> getPopularTags(Class type);

	public void setPopularTags(Class type, SortedSet<WeightedString> tags);
}
