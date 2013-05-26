package at.subera.fs.indexer.visitor;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import at.subera.memento.util.Properties;

public class Filter {
	protected Properties properties = new Properties();
	
	protected List<PathMatcher> denyMatcher;
	protected List<PathMatcher> allowMatcher;
	
	private static final Logger logger = Logger
			.getLogger(Filter.class);
	
	public Filter() {
		denyMatcher = setMatcher(Properties.PROPERTY_PATTERN_DENY);
		allowMatcher = setMatcher(Properties.PROPERTY_PATTERN_ALLOW);
	}
	
	protected List<PathMatcher> setMatcher(String property) {
		List<PathMatcher> matcher = new ArrayList<PathMatcher>();
		if (!properties.get().containsKey(property)) {
			return matcher;
		}
		return getPathMatcher(properties.get().getProperty(property));
	}
	
	protected List<PathMatcher> getPathMatcher(String prop) {
		List<PathMatcher> ret = new ArrayList<PathMatcher>();
		String[] patterns = prop.split(";");
		for (String pattern : patterns) {
			ret.add(FileSystems.getDefault().getPathMatcher("glob:" + pattern));
		}
		return ret;
	}
	
	public boolean matchPath(Path path) {
		if (denyMatcher.size() > 0 && matchFile(denyMatcher, path)) {
			if (logger.isInfoEnabled()) {
				logger.info("DenyMatcher matched: " + path.toString());
			}
			return false;
		}
		if (allowMatcher.size() > 0 && !matchFile(allowMatcher, path)) {
			if (logger.isInfoEnabled()) {
				logger.info("AllowMatcher did not match: " + path.toString());
			}
			return false;
		}
		return true;
	}
	
	protected boolean matchFile(PathMatcher matcher, Path file) {
		if (matcher.matches(file)) {
			return true;
		}
		return false;
	}

	protected boolean matchFile(List<PathMatcher> matcher, Path file) {
		for (PathMatcher match : matcher) {
			if (matchFile(match, file)) {
				return true;
			}
		}
		return false;
	}
}
