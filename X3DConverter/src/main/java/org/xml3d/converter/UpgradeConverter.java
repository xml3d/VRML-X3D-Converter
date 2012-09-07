package org.xml3d.converter;

import java.io.File;

import org.xml3d.utils.SchemaValidator;

/**
 * UpgradeConverter converts ancient XML3D versions to
 * the current format.
 * 
 * @author  Benjamin Friedrich
 * @version 1.0 09/10 
 */
public final class UpgradeConverter extends Converter
{
	private final SchemaValidator validator;
	private       boolean		  isValidationEnabled;
	
	private static final String XSL_0_3_TO_0_4 = "upgrade_0_3_to_0_4.xsl";
	private static final String XSD_SCHEMA     = "xml3d.xsd";
	
	/**
	 * Constructor of UpgradeConverter. The constructor is declared private
	 * to enable comfortable instantiation with appropriate XSL style sheets
	 * via well named factory methods.
	 * 
	 * @param xslStyleSheet
	 * 				upgrade XSL style sheet 
	 * 
	 * @see {@link Converter#Converter(String)}
	 */
	private UpgradeConverter(final String xslStyleSheet)
	{
		super(xslStyleSheet);
		
		this.validator           = new SchemaValidator(XSD_SCHEMA);
		this.isValidationEnabled = true;
	}

	/**
	 * Creates a converter for upgrading XML3D v0.3 data
	 * to XML3D v0.4.
	 * 
	 * @return  appropriate UpgradeConverter instance
	 */
	public static final UpgradeConverter getVersion03Upgrader()
	{
		return new UpgradeConverter(XSL_0_3_TO_0_4);
	}
	
	/**
	 * Determines whether schema validation shall be performed
	 * before the conversion process. Note that the schema validation
	 * is enabled per default.
	 * 
	 * @param validation
	 * 			true if the validation shall be performed, false otherwise.
	 */
	public void enableValidation(final boolean validation)
	{
		this.isValidationEnabled = validation;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws IllegalArgumentException
	 * 				if the XML3D file does not end with suffix .xml3d
	 * @throws IllegalArgumentException
	 * 				if the input file does not conform XML3D v3.0 
	 * 				(if validation is enabled)
	 */
	@Override
	protected void checkInputFile(final String inputFile)
	{
		super.checkInputFile(inputFile);
		
		if(! inputFile.endsWith(".xml3d"))
		{
			throw new IllegalArgumentException("Invalid suffix of the XML3d input file " + 
											   inputFile + 
											   ". Expected: .xml3d");
		}		
		
		if(this.isValidationEnabled)
		{
			if(! this.validator.validate(new File(inputFile)))
			{
				throw new IllegalArgumentException("File " + inputFile + 
												   " does not conform to " + XSD_SCHEMA + ": " +
												   this.validator.getValidationErrorMsg());
			}
		}
	}
}