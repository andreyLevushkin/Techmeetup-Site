package co.uk.techmeetup.data;

import java.util.HashSet;
import java.util.Set;

public class Image extends TmuEntity {

	private String url;
	private Set<TmuEntity> imageEntities = new HashSet<TmuEntity>();

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Set<TmuEntity> getImageEntities() {
		return imageEntities;
	}

	public void setImageEntities(Set<TmuEntity> imageEntities) {
		this.imageEntities = imageEntities;
	}

}
