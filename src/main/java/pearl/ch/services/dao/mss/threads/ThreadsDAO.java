package pearl.ch.services.dao.mss.threads;

import java.util.List;

import pearl.ch.services.dto.mss.threads.ThreadsMonitorDTO;
import pearl.ch.services.entity.mssdb.mss.ThreadsMonitoring;
import pearl.ch.services.enums.ThreadState;

public interface ThreadsDAO {

	List<ThreadsMonitorDTO> getThreadsMonitor();
		
	void saveThreadMonitoring(ThreadsMonitoring thread);
	
	boolean deleteThreadMonitoring(String id);
	
	boolean setThreadState(String threadId, ThreadState state);

	
}
