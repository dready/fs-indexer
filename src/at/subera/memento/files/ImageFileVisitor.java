package at.subera.memento.files;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.drew.imaging.*;
import com.drew.metadata.*;
import com.drew.metadata.exif.ExifIFD0Descriptor;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.iptc.IptcDirectory;

import at.subera.memento.rest.bean.Image;
import at.subera.memento.rest.service.ImageService;

/**
 * ImageFileVisitor
 * 
 * checks Files if they are Images and add them into the imageService
 */
public class ImageFileVisitor implements FileVisitor<Path> {
	protected ImageService imageService;
	
	private static final Logger logger = Logger.getLogger(ImageFileVisitor.class);
	
	// found due tests
	public static final int TAG_RATING = 0x4746;

	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			throws IOException {
		if (logger.isInfoEnabled()) {
			logger.info("Working on File: " + file.toString());
		}
		
		try {			
			// check if file is image
			BufferedImage image = ImageIO.read(file.toFile());
			
			Metadata metadata = ImageMetadataReader.readMetadata(file.toFile());
			
			// add image to service
			Image i = new Image();
			i.setId("" + file.toString().hashCode());
			i.setPath(file.toString());

			// add basic file attributes
			BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);
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
					i.setOrientation((byte) directory.getInt(ExifIFD0Directory.TAG_ORIENTATION));
					int rating = -1;
					if (directory.containsTag(TAG_RATING)) {
						rating = directory.getInt(TAG_RATING);
					}
					i.setRating(rating);
				}
				// obtain the Exif sub directory
				if (metadata.containsDirectory(ExifSubIFDDirectory.class)) {
					directory = metadata.getDirectory(ExifSubIFDDirectory.class);
					i.setCreationTime(directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL));
					i.setWidth(directory.getInt(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH));
					i.setHeight(directory.getInt(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT));
				}
				// obtain the GPS directory
				if (metadata.containsDirectory(GpsDirectory.class)) {
					directory = metadata.getDirectory(GpsDirectory.class);
					i.setLatitude(directory.getInt(GpsDirectory.TAG_GPS_LATITUDE));
					i.setLongitude(directory.getInt(GpsDirectory.TAG_GPS_LONGITUDE));
				}
				// optain the IPTC directory
				if (metadata.containsDirectory(IptcDirectory.class)) {
					directory = metadata.getDirectory(IptcDirectory.class);
					i.setKeywords(directory.getString(IptcDirectory.TAG_KEYWORDS));
				}
			} catch (MetadataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
			if (logger.isDebugEnabled()) {
				logger.debug(i);
			}
			
			imageService.add(i);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ImageProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc)
			throws IOException {
		// TODO Auto-generated method stub
		return FileVisitResult.TERMINATE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc)
			throws IOException {
		// TODO Auto-generated method stub
		return FileVisitResult.CONTINUE;
	}

}
