package at.subera.fs.indexer.watchdog.listener;

import at.subera.fs.indexer.watchdog.service.WatchDirectoryService;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Listener Broker for the {@link WatchDirectoryService}
 */
public class WatchListenerBroker implements Watchable {
    private static Map<Integer, Watchable> listeners = new HashMap<>();

    public void register(Watchable listener) {
        listeners.put(listener.hashCode(), listener);
    }

    public void unregister(Watchable listener) {
        listeners.remove(listener);
    }

    public WatchListenerBroker() {
    }

    public WatchListenerBroker(Map<Integer, Watchable> map) {
        listeners = map;
    }

    public WatchListenerBroker(List<Watchable> list) {
        for (Watchable l : list) {
            register(l);
        }
    }

    @Override
    public void visitFile(Path child, WatchEvent.Kind<?> kind) {
        Iterator<Map.Entry<Integer, Watchable>> it = listeners.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Watchable> pairs = it.next();

            pairs.getValue().visitFile(child, kind);
        }
    }

    @Override
    public void visitDirectory(Path child, WatchEvent.Kind<?> kind) {
        Iterator<Map.Entry<Integer, Watchable>> it = listeners.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Watchable> pairs = it.next();

            pairs.getValue().visitDirectory(child, kind);
        }
    }
}
