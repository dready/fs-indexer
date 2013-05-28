package at.subera.fs.indexer.index;

import at.subera.fs.indexer.index.listener.IndexListenable;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.util.EnumSet;

/**
 * Indexing Class
 * walks through all Files and Directories with the injected visitor {@link FileVisitor}
 */
public class Indexer {
    private static final Logger logger = Logger.getLogger(Indexer.class);

    public static int MAX_DEPTH = 100;

    protected FileVisitor<Path> visitor;

    protected IndexListenable listener;

    public void setVisitor(FileVisitor<Path> visitor) {
        this.visitor = visitor;
    }

    public void setListener(IndexListenable listener) {
        this.listener = listener;
    }

    /**
     * Indexes all files and subdirectories from the directory parameter
     *
     * @param directory start point of indexing process
     */
    public void index(String directory) {
        logger.info("Indexer start:" + directory);

        if (listener != null) {
            listener.preIndex(directory);
        }

        // prepare visitors
        Path root = Paths.get(directory);
        logger.debug(root);

        EnumSet<FileVisitOption> opts = EnumSet
                .of(FileVisitOption.FOLLOW_LINKS);

        if (visitor == null) {
            logger.error("No visitor defined");
            return;
        }

        try {
            Files.walkFileTree(root, opts, MAX_DEPTH, visitor);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (listener != null) {
            listener.postIndex(directory);
        }

        logger.info("Indexer end:" + directory);
    }
}
