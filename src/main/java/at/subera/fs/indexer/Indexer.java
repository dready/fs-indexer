package at.subera.fs.indexer;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;

import org.apache.log4j.Logger;

import at.subera.fs.indexer.visitor.IndexFileVisitor;

public class Indexer {
	private static final Logger logger = Logger.getLogger(Indexer.class);
	
	public static int MAX_DEPTH = 100;
	
	protected IndexFileVisitor<Path> visitor;

	public void setVisitor(IndexFileVisitor<Path> visitor) {
		this.visitor = visitor;
	}

	public void index(String directory) {
		if (logger.isInfoEnabled()) {
			logger.info("Indexer start:" + directory);
		}
		
		visitor.preIndex(directory);
		
		// prepare visitors
		Path root = Paths.get(directory);

		if (logger.isDebugEnabled()) {
			logger.debug(root);
		}

		EnumSet<FileVisitOption> opts = EnumSet
				.of(FileVisitOption.FOLLOW_LINKS);

		try {
			Files.walkFileTree(root, opts, MAX_DEPTH, visitor);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		visitor.postIndex(directory);
		
		if (logger.isInfoEnabled()) {
			logger.info("Indexer end:" + directory);
		}
	}
}
