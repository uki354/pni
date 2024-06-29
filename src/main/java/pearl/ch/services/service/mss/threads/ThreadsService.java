package pearl.ch.services.service.mss.threads;

import java.util.List;
import java.util.Map;

import pearl.ch.services.dto.mss.threads.ThreadsMonitorDTO;
import pearl.ch.services.entity.mssdb.mss.ThreadsMonitoring;
import pearl.ch.services.enums.ThreadState;

public interface ThreadsService {

	List<ThreadsMonitorDTO> getThreadsMonitor();
	
	Map<String, Thread> getThreads();

	boolean startThread(String threadId, int templateId);

	boolean stopThread(String threadId, int templateId);

	boolean setThreadState(String threadId, ThreadState state);
	
	boolean deleteThreadMonitoring(String id);
	
	void saveThreadMonitoring(ThreadsMonitoring thread);

	void addScheduledThread(String threadId, Thread thread);
	
	void removeThread(String threadId);	
	
	

}
