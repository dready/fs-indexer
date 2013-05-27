package at.subera.fs.indexer.index.visitor;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import at.subera.fs.indexer.watchdog.service.WatchDirectoryService;

public class WatchDirVisitor extends SimpleFileVisitor<Path> {
	private WatchDirectoryService watchDirectoyService;
	
	public void setWatchDirectoryService(WatchDirectoryService watchDirectoryService) {
		this.watchDirectoyService = watchDirectoryService;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {
		watchDirectoyService.register(dir);
		return FileVisitResult.CONTINUE;
	}
}
