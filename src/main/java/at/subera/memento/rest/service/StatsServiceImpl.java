package at.subera.memento.rest.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.subera.memento.stats.ThreadInfo;

public class StatsServiceImpl implements StatsService {
	Map<Integer, ThreadInfo> threads = new HashMap<Integer, ThreadInfo>();

	@Override
	public void startThread(Runnable r) {
		ThreadInfo thread = new ThreadInfo(r.getClass().toString());
		threads.put(r.hashCode(), thread);
	}

	@Override
	public void endThread(Runnable r) {
		if (!threads.containsKey(r.hashCode())) {
			throw new RuntimeException("thread " + r + " not found!");
		}
		ThreadInfo thread = threads.get(r.hashCode());
		thread.setEnd(new Date());
	}

	@Override
	public List<ThreadInfo> getThreads() {
		List<ThreadInfo> ret = new ArrayList<ThreadInfo>(threads.values());
		return ret;
	}
}
