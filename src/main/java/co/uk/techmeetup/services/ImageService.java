package co.uk.techmeetup.services;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;

import org.apache.tapestry5.upload.services.UploadedFile;

import co.uk.techmeetup.data.Image;
import co.uk.techmeetup.data.Meetup;
import co.uk.techmeetup.data.User;
import co.uk.techmeetup.misc.TmpImage;

public interface ImageService {

	public static final int PROFILE = 1;
	public static final int VIDEO_THUMB = 2;
	public static final int BLOG_THUMB = 3;
	public static final int EVENT_THUMB = 4;
	public static final String IMAGE_EXT = "PNG";

	public BufferedImage scaleImage(BufferedImage in, int type);

	public Image createImage(UploadedFile uploadedFile, User owner, Meetup meetup,
			int type) throws IOException;

	public TmpImage createTmpImage(UploadedFile uploadedFile, User owner,
			Meetup meetup, int type) throws IOException;
	
	public boolean isSupported(UploadedFile uploadedFile);
}
