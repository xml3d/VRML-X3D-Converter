package org.xml3d.utils;

/**
 * Exception for problems while reading file
 * 
 * @author  Benjamin Friedrich
 * @version 1.0  08/2010
 */
public final class ReadFileException extends RuntimeException
{
	private static final long serialVersionUID = 1015522575920209542L;

	public ReadFileException(final String msg)
	{
		super(msg);
	}
}
