package co.uk.techmeetup.data;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Indexed
public class BlogPost extends TmuEntity {

	public static final int MAX_BODY_NICE_LENGTH = 200;

	private User approvedBy;

	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String title;

	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String body;

	private Image mainImage;

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public String getBodyNice() {
		if (body.length() > MAX_BODY_NICE_LENGTH) {
			return body.substring(0, MAX_BODY_NICE_LENGTH)+" ...";
		}else{
			return body;
		}
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Image getMainImage() {
		return mainImage;
	}

	public void setMainImage(Image mainImage) {
		this.mainImage = mainImage;
	}

}
