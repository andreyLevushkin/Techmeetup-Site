package co.uk.techmeetup.services;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.hibernate.Session;
import org.hibernate.Transaction;

import co.uk.techmeetup.data.User;
import co.uk.techmeetup.misc.ControlVars;

public class PasswordServiceImpl implements PasswordService {

	private Session session;

	public PasswordServiceImpl(Session session) {
		this.session = session;
	}

	@Override
	public boolean checkPassword(User user, String password) {
		return user.getPasswordHash().equals(
				getHash(password, user.getPasswordSalt()));
	}

	@Override
	public String resetPassword(User user) {
		String newPass = getRandomString(ControlVars.DEFAULT_PASSWORD_LENGTH);
		updatePassword(user, newPass);
		return newPass;
	}

	@Override
	public void updatePassword(User user, String newPass) {
		String newSalt = getRandomString(ControlVars.SALT_LENGTH);
		Transaction transaction = session.getTransaction();
		boolean hadTransaction = false;
		if (transaction == null || transaction.wasCommitted()
				|| transaction.wasRolledBack()) {
			transaction = session.beginTransaction();
		} else {
			hadTransaction = true;
		}

		user.setPasswordSalt(newSalt);
		user.setPasswordHash(getHash(newPass, newSalt));
		if (!hadTransaction) {
			session.getTransaction().commit();
		}

	}

	private String getHash(String password, String salt) {
		return SHA1(password + salt);
	}

	private String getRandomString(int length) {
		Random random = new Random();
		String out = "";
		for (int i = 0; i < length; i++) {
			int rand = random.nextInt(ControlVars.SALT_ALPHABET.length());
			out += ControlVars.SALT_ALPHABET.charAt(rand);
		}
		return out;
	}

	private String convertToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	private String SHA1(String text) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException(
					"MD5 does not existst - can't hash passwords");
		}
		byte[] sha1hash = new byte[40];
		try {
			md.update(text.getBytes("iso-8859-1"), 0, text.length());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException("Unsupported encoding");
		}
		sha1hash = md.digest();
		return convertToHex(sha1hash);
	}
}
