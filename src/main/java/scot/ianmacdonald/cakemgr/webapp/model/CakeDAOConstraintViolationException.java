package scot.ianmacdonald.cakemgr.webapp.model;

public class CakeDAOConstraintViolationException extends RuntimeException {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -5617158610744360704L;

	/**
	 * Runtime exception wrapping exceptions thrown by implementations of the
	 * CakeDAO class.
	 */
	public CakeDAOConstraintViolationException(String errorMessage, Throwable throwable) {
		super(errorMessage, throwable);
	}
}
