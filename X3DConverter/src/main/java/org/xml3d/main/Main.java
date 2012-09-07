package org.xml3d.main;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import javax.xml.transform.TransformerException;

import org.xml3d.converter.UpgradeConverter;
import org.xml3d.converter.x3d.X3DConverter;
import org.xml3d.merger.XML3DMerger;
import org.xml3d.utils.ParseException;
import org.xml3d.utils.SchemaValidator;

/**
 * Main represents the main entry point for utilizing all XML3D tools 
 * developed in this project. It acts as facade to the different tool
 * implementations.<br/>
 * <br/>
 * At the moment, the following tools are available:<br/>
 * <code>
 *	xml3d
 * 	 -c    x3dfile                   [-xhtml]     [-o outfile] -- Converts x3d file to xml3d
 * 	 -m    xml3dfile1 ... xml3dfileN [-xhtml] [-i] -o outfile  -- Merges multiple xml3d files one
 * 	 -v    xml3dfile                                           -- Validates xml3d file current schema (xml3d.xsd)
 *   -u0.3 xml3d_0.3_file            [-xhtml] [-i] -o outfile  -- Upgrades the given XML3D (v. 0.3) file into the
 *                                                                current format
 * </code>
 * <br/>
 * <b>Example for x3d to xml3d conversion:</b><br/>
 * <code>
 *   xml3d -c test.x3d
 *   xml3d -c test.x3d -o test.xml3d
 *   xml3d -c test.x3d -xhtml
 *   xml3d -c test.x3d -xhtml -o test.xml3d
 * </code>
 * <br/>
 * <b>Example for merging multiple xml3d files:</b><br/>
 * <code>
 * 	  xml3d -m test1.xml test2.xml test3.xml -o out.xml3d
 *    xml3d -m test1.xml test2.xml test3.xml -xhtml -o out.xhtml
 * </code>
 * <br/>
 * <b>Example for merging ignoring the xml3d schema:</b><br/>
 * <code>
 * 	  xml3d -m test1.xml test2.xml test3.xml -i -o out.xml3d
 *    xml3d -m test1.xml test2.xml test3.xml -xhtml -i -o out.xhtml
 * </code>
 * <br/>
 * <b>Example for validating a xml3d file:</b><br/>
 * <code>
 * 	  xml3d -v test1.xml3d
 * </code>
 * <br/>
 * <b>Example for upgrading a xml3d v.0.3 file:</b><br/>
 * <code>
 * 	  xml3d -u0.3 test1.xml3d -o new.xml3d
 * 	  xml3d -u0.3 test1.xml3d -xhtml -o new.xml3d
 * </code>
 * 
 * @author  Benjamin Friedrich
 * @version 1.0  08/2010
 */
public final class Main 
{
	private static final String   OUTPUT_FLAG      = "-o";
	private static final String   MERGE_FLAG       = "-m";
	private static final String   VALID_FLAG       = "-v";
	private static final String   CONV_FLAG        = "-c";
	private static final String   No_VALID_FLAG    = "-i";
	private static final String   UPGRADE_0_3_FLAG = "-u0.3";
	private static final String   XHTML_FLAG       = "-xhtml";
	
	private static final String[] SUPPORT_FUNC = new String[] { CONV_FLAG, MERGE_FLAG, VALID_FLAG, UPGRADE_0_3_FLAG};

	
	private static String USAGE_MSG = " \nxml3d\n" +
	 								  " -c    x3dfile                   [-xhtml]     [-o outfile]    -- Converts x3d file to xml3d\n" +
	 								  " -m    xml3dfile1 ... xml3dfileN [-xhtml] [-i] -o outfile     -- Merges multiple xml3d files to\n" +
	 								  "                                                                 one\n" +
	 								  " -v    xml3dfile                                              -- Validates xml3d file against\n" +
	 								  "                                                                 current schema (xml3d.xsd)\n" +
	 								  " -u0.3 xml3d_0.3_file            [-xhtml] [-i] -o outfile     -- Upgrades the given XML3D (v. 0.3) file\n" +
	 								  "                                                                 into the current format\n";
	/**
	 * Private constructor to avoid instantiation of Main
	 */
	private Main(){}
	
	/**
	 * Checks for the correct position of the output arguments. 
	 * If the position is not correct or no output file is specified, 
	 * an error message as well as a short usage instruction are printed 
	 * and the program exits with -4 (wrong position) or -10 (no output file). 
	 * 
	 * @param args 
	 * 			Arguments retrieved from {@link Main#main(String[])}
	 */
	private static void checkOutputFlag(final String[] args)
	{
		final List<String> arguments = Arrays.asList(args);
		final int          index     = arguments.indexOf(OUTPUT_FLAG);
		
		if(index == arguments.size() - 1)
		{
			System.err.println("No output file specified");
			System.err.println(USAGE_MSG);
			System.exit(-10);
		}
		
		if(index != arguments.size() - 2)
		{
			System.err.println("Invalid position of the output flag");
			System.err.println(USAGE_MSG);
			System.exit(-4);
		}
	}
	
	/**
	 * Performs the xml3d validation for a file. If the validation is successful, a
	 * success message is shown, otherwise an error message is printed out as well as
	 * the reason of the unsuccessful validation and the program exits with -3.
	 * 
	 * @param args 
	 * 				Arguments retrieved from {@link Main#main(String[])}
	 * 
	 * @throws URISyntaxException
	 * 				Should never be thrown
	 */	
	private static void performValidation(final String args[]) throws URISyntaxException
	{
		final SchemaValidator validator = new SchemaValidator("xml3d.xsd");	
		
		if(validator.validate(new File(args[1])))
		{
			System.out.println("Validation has been successful");
		}
		else
		{
			System.err.println("Validation has NOT been successful");
			System.err.println(validator.getValidationErrorMsg());
			System.exit(-3);
		}
	}
	
	/**
	 * Performs the conversion from x3d to xml3d or to xml3d embedded in XHTML. 
	 * If problems occur during the XSLT transformation. An appropriate error message
	 * is shown and the program exits with -9. If the XHTML flag is specified at
	 * a wrong position the program exits with -12.
	 * 
	 * @param args 
	 * 			Arguments retrieved from {@link Main#main(String[])}
	 */	
	private static void performConversion(final String[] args)
	{
		final X3DConverter converter = new X3DConverter();
		final List<String> arguments = Arrays.asList(args);
		try
		{
			final int index 		 = arguments.indexOf(OUTPUT_FLAG);
			final int xhtmlFlagIndex = arguments.indexOf(XHTML_FLAG);

			if(index == -1)
			{
				if(xhtmlFlagIndex != -1 && xhtmlFlagIndex != arguments.size() - 1)
				{
					System.err.println("-xhtml is specified at a wrong position");
					System.err.println(USAGE_MSG);
					System.exit(-12);
				}
				
				converter.run(args[1], arguments.contains(XHTML_FLAG));
			}
			else
			{
				if(xhtmlFlagIndex != -1 && xhtmlFlagIndex != arguments.size() - 3)
				{
					System.err.println("-xhtml is specified at a wrong position");
					System.err.println(USAGE_MSG);
					System.exit(-12);
				}
				
				Main.checkOutputFlag(args);
				converter.run(args[1], new File(args[index + 1]), arguments.contains(XHTML_FLAG));
			}
			
			System.out.println("Conversion has been finished successfully");
		}
		catch(final TransformerException e)
		{
			System.err.println("An error occured while xslt transformation");
			e.printStackTrace();
			System.exit(-9);
		}
	}
	
	/**
	 * Performs the merging of multiple XML3D files. If no output argument
	 * is specified, the program exits with -6. If an error occurs during 
	 * parsing a xml3d file, the program exits with -7. If the xhtml-Flag is
	 * specified at a wrong position the program exits with -11.
	 * 
	 * @param args 
	 * 			Arguments retrieved from {@link Main#main(String[])}
	 */	
	private static void performMerging(final String[] args)
	{
		final List<String> arguments = Arrays.asList(args);
		
		if(! arguments.contains(OUTPUT_FLAG))
		{
			System.err.println("No output flag specified");
			System.err.println(USAGE_MSG);
			System.exit(-6);
		}
		
		Main.checkOutputFlag(args);
		
		final boolean isValidationDisabled = arguments.contains(No_VALID_FLAG);
		final int     outFlagIndex         = arguments.indexOf(OUTPUT_FLAG);
		int           lastInputIndex       = outFlagIndex;
		
		if(isValidationDisabled)
		{
			if(No_VALID_FLAG.equals(args[outFlagIndex - 1]))
			{
				lastInputIndex--;
			}
			else
			{
				System.err.println("The flag for ignoring the schema validation is specified at a wrong position");
				System.err.println(USAGE_MSG);
				System.exit(-13);
			}
		}
		
		final XML3DMerger merger = new XML3DMerger(new File(args[outFlagIndex + 1]), ! isValidationDisabled);
		
		if(arguments.contains(XHTML_FLAG))
		{
			final int xhtmlFlagIndex = arguments.indexOf(XHTML_FLAG);
			lastInputIndex--;
			
			if(xhtmlFlagIndex != lastInputIndex)
			{
				System.err.println("-xhtml is specified at a wrong position");
				System.err.println(USAGE_MSG);
				System.exit(-11);
			}
		}

		
		try
		{
			for(int i = 1; i < lastInputIndex; i++)
			{
				merger.process(args[i]);
			}
			
			if(XHTML_FLAG.equals(args[outFlagIndex - 1]))
			{
				merger.writeXHTML();
			}
			else
			{
				merger.writeXML();
			}
			
			System.out.println("Merge process has been successful");
		}
		catch(final ParseException e)
		{
			System.err.println("A parsing error occured while merging files");
			e.printStackTrace();
			System.exit(-7);
		}
	}
	
	/**
	 * Upgrades XML3D v.0.3 to v.0.4 to XML3D v.0.4 embedded in XHTML. 
	 * If problems occur during the XSLT transformation. An appropriate error message
	 * is shown and the program exits with -16. If the XHTML flag is specified at
	 * a wrong position, the program exits with -14. If the flag for disabling
	 * the schema validation is positioned at a wrong location, the program exits with
	 * -15. See also {@link Main#checkOutputFlag(String[])}.
	 * 
	 * @param args 
	 * 			Arguments retrieved from {@link Main#main(String[])}
	 */	
	private static void performUpgrade(final String[] args)
	{
		final UpgradeConverter upgrader  = UpgradeConverter.getVersion03Upgrader();
		final List<String>     arguments = Arrays.asList(args);
		try
		{
			final int xhtmlFlagIndex = arguments.indexOf(XHTML_FLAG);

			if(xhtmlFlagIndex != -1 && xhtmlFlagIndex != arguments.size() - 3)
			{
				System.err.println("-xhtml is specified at a wrong position");
				System.err.println(USAGE_MSG);
				System.exit(-14);
			}
			
			Main.checkOutputFlag(args);
			
			final boolean isValidationDisabled = arguments.contains(No_VALID_FLAG);
			final int     outFlagIndex         = arguments.indexOf(OUTPUT_FLAG);
			
			if(isValidationDisabled)
			{
				if(! No_VALID_FLAG.equals(args[outFlagIndex - 1]))
				{
					System.err.println("The flag for ignoring the schema validation is specified at a wrong position");
					System.err.println(USAGE_MSG);
					System.exit(-15);
				}
			}

			upgrader.enableValidation(! isValidationDisabled);
			
			final int index = arguments.indexOf(OUTPUT_FLAG);
			upgrader.run(args[1], new File(args[index + 1]), arguments.contains(XHTML_FLAG));
			
			System.out.println("Upgrade from XML3D v.0.3 to v.0.4 has been finished successfully");
		}
		catch(final TransformerException e)
		{
			System.err.println("An error occured while xslt transformation");
			e.printStackTrace();
			System.exit(-16);
		}
	}
		
	
	
	/** 
	 * Starts XML3D Tool Suite <br/>
	 * <br/>
	 * <code>
	 *   -c x3dfile                   [-o outfile] -- Converts x3d file to xml3d
	 *   -m xml3dfile1 ... xml3dfileN  -o outfile  -- Merges multiple xml3d files to one
	 *   -v xml3dfile								 -- Validates xml3d file against current schema (xml3d.xsd)
	 * </code>
	 * <br/>
	 */
	public static void main(final String[] args) 
	{
		// minimum of needed arguments
		if(args.length < 2)
		{
			System.err.println("Invalid number of arguments: " + args.length);
			System.err.println(USAGE_MSG);
			System.exit(-1);
		}
		
		final List<String> supportedFuncs = Arrays.asList(SUPPORT_FUNC);
		if(! supportedFuncs.contains(args[0]))
		{
			System.err.println("Unsupported flag " + args[0]);
			System.err.println(USAGE_MSG);
			System.exit(-2);
		}
		
		try
		{
			if(VALID_FLAG.equals(args[0])) // Validation of a xml3d file
			{
				Main.performValidation(args);
			}
			else if(CONV_FLAG.equals(args[0])) // Conversion from x3d to xml3d
			{
				Main.performConversion(args);
			}
			else if(MERGE_FLAG.equals(args[0])) // Merging of multiple xml3d files
			{
				Main.performMerging(args);
			}
			else if(UPGRADE_0_3_FLAG.equals(args[0])) // Upgrade from XML3D v.0.3 to v.0.4
			{
				Main.performUpgrade(args);
			}
			else
			{
				System.err.println("Unknown function tag " + args[0]);
				System.exit(-8);
			}
		}
		catch(final Exception e)
		{
			e.printStackTrace();
			System.exit(-5);
		}
	}
}