package at.subera.fs.indexer;

import java.io.IOException;
import java.nio.file.*;
import java.util.EnumSet;

import at.subera.fs.indexer.listener.IndexListenable;
import org.apache.log4j.Logger;

public class Indexer {
	private static final Logger logger = Logger.getLogger(Indexer.class);
	
	public static int MAX_DEPTH = 100;
	
	protected FileVisitor<Path> visitor;

    protected IndexListenable<Path> listener;

	public void setVisitor(FileVisitor<Path> visitor) {
		this.visitor = visitor;
	}

    public void setListener(IndexListenable<Path> listener) {
        this.listener = listener;
    }

    public void index(String directory) {
		if (logger.isInfoEnabled()) {
			logger.info("Indexer start:" + directory);
		}

        if (listener != null) {
            listener.preIndex(directory);
        }

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

        if (listener != null) {
            listener.postIndex(directory);
        }

		if (logger.isInfoEnabled()) {
			logger.info("Indexer end:" + directory);
		}
	}
}
