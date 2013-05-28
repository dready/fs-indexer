package at.subera.fs.indexer.watchdog.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.util.Map;

/**
 * Basic watchdog Service
 */
public interface WatchDirectoryService {
    /**
     * register the watchdog to the directory
     *
     * @param dir the Directory to register
     * @throws IOException
     */
    public void register(Path dir) throws IOException;

    /**
     * process any Event found
     */
    public void processEvents();

    /**
     * the registered Directories
     *
     * @return the registered Directories
     */
    public Map<WatchKey, Path> get();
}
