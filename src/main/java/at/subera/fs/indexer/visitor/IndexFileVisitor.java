package at.subera.fs.indexer.visitor;

public interface IndexFileVisitor<T> 
{
	public void preIndex(String directory);
	public void postIndex(String directory);
}
