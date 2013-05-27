package at.subera.fs.indexer.index.listener;

public interface IndexListenable<T>
{
	public void preIndex(String directory);
	public void postIndex(String directory);
}
