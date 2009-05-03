package co.uk.techmeetup.services;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.tapestry5.upload.services.UploadedFile;
import org.hibernate.Session;
import org.hibernate.Transaction;

import co.uk.techmeetup.data.Image;
import co.uk.techmeetup.data.Meetup;
import co.uk.techmeetup.data.User;
import co.uk.techmeetup.misc.ControlVars;
import co.uk.techmeetup.misc.TmpImage;

public class ImageServiceImpl implements ImageService {

	private Session session;

	public ImageServiceImpl(Session session) {
		this.session = session;
	}

	@Override
	public BufferedImage scaleImage(BufferedImage in, int type) {
		if (in == null) {
			throw new NullPointerException("The input image cannot be null");
		}
		switch (type) {
		case PROFILE:
			return getScaledInstance(in, ControlVars.PROFILE_IMAGE_WIDTH,
					ControlVars.PROFILE_IMAGE_HEIGHT);
		case VIDEO_THUMB:
			return getScaledInstance(in, ControlVars.THUMBNAIL_IMAGE_WIDTH,
					ControlVars.THUMBNAIL_IMAGE_HEIGHT);
		case BLOG_THUMB:
			return getScaledInstance(in, ControlVars.BLOG_IMAGE_HEIGHT,
					ControlVars.BLOG_IMAGE_WIDTH);
		case EVENT_THUMB:
			return getScaledInstance(in, ControlVars.EVENT_THUMB_HEIGHT,
					ControlVars.EVENT_THUMB_WIDTH);
		default:
			return null;
		}
	}

	/**
	 * Convenience method that returns a scaled instance of the provided {@code
	 * BufferedImage}.
	 * 
	 * @param img
	 *            the original image to be scaled
	 * @param targetWidth
	 *            the desired width of the scaled instance, in pixels
	 * @param targetHeight
	 *            the desired height of the scaled instance, in pixels
	 * @return a scaled version of the original {@code BufferedImage}
	 */
	public BufferedImage getScaledInstance(BufferedImage imgRaw,
			int targetWidth, int targetHeight) {
		Object hint = null;
		BufferedImage img = crop(imgRaw, targetWidth, targetHeight);
		if (targetWidth > img.getWidth() && targetHeight > img.getHeight()) {
			hint = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
		} else {
			hint = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
		}

		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = (BufferedImage) img;
		int w, h;

		w = img.getWidth();
		h = img.getHeight();

		do {
			if (w > targetWidth) {
				w /= 2;

			}
			if (w < targetWidth) {
				w = targetWidth;
			}

			if (h > targetHeight) {
				h /= 2;

			}
			if (h < targetHeight) {
				h = targetHeight;
			}

			BufferedImage tmp = new BufferedImage(w, h, type);
			Graphics2D g2 = tmp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
			g2.drawImage(ret, 0, 0, w, h, null);
			g2.dispose();

			ret = tmp;
		} while (w != targetWidth || h != targetHeight);

		return ret;
	}

	@Override
	public Image createImage(UploadedFile uploadedFile, User owner,
			Meetup meetup, int type) throws IOException {

		Transaction transaction = session.getTransaction();
		boolean hadTransaction = true;
		if (transaction == null) {
			transaction = session.beginTransaction();
			hadTransaction = false;
		}

		BufferedImage thumbnailImage;
		thumbnailImage = ImageIO.read(uploadedFile.getStream());
		thumbnailImage = scaleImage(thumbnailImage, type);

		Image image = new Image();
		image.setOwner(owner);
		image.setMeetup(meetup);
		session.save(image);
		image.setUrl(ControlVars.IMAGE_URL_PREFIX + image.getId() + "."
				+ IMAGE_EXT);
		File imageFile = new File(ControlVars.IMAGE_FILE_PATH + image.getId()
				+ "." + IMAGE_EXT);
		ImageIO.write(thumbnailImage, IMAGE_EXT, imageFile);

		if (!hadTransaction) {
			transaction.commit();
		}
		return image;
	}

	@Override
	public TmpImage createTmpImage(UploadedFile uploadedFile, User owner,
			Meetup meetup, int type) throws IOException {
		BufferedImage thumbnailImage;
		thumbnailImage = ImageIO.read(uploadedFile.getStream());
		thumbnailImage = scaleImage(thumbnailImage, type);

		Image image = new Image();
		image.setOwner(owner);
		image.setMeetup(meetup);
		String path = ControlVars.IMAGE_FILE_PATH + uploadedFile.hashCode()
				+ "_" + ((long) (Math.random() * 100000000)) + "_tmp."
				+ IMAGE_EXT;
		File imageFile = new File(path);
		ImageIO.write(thumbnailImage, IMAGE_EXT, imageFile);
		TmpImage out = new TmpImage(image, path);

		return out;
	}

	private BufferedImage crop(BufferedImage image, int width, int height) {
		double expectedAspect = (double) height / (double) width;
		double actualAspect = (double) image.getHeight()
				/ (double) image.getWidth();
		BufferedImage out = null;
		if (expectedAspect < actualAspect) {
			int newHeight = (int) ((double) image.getHeight() / actualAspect);
			out = image.getSubimage(0, 0, image.getWidth(), newHeight);

		} else if (expectedAspect > actualAspect) {
			int newWidth = (int) ((double) image.getWidth() * actualAspect);
			out = image.getSubimage(0, 0, newWidth, image.getHeight());

		} else {
			out = image;
		}
		return out;
	}

	@Override
	public boolean isSupported(UploadedFile uploadedFile) {
		if (uploadedFile != null) {
			return (ControlVars.SUPPORTED_IMAGE_MIME.contains(uploadedFile
					.getContentType()));
		} else {
			return true;
		}
	}

}
