package at.subera.memento.stats;

import java.util.Date;

public class ThreadInfo {
	private String id;
	private Date start;
	private Date end;
	
	public ThreadInfo() {}
	
	public ThreadInfo(String id) {
		this.id = id;
		this.start = new Date();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}

	public boolean isRunning() {
		if (start == null) {
			return false;
		}
		return end == null;
	}
	
	public long getDuration() {
		if (isRunning()) {
			throw new RuntimeException("Thread " + id + " is still running!");
		}
		return end.getTime() - start.getTime();
	}
}
