package pearl.ch.services.service.mss.threads;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import pearl.ch.services.dao.mss.core.CoreDAO;
import pearl.ch.services.dao.mss.threads.ThreadsDAO;
import pearl.ch.services.dto.mss.threads.ThreadsMonitorDTO;
import pearl.ch.services.entity.mssdb.mss.ThreadsMonitoring;
import pearl.ch.services.enums.TemplateState;
import pearl.ch.services.enums.ThreadState;
import pearl.ch.services.service.mss.core.CoreService;
import pearl.ch.services.service.mss.email.EmailService;
import pearl.ch.services.service.mss.templates.TemplatesService;

@Data
@Service
@RequiredArgsConstructor
public class ThreadsServiceImpl implements ThreadsService {

	private final CoreDAO coreDao;
	private final ThreadsDAO threadsDAO;
	private final EmailService emailService;	
	private final TemplatesService templatesService;

	private Map<String, Thread> threads = new ConcurrentHashMap<>();
	private Map<String, Thread> scheduledThreads = new ConcurrentHashMap<>();

	@Override
	public List<ThreadsMonitorDTO> getThreadsMonitor() {
		return threadsDAO.getThreadsMonitor();
	}

	@Override
	public boolean startThread(String threadId, int templateId) {
		CoreService newsletterSender = new CoreService(threadId, templateId, coreDao, emailService, templatesService,
				this);
		Thread thread = new Thread(newsletterSender);
		try {
			thread.start();
			threads.put(threadId, thread);
			saveThreadMonitoring(new ThreadsMonitoring(threadId, templateId, ThreadState.RUNNING));

			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return newsletterSender.isRunning();
	}

	@Override
	public boolean stopThread(String threadId, int templateId) {

		boolean result = false;

		Thread newsletterSender = threads.get(threadId);
		Thread scheduledThread = scheduledThreads.get(threadId);
		
		setThreadState(threadId, ThreadState.STOPPED);
		templatesService.updateTemplateState(templateId, TemplateState.STOPPED);

		if (newsletterSender != null) {
			threadsDAO.deleteThreadMonitoring(threadId);
			threads.remove(threadId);
			result = true;
			newsletterSender.interrupt();
		}

		if (scheduledThread != null) {
			threadsDAO.deleteThreadMonitoring(threadId);
			scheduledThreads.remove(threadId);
			result = true;
			scheduledThread.interrupt();
		} 

		return result;

	}

	@Override
	public void saveThreadMonitoring(ThreadsMonitoring thread) {
		threadsDAO.saveThreadMonitoring(thread);

	}

	@Override
	public void addScheduledThread(String threadId, Thread thread) {
		scheduledThreads.put(threadId, thread);

	}

	@Override
	public boolean deleteThreadMonitoring(String id) {
		return threadsDAO.deleteThreadMonitoring(id);
	}

	@Override
	public void removeThread(String threadId) {
		scheduledThreads.remove(threadId);
		threads.remove(threadId);
		
	}

	@Override
	public boolean setThreadState(String threadId, ThreadState state) {
		return threadsDAO.setThreadState(threadId, state);
		
	}
	
	
	

}
