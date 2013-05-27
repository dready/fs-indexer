package at.subera.fs.indexer.watchdog.service;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

import at.subera.fs.indexer.watchdog.listener.Watchable;
import org.apache.log4j.Logger;

public class WatchDirectoryServiceImpl implements WatchDirectoryService {
	private final WatchService watcher;
	private final Map<WatchKey, Path> keys;
	private boolean trace;

	private static final Logger logger = Logger
			.getLogger(WatchDirectoryServiceImpl.class);

    protected Watchable listener;

	public WatchDirectoryServiceImpl() throws IOException {
		this.watcher = FileSystems.getDefault().newWatchService();
		this.keys = new HashMap<WatchKey, Path>();
	}

    public void setListener(Watchable listener) {
        this.listener = listener;
    }

    @Override
	public void register(Path dir) throws IOException {
		WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE,
				ENTRY_MODIFY);
		
		if (trace && logger.isInfoEnabled()) {
			Path prev = keys.get(key);
			if (prev == null) {
				logger.info("register: " + dir.toString());
			} else {
				if (!dir.equals(prev)) {
					logger.info(String.format("update: %s -> %s\n", prev, dir));
				}
			}
		}
		keys.put(key, dir);
	}

	@SuppressWarnings("unchecked")
	static <T> WatchEvent<T> cast(WatchEvent<?> event) {
		return (WatchEvent<T>) event;
	}

	@Override
	/**
	 * processEvents
	 * 
	 * catches changes of directories
	 */
	public void processEvents() {
		// wait for key to be signalled
		WatchKey key;
		try {
			key = watcher.take();
		} catch (InterruptedException x) {
			return;
		}

		Path dir = keys.get(key);
		if (dir == null) {
			if (logger.isInfoEnabled()) {
				logger.info("Error: WatchKey not recognized!!");
			}
			return;
		}

		for (WatchEvent<?> event : key.pollEvents()) {
			Kind<?> kind = event.kind();

			// TBD - provide example of how OVERFLOW event is handled
			if (kind == OVERFLOW) {
				continue;
			}

			// Context for directory entry event is the file name of entry
			WatchEvent<Path> ev = cast(event);
			Path name = ev.context();
			Path child = dir.resolve(name);

			// print out event
			// System.out.format("%s: %s\n", event.kind().name(), child);
			if (logger.isInfoEnabled()) {
				logger.info(String.format("%s: %s\n", event.kind().name(),
						child));
			}

            if (listener == null) {
                continue;
            }

			if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
				listener.visitDirectory(child, kind);
			} else {
				listener.visitFile(child, kind);
			}
		}

		// reset key and remove from set if directory no longer accessible
		boolean valid = key.reset();
		if (!valid) {
			keys.remove(key);
		}
	}

	@Override
	public Map<WatchKey, Path> get() {
		return keys;
	}
}
