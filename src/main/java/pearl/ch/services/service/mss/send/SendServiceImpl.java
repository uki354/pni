package pearl.ch.services.service.mss.send;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pearl.ch.services.entity.mssdb.mss.MssTemplates;
import pearl.ch.services.entity.mssdb.mss.ThreadsMonitoring;
import pearl.ch.services.entity.mssdb.mss.fillers.MSSFillers;
import pearl.ch.services.enums.TemplateState;
import pearl.ch.services.enums.ThreadState;
import pearl.ch.services.service.mss.fillers.FillersService;
import pearl.ch.services.service.mss.templates.TemplatesService;
import pearl.ch.services.service.mss.threads.ThreadsService;

@Service
@RequiredArgsConstructor
public class SendServiceImpl implements SendService {

	private final FillersService fillersService;
	private final ThreadsService threadsService;
	private final TemplatesService templatesService;

	@Override
	public List<MssTemplates> getTemplatesByState(TemplateState state) {
		return templatesService.getTemplatesByState(state);
	}

	@Override
	public MssTemplates getTemplate(int templateId) {
		return templatesService.getTemplateById(templateId);
	}

	@Override
	public List<MssTemplates> getTemplatesByIds(List<Integer> ids) {
		return templatesService.getTemplatesByIds(ids);
	}

	@Override
	public List<MSSFillers> getFillers() {
		return fillersService.getAllFillers();
	}

	@Override
	public boolean sendNewsletter(int templateId) {
		return threadsService.startThread(UUID.randomUUID().toString(), templateId);
		
	}

	/**
	 * Asynchronously sends a newsletter at the specified execution time based on
	 * the provided parameters.
	 * <p>
	 * The method schedules the newsletter to be sent at the given execution time,
	 * taking into account the provided time zone. The method calculates the delay
	 * between the current time and the execution time, then asynchronously sleeps
	 * the current thread for the calculated delay period.
	 * <p>
	 * Upon reaching the execution time, the method invokes the 'sendNewsletter'
	 * method from the mssCoreDAO to send the newsletter with the specified template
	 * ID.
	 * <p>
	 * If provided execution time is negative (in the past) New execution time will be calculated.
	 * based on repeatAfter parameter. First valid execution time will be used.
	 * If the repeatAfter is not provided and execution time is in the past. The task will run
	 * immediately only once.
	 *
	 * @param templateId The ID of the template for the newsletter to be sent.
	 * @param timestamp  A string representation of the execution time for the
	 *                   newsletter in "dd-MM-yyyy HH:mm" format.
	 * @param timeZone   The time zone in which the execution time is specified.
	 * @return True if the newsletter was scheduled successfully for sending,
	 *         otherwise false.
	 */
	@Override
	@Async
	public void sendNewsletterAt(int templateId, String timestamp, String timeZone, int repeatAfter) {
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		LocalDateTime executionTime = LocalDateTime.parse(timestamp, formatter);

		ZoneId zoneId = ZoneId.of(timeZone);
		ZoneOffset offset = zoneId.getRules().getOffset(Instant.now());

		LocalDateTime adjustedExecutionTime = executionTime.atOffset(offset).toLocalDateTime();

		Duration delay = Duration.between(currentTime, adjustedExecutionTime);
		
		while (repeatAfter > 0 && delay.isNegative()) {
			adjustedExecutionTime = adjustedExecutionTime.plusDays(repeatAfter);
			delay = Duration.between(currentTime, adjustedExecutionTime);
		}

		try {
			String id = UUID.randomUUID().toString();

			if (!delay.isNegative()) {
				threadsService.addScheduledThread(id, Thread.currentThread());
				threadsService.saveThreadMonitoring(
						new ThreadsMonitoring(id, templateId, repeatAfter, adjustedExecutionTime, ThreadState.SCHEDULED));
				Thread.sleep(delay.toMillis());
			}
			
			threadsService.startThread(id,templateId);

			if (repeatAfter > 0) {
				LocalDateTime nextExecutionTime = adjustedExecutionTime.plusMinutes(repeatAfter);
				String nextTimestamp = nextExecutionTime.format(formatter);

				sendNewsletterAt(templateId, nextTimestamp, timeZone, repeatAfter);
			}

		} catch (Exception e) {
			if (!(e instanceof InterruptedException))
				e.printStackTrace();

		}

	}
}
