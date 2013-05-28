package at.subera.fs.indexer.index.listener;

/**
 * Listener Interface for {@link at.subera.fs.indexer.index.Indexer}
 */
public interface IndexListenable {
    /**
     * Called right after start before Indexing has been started
     * @param directory the directory where the Index has been started
     */
	public void preIndex(String directory);

    /**
     * Called after Indexing has been finished, before Indexer is closed.
     * @param directory the directory where the Index has been finished
     */
	public void postIndex(String directory);
}
