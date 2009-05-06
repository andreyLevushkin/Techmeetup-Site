package co.uk.techmeetup.services;

import co.uk.techmeetup.data.Notification;
import co.uk.techmeetup.data.TmuEntity;
import co.uk.techmeetup.data.User;

public interface NotificationService {

	public void startNotifications();

	public void stopNotifications();

	public void mute(User user, TmuEntity entity);

	public void unMute(User user, TmuEntity entity);

	public void unMute(User user, String hash);

	public boolean isMuted(User user, TmuEntity entity);

	public Notification  mute(String hash);

}
