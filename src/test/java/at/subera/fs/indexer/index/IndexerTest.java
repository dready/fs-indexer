package at.subera.fs.indexer.index;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import at.subera.fs.indexer.index.listener.IndexListenable;
import org.junit.Test;

public class IndexerTest {
    public String getTestDirectory() {
        URL resource = IndexerTest.class.getResource("/indexer/");
        return resource.getPath();
    }

	@Test
	public final void testSetListener() {
        String directory = getTestDirectory();

        SimpleFileVisitor<Path> visitor = new SimpleFileVisitor<Path>(){};

        final String[] preIndex = {""};
        final String[] postIndex = {""};

        IndexListenable listener = new IndexListenable() {
            @Override
            public void preIndex(String directory) {
                preIndex[0] = directory;
            }

            @Override
            public void postIndex(String directory) {
                postIndex[0] = directory;
            }
        };

        Indexer indexer = new Indexer();
        indexer.setVisitor(visitor);
        indexer.setListener(listener);
        indexer.index(directory);

        assertEquals("pre Index should be the same", directory, preIndex[0]);
        assertEquals("post Index should be the same", directory, postIndex[0]);
	}

	@Test
	public final void testIndex() {
		String directory = getTestDirectory();

        final int[] countPreVisitDirectory = {0};
        final int[] countVisitFile = {0};
        final int[] countVisitFileFailed = {0};
        final int[] countPostVisitDirectory = {0};

        FileVisitor<Path> visitor = new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                countPreVisitDirectory[0]++;
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                countVisitFile[0]++;
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                countVisitFileFailed[0]++;
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                countPostVisitDirectory[0]++;
                return FileVisitResult.CONTINUE;
            }
        };
		
		Indexer indexer = new Indexer();
        indexer.setVisitor(visitor);
		indexer.index(directory);

        assertEquals("count preVisitDirectory", 1, countPreVisitDirectory[0]);
        assertEquals("count postVisitDirectory", 1, countPostVisitDirectory[0]);
        assertEquals("count visitFile", 1, countVisitFile[0]);
        assertEquals("count visitFileFailed", 0, countVisitFileFailed[0]);
	}

}
