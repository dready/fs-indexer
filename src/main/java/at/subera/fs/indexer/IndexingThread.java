package at.subera.fs.indexer;


public class IndexingThread implements Runnable {
	protected String directory;
	
	protected Indexer indexer;
	
	public void setIndexer(Indexer indexer) {
		this.indexer = indexer;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public IndexingThread(String directory) {
		setDirectory(directory);
	}

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
//		if (statsService != null) {
//			statsService.startThread(this);
//		}
	
		indexer.index(directory);
		
//		if (statsService != null) {
//			statsService.endThread(this);
//		}
//		
//		if (logger.isInfoEnabled() && imageService != null) {
//			List<Image> images = imageService.get();
//			logger.info("Collect End: Found " + images.size() + "images");
//		}
	}

}
