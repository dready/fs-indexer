package at.subera.memento.sort;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import at.subera.memento.rest.bean.File;

public class PathComparatorTest {
	static File one;
	static File two;
	static File three;

	@BeforeClass
	static public void beforeClass() {
		one = new File("1", "a", "one", 0, null, null);
		two = new File("2", "b", "two", 0, null, null);
		three = new File("3", "c", "three", 0, null, null);
	}
	
	@Test
	public final void testCompareSame() {
		PathComparator p = new PathComparator();
		
		assertEquals(0, p.compare(one, one));
	}

	@Test
	public final void testCompareNegative() {
		PathComparator p = new PathComparator();
		
		assertEquals(-1, p.compare(one, two));
	}
	
	@Test
	public final void testComparePositive() {
		PathComparator p = new PathComparator();
		
		assertEquals(1, p.compare(two, one));
	}
	
	@Test
	public final void testCompare() {
		List<File> files = new ArrayList<File>();
		files.add(two);
		files.add(one);
		files.add(three);
		
		Collections.sort(files, new PathComparator());
		
		List<File> expected = new ArrayList<File>();
		expected.add(one);
		expected.add(two);
		expected.add(three);
		
		assertEquals(expected, files);
	}
}
