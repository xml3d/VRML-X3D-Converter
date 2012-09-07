package org.xml3d.merger;

import java.io.*;
import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml3d.utils.*;

/**
 * XML3DMerger is intended to merge multiple files to one. Thereby, it
 * takes care that all IDs remain unique and their corresponding references
 * are consistent.<br/>
 * <br/>
 * <b> Usage Example: </b> <br/> 
 * <code>
 * 		XML3DMerger merger = new XML3DMerger(outFile);
 * 		merger.process(new File("file1.xml"));
 * 		merger.process(new File("file2.xml"));
 * 		merger.write();
 * </code>
 * 
 * @author  Benjamin Friedrich
 * @version 1.0  08/2010
 */
public final class XML3DMerger
{
	private final XMLReader            reader;
	private final File	               outFile;
	private final MergeContentHandler  contentHandler;
	private final SchemaValidator      schemaValidator;
	private final boolean			   isValidationEnabled;
	
	/** XML3D schema location */
	private static final String SCHEMA_LOC = "xml3d.xsd";
	
	/**
	 * Constructor of XML3DMerger
	 * 
	 * @param outFile  
	 * 				file for the outcome the merge process
	 * @param validation  
	 * 				indicates whether the schema validation within
	 * 				{@link XML3DMerger#process(String)} is enabled
	 * @throws NullPointerException
	 * 				if given outFile is null
	 * @throws IllegalArgumentException
	 * 				if outFile does already exist
	 * @throws RuntimeException
	 * 				if an unexpected problem occurs while creating XMLReader instance
	 */
	public XML3DMerger(final File outFile, final boolean validation)
	{
		if(outFile == null)
		{
			throw new NullPointerException("Given output file is null");
		}
		
		if(outFile.exists())
		{
			throw new IllegalArgumentException("Given ouput file " + outFile + " does already exist");
		}
		
		this.outFile = outFile;
		
		if(validation)
		{
			this.schemaValidator = new SchemaValidator(SCHEMA_LOC);
		}
		else
		{
			this.schemaValidator = null;
		}
		
		try
		{
			this.reader = XMLReaderFactory.createXMLReader();
		}
		catch(final SAXException e)
		{
			throw new RuntimeException(e);
		}
		
		final MergeFilter filter = new MergeFilter();
		this.contentHandler      = new MergeContentHandler();
		this.isValidationEnabled = validation;
		filter.setContentHandler(this.contentHandler);
		this.reader.setContentHandler(filter);
	}

	/**
	 * Constructor of XML3DMerger. Using this constructor enables 
	 * the schema validation within {@link XML3DMerger#process(String)}
	 * per default.
	 * 
	 * @param outFile  
	 * 				file for the outcome the merge process
	 * @throws NullPointerException
	 * 				if given outFile is null
	 * @throws IllegalArgumentException
	 * 				if outFile does already exist
	 * @throws RuntimeException
	 * 				if an unexpected problem occurs while creating XMLReader instance
	 */
	public XML3DMerger(final File outFile)
	{
		this(outFile, true);
	}
	
	/**
	 * Processes the given XML3D file and merges the outcome with the other
	 * previously processed XML3D files.
	 * 
	 * @param  targetFile
	 * 					file to be processed
	 * @throws NullPointerException
	 * 					if targetFile is null
	 * @throws IllegalArgumentException
	 * 					if the target file name is empty
	 * @throws IllegalArgumentException
	 * 					if target file does not exist
	 * @throws IllegalArgumentException
	 * 					if target file can not be read
	 * @throws ParseException
	 * 					if target file does not conform to the XML3D schema (xml3d.xsd)
	 * @throws ParseException
	 * 					when an unexpected problem during parsing targetFile occurs
	 */
	public void process(final String targetFile) throws ParseException
	{
		if(targetFile == null)
		{
			throw new NullPointerException("Given target file name is null");
		}
		
		if(targetFile.trim().isEmpty())
		{
			throw new IllegalArgumentException("Given target file name is empty");
		}
		
		final File file = new File(targetFile);
		
		if(! file.exists())
		{
			throw new IllegalArgumentException("File " + targetFile + " does not exist");
		}
		
		
		if(! file.canRead())
		{
			throw new ReadFileException("Can not read file " + targetFile);
		}
		
		try
		{
			if(this.isValidationEnabled)
			{
				if(! this.schemaValidator.validate(file))
				{
					throw new ParseException("File " + targetFile + " does not conform to schema defined in " + SCHEMA_LOC);
				}
			}

			final FileInputStream     fIn = new FileInputStream(file);
			final BufferedInputStream bIn = new BufferedInputStream(fIn);

			this.reader.parse(new InputSource(bIn));	
		}
		catch(final Exception e)
		{
			throw new ParseException(e);
		}
	}
	
	/**
	 * Writes the merge outcome to the file specified in {@link XML3DMerger#XML3DMerger(File)}
	 * 
	 * @throws WriteFileException
	 * 					if a problem occurs while writing to the file
	 */
	private void write(final String content)
	{
		BufferedWriter writer = null;
		
		try
		{
			writer = new BufferedWriter(new FileWriter(this.outFile));
			writer.write(content);
		}
		catch(final Exception e)
		{
			throw new WriteFileException(e.getMessage());
		}
		finally
		{
			try 
			{
				if(writer != null)
				{
					writer.close();
				}
			} 
			catch (final IOException e) 
			{
				throw new WriteFileException(e.getMessage());
			}
		}
	}

	/**
	 * Writes the merge outcome as XML to the file specified in 
	 * {@link XML3DMerger#XML3DMerger(File)}
	 * 
	 * @throws WriteFileException
	 * 					if a problem occurs while writing to the file
	 */	
	public void writeXML()
	{
		this.write(this.contentHandler.toXML());
	}
	
	/**
	 * Writes the merge outcome embedded in XHTML to the file specified in 
	 * {@link XML3DMerger#XML3DMerger(File)}
	 * 
	 * @throws WriteFileException
	 * 					if a problem occurs while writing to the file
	 */		
	public void writeXHTML()
	{
		this.write(this.contentHandler.toXHTML());
	}	
}