package at.subera.memento.rest.service;

import java.io.IOException;
import java.nio.file.Path;

public interface WatchDirectoryService {
	public void register(Path dir) throws IOException;
	public void processEvents();
}
