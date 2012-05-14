package at.subera.memento.files;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.log4j.Logger;

import at.subera.memento.rest.service.ImageService;

import com.drew.imaging.ImageProcessingException;

/**
 * ImageFileVisitor
 * 
 * checks Files if they are Images and add them into the imageService
 */
public class ImageFileVisitor extends SimpleFileVisitor<Path> {
	protected ImageService imageService;
	
	private static final Logger logger = Logger.getLogger(ImageFileVisitor.class);
	
	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			throws IOException {
		if (logger.isInfoEnabled()) {
			logger.info("Working on File: " + file.toString());
		}
		
		try {			
			imageService.add(ImageHelper.readImageFromFile(file, logger));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ImageProcessingException e) {
			// Typical, not an image
			if (logger.isInfoEnabled()) {
				logger.info(e);
			}
		}
		
		return FileVisitResult.CONTINUE;
	}
}
