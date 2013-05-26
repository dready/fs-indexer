package at.subera.fs.indexer.visitor;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import org.apache.log4j.Logger;

import at.subera.memento.rest.bean.Image;
import at.subera.memento.rest.service.ImageService;

/**
 * ImageFileVisitor
 * 
 * checks Files if they are Images and add them into the imageService
 */
public class ImageFileVisitor extends SimpleFileVisitor<Path> implements IndexFileVisitor<Path> {
	protected ImageService imageService;
	
	protected Filter filter = new Filter();

	private static final Logger logger = Logger
			.getLogger(ImageFileVisitor.class);

	public ImageFileVisitor() {
	}

	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			throws IOException {
		if (!filter.matchPath(file)) {
			return FileVisitResult.CONTINUE;
		}

		if (logger.isInfoEnabled()) {
			logger.info("Working on File: " + file.toString());
		}

		imageService.add(file);

		return FileVisitResult.CONTINUE;
	}

	@Override
	public void preIndex(String directory) {}

	@Override
	public void postIndex(String directory) {
		if (logger.isInfoEnabled()) {
			List<Image> images = imageService.get();
			logger.info("Collect End: Found " + images.size() + "images");
		}
	}
}
