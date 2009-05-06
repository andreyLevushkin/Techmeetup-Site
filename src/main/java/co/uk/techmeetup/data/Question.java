package co.uk.techmeetup.data;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

@Indexed
public class Question extends TmuEntity {

	@Field
	private String title;
	@Field
	private String body;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public String getBodyShort() {
		if (body.length() > 70) {
			return body.substring(0, 70);
		}else{
			return body;
		}
	}

	public void setBody(String body) {
		this.body = body;
	}

}
