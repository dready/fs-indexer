package at.subera.memento.files;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

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
	

	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {
		// TODO Auto-generated method stub
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			throws IOException {
		// TODO Auto-generated method stub
		
		try {
			// check if file is image
			BufferedImage image = ImageIO.read(file.toFile());
			
			// add image to service
			Image i = new Image();
			i.setId("" + file.toString().hashCode());
			i.setPath(file.toString());
			
			if (logger.isDebugEnabled()) {
				logger.debug(i);
			}
			
			imageService.add(i);
		} catch (IOException e) {}
		
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
