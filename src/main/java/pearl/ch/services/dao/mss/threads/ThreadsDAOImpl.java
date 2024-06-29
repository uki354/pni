package pearl.ch.services.dao.mss.threads;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import pearl.ch.services.config.annotations.ReadTransactional;
import pearl.ch.services.config.annotations.WriteTransactional;
import pearl.ch.services.dto.mss.threads.ThreadsMonitorDTO;
import pearl.ch.services.entity.mssdb.mss.ThreadsMonitoring;
import pearl.ch.services.enums.ThreadState;

@Repository
public class ThreadsDAOImpl implements ThreadsDAO {

	@PersistenceContext(unitName = "mssRead")
	private EntityManager entityManager;

	@PersistenceContext(unitName = "mssWrite")
	private EntityManager entityManagerWrite;

	@SuppressWarnings("unchecked")
	@Override
	@ReadTransactional
	public List<ThreadsMonitorDTO> getThreadsMonitor() {
		List<Object[]> resultList = entityManager
				.createNativeQuery("SELECT msst.name, tm.thread_id, msst.mss_template_id, " + "tm.state, "
						+ "COUNT(mts.template_id) AS total, "
						+ "SUM(CASE WHEN mts.state = 1 THEN 1 ELSE 0 END) AS done, tm.scheduled_date_time, "
						+ "SUM(CASE WHEN mts.state = -1 THEN 1 ELSE 0 END) as failed " + "FROM mss_templates msst "
						+ "JOIN threads_monitoring tm ON tm.template_id = msst.mss_template_id "
						+ "LEFT JOIN mss_to_send mts ON mts.template_id = msst.mss_template_id "
						+ "GROUP BY msst.name, tm.thread_id, msst.mss_template_id")
				.getResultList();

		List<ThreadsMonitorDTO> dtos = new ArrayList<>();

		for (Object[] result : resultList) {
			String name = (String) result[0];
			String threadId = (String) result[1];
			int mssTemplateId = (int) result[2];
			String state = (String) result[3];
			long total = ((Number) result[4]).longValue();
			long done = ((Number) result[5]).longValue();
			Timestamp dateTime = (Timestamp) result[6];
			long failed = ((Number) result[7]).longValue();

			LocalDateTime dLocalDateTime = dateTime != null ? dateTime.toLocalDateTime() : null;

			ThreadsMonitorDTO dto = new ThreadsMonitorDTO(name, threadId, mssTemplateId, state, done, failed, total,
					dLocalDateTime);
			dtos.add(dto);
		}

		return dtos;

	}

	@Override
	@WriteTransactional
	public void saveThreadMonitoring(ThreadsMonitoring thread) {
		entityManagerWrite.merge(thread);
	}

	@Override
	@WriteTransactional
	public boolean deleteThreadMonitoring(String id) {
		return entityManagerWrite.createQuery("DELETE FROM ThreadsMonitoring tm WHERE tm.thread_id = :id")
				.setParameter("id", id).executeUpdate() > 0;
	}

	@Override
	@WriteTransactional
	public boolean setThreadState(String threadId, ThreadState state) {
		return entityManagerWrite
				.createQuery("UPDATE ThreadsMonitoring tm SET tm.state = :state WHERE tm.thread_id = :threadId")
				.setParameter("state", state).setParameter("threadId", threadId).executeUpdate() > 0;
	}
}
