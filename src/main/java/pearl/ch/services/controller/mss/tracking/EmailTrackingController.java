package pearl.ch.services.controller.mss.tracking;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import lombok.RequiredArgsConstructor;
import pearl.ch.services.entity.mssdb.mss.pk.MssTrackingOpenPK;
import pearl.ch.services.service.mss.tracking.EmailTrackingService;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailTrackingController {

	private final EmailTrackingService emailDataService;

	/**
	 * Handles a user's click on an email, specifically for tracking email opens.
	 * Every email contains tracking pixel(image tag) that calls this method so we
	 * can track client's information who opened email, which browser do they use,
	 * device etc...
	 *
	 * @param clientUUId The unique identifier of the client who clicked the email.
	 * @param templateId The ID of the email template that was clicked.
	 * @param request    The HTTP servlet request containing the user-agent header.
	 * @return A ResponseEntity with HTTP status code 200 (OK).
	 */
	@GetMapping("/open")
	public ResponseEntity<?> open(@RequestParam String clientUUId, @RequestParam int templateId,
			HttpServletRequest request) {
		String userAgentHeader = request.getHeader("User-Agent");
		MssTrackingOpenPK pk = MssTrackingOpenPK.builder().clientUUId(clientUUId).templateId(templateId).build();

		if (emailDataService.getEmailTrackingOpen(pk) == null) {
			emailDataService.saveEmailDataOpen(clientUUId, userAgentHeader, templateId);
		} else {
			emailDataService.updateEmailDataOpen(pk);
		}
		return new ResponseEntity<>(HttpStatus.OK);

	}

	/**
	 * Redirects a user to a specified link and saves the click event data for
	 * tracking purposes. In email html template, we modify links so that they point
	 * to our server's so we can grab client's information.After that we redirect
	 * them to desired link.
	 * 
	 *
	 * @param clientUUId   The unique identifier of the client who clicked the link.
	 * @param templateId   The ID of the email template that contained the link.
	 * @param redirectLink The link to which the user will be redirected.
	 * @param request      The HTTP servlet request containing the user-agent
	 *                     header.
	 * @return RedirectView View objects that contains redirect
	 */
	@GetMapping("/openLink")
	public RedirectView openLink(@RequestParam String clientUUId, @RequestParam int templateId,
			@RequestParam String redirectLink, HttpServletRequest request) {

		emailDataService.collectEmailDataAsync(clientUUId, templateId, redirectLink, request);
		return new RedirectView(redirectLink);
	}

}
