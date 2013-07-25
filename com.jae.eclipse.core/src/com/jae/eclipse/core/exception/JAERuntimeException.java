/**
 * 
 */
package com.jae.eclipse.core.exception;

/**
 * @author hongshuiqiao
 *
 */
public class JAERuntimeException extends RuntimeException {
	private static final long serialVersionUID = -1784517707740636647L;

	public JAERuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public JAERuntimeException(String message) {
		super(message);
	}

	public JAERuntimeException(Throwable cause) {
		super(cause);
	}

}
