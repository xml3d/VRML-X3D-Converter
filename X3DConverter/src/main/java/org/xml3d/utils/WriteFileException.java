package org.xml3d.utils;

/**
 * Exception for problems while writing file
 * 
 * @author  Benjamin Friedrich
 * @version 1.0  08/2010
 */
public final class WriteFileException extends RuntimeException
{
	private static final long serialVersionUID = 1015522575920209542L;

	public WriteFileException(final String msg)
	{
		super(msg);
	}
}
