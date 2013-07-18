/**
 * 
 */
package com.jae.eclipse.cloudfoundry.exception;

/**
 * @author hongshuiqiao
 *
 */
public class CloudFoundryClientRuntimeException extends RuntimeException {
	private static final long serialVersionUID = -7578740741171562531L;

	public CloudFoundryClientRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public CloudFoundryClientRuntimeException(String message) {
		super(message);
	}

	public CloudFoundryClientRuntimeException(Throwable cause) {
		super(cause);
	}

}
