package at.subera.fs.indexer.watchdog.listener;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WatchListenerBroker implements Watchable<Path>
{
    private static Map<Integer, Watchable<Path>> listeners = new HashMap<Integer, Watchable<Path>>();

    public void register(Watchable<Path> listener) {
        listeners.put(listener.hashCode(), listener);
    }

    public void unregister(Watchable<Path> listener) {
        listeners.remove(listener);
    }

    public WatchListenerBroker() {}

    public WatchListenerBroker(Map<Integer, Watchable<Path>> map) {
        listeners = map;
    }

    public WatchListenerBroker(List<Watchable<Path>> list) {
        for (Watchable<Path> l : list) {
            register(l);
        }
    }

    @Override
    public void visitFile(Path child, WatchEvent.Kind<?> kind) {
        Iterator<Map.Entry<Integer, Watchable<Path>>> it = listeners.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Watchable<Path>> pairs = (Map.Entry<Integer, Watchable<Path>>)it.next();

            pairs.getValue().visitFile(child, kind);
        }
    }

    @Override
    public void visitDirectory(Path child, WatchEvent.Kind<?> kind) {
        Iterator<Map.Entry<Integer, Watchable<Path>>> it = listeners.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Watchable<Path>> pairs = (Map.Entry<Integer, Watchable<Path>>)it.next();

            pairs.getValue().visitDirectory(child, kind);
        }
    }
}
