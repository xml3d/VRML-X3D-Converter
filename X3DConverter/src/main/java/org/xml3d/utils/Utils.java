package org.xml3d.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

/**
 * Utils contains helper methods which can be useful for 
 * many different classes.
 * 
 * @author  Benjamin Friedrich
 * @version 1.0  08/2010
 */
public final class Utils 
{
	private Utils() {}
	
	/**
	 * 
	 * @param resource
	 * @return
	 */
	public static String getStringFromClassPathResource(final String resource)
	{
		final InputStream in = Utils.class.getClassLoader().getResourceAsStream(resource);
		
		if(in == null)
		{
			throw new UnexpectedProblemException("Can not find resource " + 
												 resource + 
												 "  in the classpath");
		}
		
		final String content;
		try 
		{
			content = IOUtils.toString(new BufferedInputStream(in));
		} 
		catch (final IOException e) 
		{
			throw new UnexpectedProblemException(e);
		}
		finally
		{
			IOUtils.closeQuietly(in);
		}
		
		return content;
	}
	
}
