package at.subera.memento.rest.service;

import java.util.List;

import at.subera.memento.stats.ThreadInfo;

public interface StatsService {
	public void startThread(Runnable r);
	public void endThread(Runnable r);
	public List<ThreadInfo> getThreads();
}
