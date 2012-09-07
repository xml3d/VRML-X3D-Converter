package org.xml3d.converter.x3d;

import java.io.*;

import javax.xml.transform.TransformerException;

import org.xml3d.converter.Converter;

/**
 * X3DConverter converts X3D files to XML3D or XML3D embedded in XHTML by means 
 * of XSLT transformations. The outcome is embedded in
 * HTML which allows to regard instantly the result with
 * a XML3D enabled browser. In spite of this, the result
 * is stored in a .xml file per default. However, other
 * filenames can be enforced in {@link X3DConverter#run(String, File)}.
 * 
 * @author Kristian Sons
 * @author Benjamin Friedrich
 * @version 1.0  08/2010
 */
public final class X3DConverter extends Converter
{
	private static final String XSL_FILE = "x3d2xml3d_m4.xsl";
	
	/** 
	 * Constructor of X3DConverter
	 */
	public X3DConverter()
	{
		super(XSL_FILE);
	}
	
	/**
	 * Converts the given X3D file to XML3D or to XML3D embedded 
	 * in XHTML. In doing so, the name of the transformed file is 
	 * the name of the input file with the corresponding suffix 
	 * ".xml3d" or ".xhtml" instead of ".x3d".
	 * 
	 * @param  x3dFile
	 * 				file to be transformed to xml3d
	 * @param  inHTML
	 * 				indicates whether the output is pure XML3D or
	 * 				XML3D embedded in XHTML
	 * 
	 * @throws TransformerException 
	 * 				if a problem during the XSLT transformation occurs 
	 */
	public void run(final String inputFile, final boolean inHTML) throws TransformerException
	{
		final String outputFile = inputFile.replaceFirst(".x3d$", inHTML ? ".xhtml": ".xml3d");
		this.run(inputFile, new File(outputFile), inHTML);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws IllegalArgumentException
	 * 				if the X3D file does not end with suffix .x3d
	 */
	@Override
	protected void checkInputFile(final String inputFile)
	{
		super.checkInputFile(inputFile);
		
		if(! inputFile.endsWith(".x3d"))
		{
			throw new IllegalArgumentException("Invalid suffix of the X3D input file " + 
											   inputFile + 
											   ". Expected: .x3d");
		}		
	}
}