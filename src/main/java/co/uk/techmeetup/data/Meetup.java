package co.uk.techmeetup.data;

import java.util.HashSet;
import java.util.Set;

public class Meetup {


	private int id;
	private String name;
	private Set<User> admins = new HashSet<User>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getAdmins() {
		return admins;
	}

	public void setAdmins(Set<User> admins) {
		this.admins = admins;
	}
}
