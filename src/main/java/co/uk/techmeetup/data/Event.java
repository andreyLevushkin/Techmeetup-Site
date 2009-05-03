package co.uk.techmeetup.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

@Indexed
public class Event extends TmuEntity {

	public static final short NEW = 0;
	public static final short APPROVED = 1;
	public static final short REJECTED = 2;
	public static final SimpleDateFormat sdf = new SimpleDateFormat(
			"EEE, d MMM yyyy");
	@Field
	private String title;
	@Field
	private String description;
	private float cost;
	@Field
	private String location;
	private Date date;
	private String time;
	private Date lastChanged;
	@Field
	private String website;
	private User approvedBy;
	private Date approvedOn;
	private short status = NEW;
	private Image image;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getLastChanged() {
		return lastChanged;
	}

	public void setLastChanged(Date lastChanged) {
		this.lastChanged = lastChanged;
	}

	public Date getDate() {
		return date;
	}

	public String getNiceDate() {
		return sdf.format(date);
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
	}

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

}
