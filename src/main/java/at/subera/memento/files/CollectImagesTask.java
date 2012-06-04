package at.subera.memento.files;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.List;

import org.apache.log4j.Logger;

import at.subera.memento.rest.bean.Image;
import at.subera.memento.rest.service.ImageService;

public class CollectImagesTask implements Runnable {
	protected String directory;
	protected ImageService imageService;
	
	public static int MAX_DEPTH = 100;
	
	protected FileVisitor<Path> visitor;

		public void setVisitor(FileVisitor<Path> visitor) {
		this.visitor = visitor;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public CollectImagesTask(String directory) {
		setDirectory(directory);
	}
	
	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}
	
	private static final Logger logger = Logger.getLogger(CollectImagesTask.class);

	Thread collector;
	
	public void init() {
		collector = new Thread(this);
		collector.setPriority(Thread.MIN_PRIORITY);
		collector.start();
	}
	
	@SuppressWarnings("deprecation")
	public void cleanup() {
		collector.stop();
	}
	
	@Override
	public void run() {
		if (logger.isInfoEnabled()) {
			logger.info("CollectImagesTask start:" + this.directory);
		}
		
		// prepare visitors
		Path root = Paths.get(directory);
		
		if (logger.isDebugEnabled()) {
			logger.debug(root);
		}
		
		EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
		
		try {
			Files.walkFileTree(root, opts, MAX_DEPTH, visitor);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (logger.isInfoEnabled() && imageService != null) {
			List<Image> images = imageService.get();
			logger.info("Collect End: Found " + images.size() + "images");
		}
	}

}
