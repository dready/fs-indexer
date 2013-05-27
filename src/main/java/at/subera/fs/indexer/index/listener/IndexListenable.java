package at.subera.fs.indexer.index.listener;

public interface IndexListenable {
	public void preIndex(String directory);
	public void postIndex(String directory);
}
