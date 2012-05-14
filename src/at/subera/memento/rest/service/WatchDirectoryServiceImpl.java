package at.subera.memento.rest.service;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

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

import org.apache.log4j.Logger;

import at.subera.memento.files.CollectImagesTask;

public class WatchDirectoryServiceImpl implements WatchDirectoryService {
	private final WatchService watcher;
	private final Map<WatchKey,Path> keys;
	private final boolean recursive;
	private boolean trace;
	
	private static final Logger logger = Logger.getLogger(WatchDirectoryServiceImpl.class);
	
	public WatchDirectoryServiceImpl(boolean recursive) throws IOException {
		this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey,Path>();
        this.recursive = recursive;
	}
	
	public WatchDirectoryServiceImpl() throws IOException {
		this(false);
	}

	@Override
	public void register(Path dir) throws IOException {
		WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                if (logger.isInfoEnabled()) {
        			logger.info("register: " + dir.toString());
        		}
            } else {
                if (!dir.equals(prev)) {
                    System.out.format("update: %s -> %s\n", prev, dir);
                    if (logger.isInfoEnabled()) {
            			logger.info(String.format("update: %s -> %s\n", prev, dir));
            		}
                }
            }
        }
        keys.put(key, dir);
	}
	
	@SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }

	@Override
	/**
	 * processEvents
	 * 
	 * catches changes of directories
	 */
	public void processEvents() {
//        for (;;) {

            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
//                System.err.println("WatchKey not recognized!!");
                if (logger.isInfoEnabled()) {
        			logger.info("Error: WatchKey not recognized!!");
        		}
//                continue;
                return;
            }

            for (WatchEvent<?> event: key.pollEvents()) {
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
                //System.out.format("%s: %s\n", event.kind().name(), child);
                if (logger.isInfoEnabled()) {
        			logger.info(String.format("%s: %s\n", event.kind().name(), child));
        		}

                // if directory is created, and watching recursively, then
                // register it and its sub-directories
                if (recursive && (kind == ENTRY_CREATE)) {
                 //   try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                        	CollectImagesTask task = new CollectImagesTask(child.toString());
                        	task.init();
                            //registerAll(child);
                        }
                 //   } catch (IOException x) {
                        // ignore to keep sample readbale
                 //   }
                }
                
                if (kind == ENTRY_MODIFY) {
                	if (logger.isInfoEnabled()) {
            			logger.info("Modified Directory " + child.toString());
            		}
                	
                }
            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
//                    break;
//                    throw new RuntimeException("all directories are inaccessible");
                    return; // just return, thread should go on
                }
            }
//        }
	}
}
