package co.uk.techmeetup.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.hibernate.Session;

import co.uk.techmeetup.data.Image;
import co.uk.techmeetup.data.User;
import co.uk.techmeetup.services.ImageService;

public class TmpImage {

	private final Image image;
	private final String path;

	public TmpImage(Image image, String path) {
		if (image == null || path == null) {
			throw new NullPointerException("Tmp image or path cannot be null");
		}
		this.image = image;
		this.path = path;
	}

	public Image getImage() {
		return image;
	}

	public Image keep(Session session, User user) throws IOException {
		boolean hadTransaction = false;
		if (session.getTransaction() == null) {
			session.beginTransaction();
			hadTransaction = true;
		}
		session.save(image);
		image.setOwner(user);
		image.setUrl(ControlVars.IMAGE_URL_PREFIX + image.getId() + "."
				+ ImageService.IMAGE_EXT);
		if (hadTransaction) {
			session.getTransaction().commit();
		}

		String permanentPath = ControlVars.IMAGE_FILE_PATH + image.getId()
				+ "." + ImageService.IMAGE_EXT;
		File permanentImage = new File(permanentPath);
		File tmpFile = new File(path);

		FileInputStream from = null;
		FileOutputStream to = null;

		from = new FileInputStream(tmpFile);
		to = new FileOutputStream(permanentImage);
		byte[] buffer = new byte[4096];
		int bytesRead;

		while ((bytesRead = from.read(buffer)) != -1) {
			to.write(buffer, 0, bytesRead); // write
		}
		return image;
	}

	protected void finalize() throws Throwable {
		File file = new File(path);
		file.delete();
	}

}
