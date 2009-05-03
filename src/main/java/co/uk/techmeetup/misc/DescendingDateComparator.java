package co.uk.techmeetup.misc;

import java.util.Comparator;

import co.uk.techmeetup.data.TmuEntity;

public class DescendingDateComparator implements Comparator<TmuEntity> {

	@Override
	public int compare(TmuEntity o1, TmuEntity o2) {
		return -1 * o1.getCreated().compareTo(o2.getCreated());
	}

}
