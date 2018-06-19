package com.sierotech.alarmsys.common;


/**
 * Service层公用的Exception. 继承自RuntimeException
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1401593546385403720L;

	public BusinessException() {
		super();
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}
}
