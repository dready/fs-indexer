package at.subera.fs.indexer.watchdog.listener;

import at.subera.fs.indexer.watchdog.service.WatchDirectoryService;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * Listener for the {@link WatchDirectoryService}
 */
public interface Watchable {
    /**
     * is called if the {@link WatchDirectoryService} has catched a {@link WatchEvent} on a File
     *
     * @param child Filename
     * @param kind  {@link WatchEvent}
     */
    public void visitFile(Path child, WatchEvent.Kind<?> kind);

    /**
     * is called if the {@link WatchDirectoryService} has catched a {@link WatchEvent} on a Directory
     *
     * @param child Directory
     * @param kind  {@link WatchEvent}
     */
    public void visitDirectory(Path child, WatchEvent.Kind<?> kind);
}
