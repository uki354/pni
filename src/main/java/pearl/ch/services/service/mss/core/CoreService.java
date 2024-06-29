package pearl.ch.services.service.mss.core;

import lombok.Data;
import pearl.ch.services.dao.mss.core.CoreDAO;
import pearl.ch.services.dto.mss.mssCore.DataForMail;
import pearl.ch.services.enums.TemplateState;
import pearl.ch.services.enums.ThreadState;
import pearl.ch.services.service.mss.email.EmailService;
import pearl.ch.services.service.mss.templates.TemplatesService;
import pearl.ch.services.service.mss.threads.ThreadsService;
import pearl.ch.services.service.mss.threads.ThreadsServiceImpl;

@Data
public class CoreService implements Runnable {

	private final EmailService emailService;
	private final TemplatesService templatesService;
	private final ThreadsService threadsService;

	private final CoreDAO coreDao;
	private final String threadId;
	private final int templateId;

	private boolean running = false;

	private final int sendRatePerSeconds = 45; // actually rate ist 50, but 5 for buffer
	
	public CoreService(String threadId, int templateId, CoreDAO newsletterDao, EmailService emailService,
			TemplatesService templatesService, ThreadsServiceImpl threadsServiceImpl) {
		this.threadId = threadId;
		this.templateId = templateId;
		this.coreDao = newsletterDao;
		this.emailService = emailService;
		this.templatesService = templatesService;
		this.threadsService = threadsServiceImpl;
	}

	@Override
	public void run() {
		running = true;
		coreDao.filling(templateId);

		while (running) {

			DataForMail dataForMail = coreDao.prepareDataForMail(templateId);

			if (dataForMail != null) {

				try {

					emailService.sendEmail(dataForMail.getMailTo(), dataForMail.getSubject(),
							dataForMail.getHtmlTemplate(), dataForMail.getMailFrom(), dataForMail.getReplyTo());

					coreDao.setSendState(dataForMail.getMailTo(), templateId, 1);

					Thread.sleep(1000 / sendRatePerSeconds);
				} catch (Exception e) {
					if (e instanceof InterruptedException) {
						running = false;
					} else {
						e.printStackTrace();
						coreDao.setSendState(dataForMail.getMailTo(), templateId, -1);
					}
				}

			} else {
				this.running = false;
				templatesService.updateTemplateState(templateId, TemplateState.FINISHED);
				threadsService.setThreadState(threadId, ThreadState.FINISHED);
				threadsService.deleteThreadMonitoring(threadId);
				threadsService.removeThread(threadId);
			}
		}
	}

}
