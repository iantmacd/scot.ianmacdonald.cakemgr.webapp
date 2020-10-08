package scot.ianmacdonald.cakemgr.webapp.model;

public class PojoJsonConverterException extends RuntimeException {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -3489336143111885272L;

	/**
	 * Runtime exception wrapping exceptions thrown by the PojoJsonConverter methods
	 */
	public PojoJsonConverterException(String errorMessage, Throwable throwable) {
		super(errorMessage, throwable);
	}
}
