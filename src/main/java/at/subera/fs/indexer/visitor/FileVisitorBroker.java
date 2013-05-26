package at.subera.fs.indexer.visitor;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FileVisitorBroker<E> implements FileVisitor<Path> {
	private static Map<Integer, FileVisitor<Path>> visitors = new HashMap<Integer, FileVisitor<Path>>();
	
	public void register(FileVisitor<Path> visitor) {
		visitors.put(visitor.hashCode(), visitor);
	}
	
	public void unregister(FileVisitor<Path> visitor) {
		visitors.remove(visitor);
	}
	
	public FileVisitorBroker() {
	}
	
	public FileVisitorBroker(Map<Integer, FileVisitor<Path>> map) {
		visitors = map;
	}
	
	public FileVisitorBroker(List<FileVisitor<Path>> list) {
		for (FileVisitor<Path> v : list) {
			register(v);
		}
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {
		Iterator<Entry<Integer, FileVisitor<Path>>> it = visitors.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, FileVisitor<Path>> pairs = (Map.Entry<Integer, FileVisitor<Path>>)it.next();
			pairs.getValue().preVisitDirectory(dir, attrs);
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			throws IOException {
		Iterator<Entry<Integer, FileVisitor<Path>>> it = visitors.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, FileVisitor<Path>> pairs = (Map.Entry<Integer, FileVisitor<Path>>)it.next();
			pairs.getValue().visitFile(file, attrs);
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc)
			throws IOException {
		Iterator<Entry<Integer, FileVisitor<Path>>> it = visitors.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, FileVisitor<Path>> pairs = (Map.Entry<Integer, FileVisitor<Path>>)it.next();
			pairs.getValue().visitFileFailed(file, exc);
		}
		return FileVisitResult.TERMINATE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc)
			throws IOException {
		Iterator<Entry<Integer, FileVisitor<Path>>> it = visitors.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, FileVisitor<Path>> pairs = (Map.Entry<Integer, FileVisitor<Path>>)it.next();
			pairs.getValue().postVisitDirectory(dir, exc);
		}
		return FileVisitResult.CONTINUE;
	}
}
