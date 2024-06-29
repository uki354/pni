package pearl.ch.services.entity.mssdb.mss;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pearl.ch.services.enums.ThreadState;

@Entity
@Data
@Table(name = "threads_monitoring")
@AllArgsConstructor
@NoArgsConstructor
public class ThreadsMonitoring {
	
	@Id
	@Column(name = "thread_id")
	private String thread_id;

	@Column(name = "template_id")
	private int template_id;
	
	@Column(name = "repeat_after")
	private int reapeatAfter;
	
	@Column(name = "scheduled_date_time")
	private LocalDateTime scheduledDateTime;	
	
	@Enumerated(EnumType.STRING)
	private ThreadState state;
	
	public ThreadsMonitoring(String thread_id, int template_id, ThreadState state) {
		this.thread_id = thread_id;
		this.template_id = template_id;		
		this.state = state;
	}

}
