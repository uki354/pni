package pearl.ch.services.dto.mss.threads;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ThreadsMonitorDTO {
	
	private String name;
	private String threadId;
	private int templateId;
	private String state;
	private long done;
	private long failed;
	private long total;
	private LocalDateTime scheduledDateTime;
	

}
