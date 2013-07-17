/**
 * 
 */
package com.jae.eclipse.core.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 用于在errordialog中显示详细的错误堆栈信息
 * @author hongshuiqiao
 *
 */
public class StatusException extends Exception {
	private static final long serialVersionUID = 3499706476041999834L;
	private Throwable cause;

	public StatusException(Throwable cause) {
		super(cause);
		this.cause = cause;
	}

	@Override
	public String getMessage() {
		if(null == this.cause)
			return super.getMessage();
		
		StringWriter writer = new StringWriter();
		this.cause.printStackTrace(new PrintWriter(writer));
		String message = writer.toString();
		return message;
	}
}
