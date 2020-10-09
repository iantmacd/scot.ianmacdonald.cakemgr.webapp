package scot.ianmacdonald.cakemgr.webapp.controller;

/**
 * A POJO representing a Java Exception to be returned to the client
 * 
 * @author ian.macdonald@ianmacdonald.scot
 */
public class CakeExceptionMessage {

	private String message;

	private String type;

	private String causeType;

	private String causeMessage;

	public CakeExceptionMessage(final String message, final String type, final String causeType,
			final String causeMessage) {

		this.message = message;
		this.type = type;
		this.causeType = causeType;
		this.causeMessage = causeMessage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCauseType() {
		return causeType;
	}

	public void setCauseType(String causeType) {
		this.causeType = causeType;
	}

	public String getCauseMessage() {
		return causeMessage;
	}

	public void setCauseMessage(String causeMessage) {
		this.causeMessage = causeMessage;
	}

}
