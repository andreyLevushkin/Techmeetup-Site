package co.uk.techmeetup.services;

import co.uk.techmeetup.data.User;

public interface PasswordService {
	public boolean checkPassword(User user, String password);

	public String resetPassword(User user);

	public void updatePassword(User user, String string);
}
