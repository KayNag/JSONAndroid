package kay.zendesk.android.data;

public class ZendeskData {

	private String subject;
	private String ticket_no;
	private String imageUrl;
	private String ticket_status;
	private String description;

	public ZendeskData(String subject, String number, String imageUrl,
			String status, String desc) {
		super();
		this.subject = subject;
		this.ticket_no = number;
		this.imageUrl = imageUrl;
		this.ticket_status = status;
		this.description = desc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTicketno() {
		return ticket_no;
	}

	public void setTicketno(String number) {
		this.ticket_no = number;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getTicketstatus() {
		return ticket_status;
	}

	public void setTicketstatus(String status) {
		this.ticket_status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}

}
