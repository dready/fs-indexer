package at.subera.memento.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.log4j.Logger;

import at.subera.memento.rest.bean.Image;
import at.subera.memento.rest.service.ImageService;
import at.subera.memento.rest.service.WatchDirectoryService;

public class CollectImagesTask implements Runnable {
	protected String directory;
	protected ImageService imageService;
	protected WatchDirectoryService watchDirectoryService;
	
	protected ImageFileVisitor imageVisitor;
	protected WatchDirVisitor dirVisitor;
	
	public void setImageFileVisitor(ImageFileVisitor imageVisitor) {
		this.imageVisitor = imageVisitor;
	}
	
	public void setDirVisitor(WatchDirVisitor dirVisitor) {
		this.dirVisitor = dirVisitor;
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
	
	public void setImageVisitor(ImageFileVisitor imageVisitor) {
		this.imageVisitor = imageVisitor;
	}

	public void setWatchDirectoryService(WatchDirectoryService watchDirectoryService) {
		this.watchDirectoryService = watchDirectoryService;
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
		CompositeFileVisitor<Path> visitor = new CompositeFileVisitor<Path>();
		visitor.register(imageVisitor);
		visitor.register(dirVisitor);
		
		Path root = Paths.get(directory);
		
		if (logger.isDebugEnabled()) {
			logger.debug(root);
		}
		
		try {
			Files.walkFileTree(root, visitor);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (logger.isInfoEnabled()) {
			List<Image> images = imageService.get();
			logger.info("Collect End: Found " + images.size() + "images");
		}
	}

}
