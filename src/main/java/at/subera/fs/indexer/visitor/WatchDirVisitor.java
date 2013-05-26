package at.subera.fs.indexer.visitor;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import at.subera.memento.rest.service.WatchDirectoryService;

public class WatchDirVisitor extends SimpleFileVisitor<Path> {
	private WatchDirectoryService watchDirectoyService;
	
	public void setWatchDirectoyService(WatchDirectoryService watchDirectoyService) {
		this.watchDirectoyService = watchDirectoyService;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {
		watchDirectoyService.register(dir);
		return FileVisitResult.CONTINUE;
	}
}
