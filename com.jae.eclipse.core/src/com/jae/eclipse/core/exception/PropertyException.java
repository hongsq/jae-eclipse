/**
 * 
 */
package com.jae.eclipse.core.exception;

/**
 * @author hongshuiqiao
 *
 */
public class PropertyException extends RuntimeException {
	private static final long serialVersionUID = 1468104188885521011L;

	public PropertyException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropertyException(String message) {
		super(message);
	}

	public PropertyException(Throwable cause) {
		super(cause);
	}

}
