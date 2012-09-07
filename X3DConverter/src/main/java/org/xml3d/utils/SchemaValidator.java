package org.xml3d.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

/**
 * SchemaValidator validates files against schemas encoded in the
 * W3C XML Schema language (<code>http://www.w3.org/2001/XMLSchema</code>)
 * and provides means to get the reasons of unsuccessful validations. <b>Note
 * that schemas which are specified in the XML file are also taken in
 * account in the validation process besides the target schema given to 
 * {@link SchemaValidator#SchemaValidator(String)}.</b>
 * 
 * @author  Benjamin Friedrich
 * @version 1.0  08/2010
 */
public final class SchemaValidator 
{
	private final  Validator validator;
	private 	   String 	 validationErrorMsg;

	/** W3C XML Schema language */
	private static final String SCHEMA_LANG = "http://www.w3.org/2001/XMLSchema";
	
	/**
	 * Constructor of SchemaValidator
	 * 
	 * @param xsdLocation
	 * 				location of the schema such as xml3d.xsd. First, the xsdLocation is considered
	 * 				as file path. If xsdLocation is not found in the file path, the schema is retrieved
	 * 				from the classpath
	 * @throws NullPointerException
	 * 				if xsdLocation is null
     * @throws IllegalArgumentException
	 * 				if xsdLocation is empty
	 * @throws IllegalArgumentException
	 * 				if xsdLocation does not exist
	 * @throws UnexpectedProblemException
	 * 				if an unexpected problem occurs while schema related initializations
	 */
	public SchemaValidator(final String xsdLocation)
	{
		if(xsdLocation == null)
		{
			throw new NullPointerException("Given xsd location is null");
		}
		
		if(xsdLocation.trim().isEmpty())
		{
			throw new IllegalArgumentException("Given xsd location is empty");
		}
			
		final File        xsdFile = new File(xsdLocation);
		final InputStream in;
		
		if(xsdFile.exists())
		{
			try 
			{
				in = new FileInputStream(xsdLocation);
			} 
			catch (final FileNotFoundException e)
			{
				// should never happen
				throw new IllegalArgumentException(e);
			}
		}
		else
		{
			in = this.getClass().getClassLoader().getResourceAsStream(xsdLocation);
			if(in == null)
			{
				throw new IllegalArgumentException("File " + xsdLocation + " does not exist");
			}
		}

        final SchemaFactory factory = SchemaFactory.newInstance(SCHEMA_LANG);
        try 
        {
        	final Schema schema = factory.newSchema(new StreamSource(in));
			this.validator      = schema.newValidator();
		} 
        catch (final SAXException e) 
        {
			throw new UnexpectedProblemException(e);
		}
        
        this.validationErrorMsg = "";
	}
	
	/**
	 * Return the error message of the last validation.
	 * 
	 * @return  error message
	 */
	public String getValidationErrorMsg()
	{
		return this.validationErrorMsg;
	}
	
	/**
	 * Validates the given file against the schema specified in @link {@link SchemaValidator#SchemaValidator(String)}.
	 * 
	 * @param  targetFile
	 * 				file to be validated
	 * 
	 * @return true if targetFile conforms to the schema, otherwise false
	 * 
	 * @throws NullPointerException
	 * 				if targetFile is null
	 * @throws IllegalArgumentException
	 * 				if targetFile does not exist or is not readable
	 */
	public boolean validate(final File targetFile)
	{
		if(targetFile == null)
		{
			throw new NullPointerException("Given file to be validated is null");
		}
		
		if(! targetFile.canRead())
		{
			throw new IllegalArgumentException("Can not read file " + targetFile);
		}
		
        try 
        {
    		final FileInputStream fIn   = new FileInputStream(targetFile);
            final Source         source = new StreamSource(new BufferedInputStream(fIn));
            
			this.validator.validate(source);
			this.validationErrorMsg = "";
			return true;
		} 
        catch (final Exception e) 
        {
        	this.validationErrorMsg = e.getMessage();
			return false;
		}
	}
}