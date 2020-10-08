package scot.ianmacdonald.cakemgr.webapp.controller;

/**
 * A POJO representing a message to be returned to the client
 * @author ian.macdonald@ianmacdonald.scot
 */
public class CakeServletMessage {
	
	private String message;
	
	public CakeServletMessage(final String message) {
		
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
