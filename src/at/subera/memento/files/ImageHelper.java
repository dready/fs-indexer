package at.subera.memento.files;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;

import at.subera.memento.rest.bean.Image;

public class ImageHelper {
	// found due tests
	public static final int TAG_WIN_RATING = 0x4746;
	
	public static Image readImageFromFile(Path file, Logger logger) throws IOException, ImageProcessingException {
		// check if file is image
		@SuppressWarnings("unused")
		BufferedImage image = ImageIO.read(file.toFile());

		Metadata metadata = ImageMetadataReader.readMetadata(file.toFile());

		// add image to service
		Image i = new Image();
		i.setId("" + file.toString().hashCode());
		i.setPath(file.toString());
		i.setFilename(file.getFileName().toString());

		// add basic file attributes
		BasicFileAttributes attributes = Files.readAttributes(file,
				BasicFileAttributes.class);
		i.setFilesize(attributes.size());
		i.setLastModifiedTime(new Date(attributes.lastModifiedTime().toMillis()));

		if (logger.isInfoEnabled()) {
			logger.info(metadata);
		}

		// JPEG Metadata
		try {
			Directory directory;
			// obtain the Exif directory
			if (metadata.containsDirectory(ExifIFD0Directory.class)) {
				directory = metadata.getDirectory(ExifIFD0Directory.class);
				// Image Orientation
				i.setOrientation((byte) directory
						.getInt(ExifIFD0Directory.TAG_ORIENTATION));
				// Windows Keywords
				if (directory.containsTag(ExifIFD0Directory.TAG_WIN_KEYWORDS)) {
					i.setKeywords(directory
							.getDescription(ExifIFD0Directory.TAG_WIN_KEYWORDS));
				}
				// Windows Rating
				int rating = -1;
				if (directory.containsTag(TAG_WIN_RATING)) {
					rating = directory.getInt(TAG_WIN_RATING);
				}
				i.setRating(rating);
			}
			// obtain the Exif sub directory
			if (metadata.containsDirectory(ExifSubIFDDirectory.class)) {
				directory = metadata.getDirectory(ExifSubIFDDirectory.class);
				// creation date
				i.setCreationTime(directory
						.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL));
				// resolution width
				i.setWidth(directory
						.getInt(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH));
				// resolution height
				i.setHeight(directory
						.getInt(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT));
			}
			// obtain the GPS directory
			if (metadata.containsDirectory(GpsDirectory.class)) {
				directory = metadata.getDirectory(GpsDirectory.class);
				// gps coordinations
				i.setLatitude(directory.getInt(GpsDirectory.TAG_GPS_LATITUDE));
				i.setLongitude(directory.getInt(GpsDirectory.TAG_GPS_LONGITUDE));
			}
		} catch (MetadataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// if (logger.isInfoEnabled()) {
			// logger.info(e);
			// }
		}

		if (logger.isDebugEnabled()) {
			logger.debug(i);
		}
		
		return i;
	}
}
