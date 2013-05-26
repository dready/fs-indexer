package at.subera.fs.indexer;

import org.apache.log4j.Logger;

import at.subera.memento.rest.service.WatchDirectoryService;

public class WatchDirectoriesTask implements Runnable {
	protected WatchDirectoryService watchDirectoryService;
	
	private static final Logger logger = Logger.getLogger(WatchDirectoriesTask.class);
	
	Thread watcher;
	
	public void setWatchDirectoryService(WatchDirectoryService watchDirectoryService) {
		this.watchDirectoryService = watchDirectoryService;
	}
	
	public void init() {
		watcher = new Thread(this);
		watcher.setPriority(Thread.MIN_PRIORITY);
		watcher.start();
	}
	
	@SuppressWarnings("deprecation")
	public void cleanup() {
		watcher.stop();
	}
	
	@Override
	public void run() {
		if (logger.isInfoEnabled()) {
			logger.info("WatchDirectoryTask start");
		}
		
		for (;;) {
			watchDirectoryService.processEvents();
		}
	}

}
