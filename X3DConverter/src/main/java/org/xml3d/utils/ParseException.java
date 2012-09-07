package org.xml3d.utils;

/**
 * Exception for problems during the parsing process
 * 
 * @author  Benjamin Friedrich
 * @version 1.0  08/2010
 */
public final class ParseException extends Exception
{
	private static final long serialVersionUID = -890572950710407486L;

	public ParseException(final Throwable t)
	{
		super(t);
	}
	
	public ParseException(final String msg)
	{
		super(msg);
	}
}
