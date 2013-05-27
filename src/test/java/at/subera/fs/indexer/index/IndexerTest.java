package at.subera.fs.indexer.index;

import static org.junit.Assert.*;

import java.net.URL;

import org.junit.Test;

public class IndexerTest {

	@Test
	public final void testSetVisitor() {
		fail("Not yet implemented");
	}

	@Test
	public final void testSetListener() {
		fail("Not yet implemented");
	}

	@Test
	public final void testIndex() {
		URL resource = IndexerTest.class.getResource("indexer/testfile.txt");
		String directory = resource.getPath();
		
		Indexer indexer = new Indexer();
		indexer.index(directory);
	}

}
