package at.subera.fs.indexer.index.listener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ListenerBroker implements IndexListenable {
    private static Map<Integer, IndexListenable> visitors = new HashMap<>();

    public void register(IndexListenable visitor) {
        visitors.put(visitor.hashCode(), visitor);
    }

    public void unregister(IndexListenable visitor) {
        visitors.remove(visitor);
    }

    public ListenerBroker() {
    }

    public ListenerBroker(Map<Integer, IndexListenable> map) {
        visitors = map;
    }

    public ListenerBroker(List<IndexListenable> list) {
        for (IndexListenable v : list) {
            register(v);
        }
    }

    @Override
    public void preIndex(String directory) {
        Iterator<Map.Entry<Integer, IndexListenable>> it = visitors.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, IndexListenable> pairs = it.next();

            pairs.getValue().preIndex(directory);
        }
    }

    @Override
    public void postIndex(String directory) {
        Iterator<Map.Entry<Integer, IndexListenable>> it = visitors.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, IndexListenable> pairs = it.next();

            pairs.getValue().postIndex(directory);
        }
    }
}
