package co.uk.techmeetup.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import co.uk.techmeetup.misc.WeightedString;

public class TagCacheImpl implements TagCache {

	public static final long EXPIERY = 1000 * 60 * 30;

	private Map<Class, SortedSet<WeightedString>> cache = new HashMap<Class, SortedSet<WeightedString>>();
	private Map<Class, Date> cacheExpiery = new HashMap<Class, Date>();

	@Override
	public SortedSet<WeightedString> getPopularTags(Class type) {
		Date created = cacheExpiery.get(type);
		if (created != null
				&& (System.currentTimeMillis() - created.getTime()) < EXPIERY) {
			return cache.get(type);
		} else {
			return null;
		}
	}

	@Override
	public synchronized void setPopularTags(Class type,
			SortedSet<WeightedString> tags) {
		cache.put(type, tags);
		cacheExpiery.put(type, new Date());
	}

}
