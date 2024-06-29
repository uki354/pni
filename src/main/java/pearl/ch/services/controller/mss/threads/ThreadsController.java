package pearl.ch.services.controller.mss.threads;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import pearl.ch.services.dto.mss.threads.ThreadsMonitorDTO;
import pearl.ch.services.service.mss.threads.ThreadsService;

@RestController
@CrossOrigin
@RequestMapping("mss/threads/")
@RequiredArgsConstructor
public class ThreadsController {

	@Autowired
	private ThreadsService threadsService;

	@GetMapping("/getThreadMonitors")
	public List<ThreadsMonitorDTO> getThreadMonitors() {
		return threadsService.getThreadsMonitor();
	}

	@PostMapping("/stopThread")
	public @ResponseBody boolean stopThread(@RequestBody Map<String, ?> threadData) {

		JSONObject jsonThreadData = new JSONObject(threadData);

		return threadsService.stopThread(jsonThreadData.getAsString("threadId"),
				Integer.parseInt(jsonThreadData.getAsString("templateId")));

	}

}
