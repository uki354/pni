package pearl.ch.services.dto.mss.templates;

import lombok.Data;

@Data
public class TemplatesDTO {
	
    private int mss_template_id;

	private String language_name;

	private String shop_name;

	private String mss_type_name;

	private String name;

	private String html_template;

	private String online_version_url;

	private String state;

}
