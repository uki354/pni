package pearl.ch.services.service.mss.threads;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import lombok.RequiredArgsConstructor;
import pearl.ch.services.entity.mssdb.mss.ThreadsMonitoring;
import pearl.ch.services.service.mss.send.SendService;

@Service
@RequiredArgsConstructor
public final class ThreadRestoreService {

	private final SendService sendService;

	@Autowired
	@Qualifier("transactionManagerWrite")
	private PlatformTransactionManager transactionManager;

	@PersistenceContext(unitName = "mssWrite")
	private EntityManager entityManagerWrite;

	@PostConstruct
	@SuppressWarnings("unchecked")
	public void truncateThreadMonitoringTable() {
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

		try {
			DateTimeFormatter dft =  DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
			List<ThreadsMonitoring> threads = entityManagerWrite.createQuery("SELECT tm FROM ThreadsMonitoring tm")
					.getResultList();
			
			entityManagerWrite.createNativeQuery("DELETE FROM threads_monitoring WHERE 1=1").executeUpdate();
			
			transactionManager.commit(status);
			
			for (ThreadsMonitoring thread : threads) {
				if (thread.getScheduledDateTime() == null) {
					sendService.sendNewsletter(thread.getTemplate_id());
				} else {
					sendService.sendNewsletterAt(thread.getTemplate_id(), thread.getScheduledDateTime().format(dft),
							TimeZone.getTimeZone(ZoneId.systemDefault()).getID(), thread.getReapeatAfter());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(status);
		}
	}

}
