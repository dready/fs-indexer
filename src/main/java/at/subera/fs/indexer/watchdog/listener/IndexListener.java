package at.subera.fs.indexer.watchdog.listener;

import at.subera.fs.indexer.index.Indexer;
import at.subera.fs.indexer.index.IndexingThread;
import org.apache.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class IndexListener implements Watchable {
    private final boolean recursive;

    private static final Logger logger = Logger
            .getLogger(IndexListener.class);

    protected Indexer indexer;

    public IndexListener(boolean recursive) {
        this.recursive = recursive;
    }

    public void setIndexer(Indexer indexer) {
        this.indexer = indexer;
    }

    @Override
    public void visitFile(Path child, WatchEvent.Kind<?> kind) {
        // do nothing
    }

    @Override
    public void visitDirectory(Path child, WatchEvent.Kind<?> kind) {
        // if directory is created, and watching recursively, then
        // register it and its sub-directories
        if (recursive && (kind == ENTRY_CREATE)) {
            IndexingThread task = new IndexingThread(child.toString());
            task.setIndexer(indexer);
            task.init();
        }

        if (kind == ENTRY_MODIFY) {
            if (logger.isInfoEnabled()) {
                logger.info("Modified Directory " + child.toString());
            }
            IndexingThread task = new IndexingThread(child.toString());
            task.setIndexer(indexer);
            task.init();
        }
    }
}
