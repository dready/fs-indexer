package at.subera.fs.indexer.index.listener;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ListenerBroker<E> implements IndexListenable<Path>
{
    private static Map<Integer, IndexListenable<Path>> visitors = new HashMap<Integer, IndexListenable<Path>>();

    public void register(IndexListenable<Path> visitor) {
        visitors.put(visitor.hashCode(), visitor);
    }

    public void unregister(IndexListenable<Path> visitor) {
        visitors.remove(visitor);
    }

    public ListenerBroker() {
    }

    public ListenerBroker(Map<Integer, IndexListenable<Path>> map) {
        visitors = map;
    }

    public ListenerBroker(List<IndexListenable<Path>> list) {
        for (IndexListenable<Path> v : list) {
            register(v);
        }
    }

    @Override
    public void preIndex(String directory) {
        Iterator<Map.Entry<Integer, IndexListenable<Path>>> it = visitors.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, IndexListenable<Path>> pairs = (Map.Entry<Integer, IndexListenable<Path>>)it.next();

            pairs.getValue().preIndex(directory);
        }
    }

    @Override
    public void postIndex(String directory) {
        Iterator<Map.Entry<Integer, IndexListenable<Path>>> it = visitors.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, IndexListenable<Path>> pairs = (Map.Entry<Integer, IndexListenable<Path>>)it.next();

            pairs.getValue().postIndex(directory);
        }
    }
}
