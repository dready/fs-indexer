package at.subera.fs.indexer.watchdog.listener;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

public interface Watchable {
    public void visitFile(Path child, WatchEvent.Kind<?> kind);
    public void visitDirectory(Path child, WatchEvent.Kind<?> kind);
}
