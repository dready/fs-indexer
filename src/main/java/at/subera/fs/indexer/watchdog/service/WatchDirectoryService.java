package at.subera.fs.indexer.watchdog.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.util.Map;

public interface WatchDirectoryService {
	public void register(Path dir) throws IOException;
	public void processEvents();
	public Map<WatchKey, Path> get();
}
