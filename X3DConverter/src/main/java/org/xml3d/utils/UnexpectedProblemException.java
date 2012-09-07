package org.xml3d.utils;

/**
 * Exception for unexpected problems
 * 
 * @author  Benjamin Friedrich
 * @version 1.0  08/2010
 */
public final class UnexpectedProblemException extends RuntimeException
{
	private static final long serialVersionUID = 2264764981302825365L;

	public UnexpectedProblemException(final Throwable t)
	{
		super(t);
	}
	
	public UnexpectedProblemException(final String msg)
	{
		super(msg);
	}
}
