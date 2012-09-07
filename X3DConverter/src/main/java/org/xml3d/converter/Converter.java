package org.xml3d.converter;

import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml3d.converter.x3d.xslt.XSLTLoader;
import org.xml3d.utils.Utils;

/**
 * Converter converts a given input files to XML3D or XML3D embedded in XHTML by means 
 * of XSLT transformations. The outcome is embedded in HTML which allows to regard instantly 
 * the result with a XML3D enabled browser. 
 * 
 * @author Kristian Sons
 * @author Benjamin Friedrich
 * @version 1.0  09/2010
 */
public class Converter 
{
	private final String xslStyleSheet;
	
	/** XHTML output template */
	private static final String XHTML_TPL = "xhtml.tpl";
	
	/** Template place holder */
	private static final String TPL_VAR = "${xml3d}";
	
	/** Start of the XML3D document */
	private static final String DOC_START = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

	
	/** 
	 * Constructor of Converter
	 * 
	 * @param  xslStyleSheet 
	 * 				XSL file used for the transformation. <b>Note that 
	 * 				the outcome of the XSL transformation must be XML3D.</b>
	 * 				However, there is no check implemented which ensures
	 * 				proper outcome of the transformation.
	 * 
	 * @throws NullPointerException
	 * 				if the given XSL file is null
	 * @throws IllegalArgumentException
	 * 				if the given XSL file is empty
	 * @throws IllegalArgumentException
	 * 				if the XSL file can not be found in class path or file system
	 * @throws IllegalArgumentException
	 * 				if the XSL file does not end with suffix .xsl
	 */
	public Converter(final String xslStyleSheet)
	{
		if(xslStyleSheet == null)
		{
			throw new NullPointerException("Given input file is null");
		}
		
		if(xslStyleSheet.trim().isEmpty())
		{
			throw new IllegalArgumentException("Name of the input file is empty");
		}
		
		if(! xslStyleSheet.endsWith(".xsl"))
		{
			throw new IllegalArgumentException("Invalid suffix of the input file " + 
											   xslStyleSheet + 
											   ". Expected: .xsl");
		}
		
		final InputStream xslStream = XSLTLoader.class.getClassLoader().getResourceAsStream(xslStyleSheet);
		if(xslStream == null)
		{
			throw new IllegalArgumentException("Can not find XSL style sheet" +
					                           xslStyleSheet + 
					                           " in class path or file system");
		}
		
		this.xslStyleSheet = xslStyleSheet;
		
		System.setProperty("javax.xml.transform.TransformerFactory", 
						   "net.sf.saxon.TransformerFactoryImpl");
	}
	
	/**
	 * Checks the given input file and throws corresponding
	 * exceptions if the check fails.
	 * 
	 * @param inputFile
	 * 				input file
	 * 
	 * @throws NullPointerException
	 * 				if the given input file is null
	 * @throws IllegalArgumentException
	 * 				if the given input file is empty
	 * @throws IllegalArgumentException
	 * 				if the input file does not exist or can not be read
	 */
	protected void checkInputFile(final String inputFile)
	{
		if(inputFile == null)
		{
			throw new NullPointerException("Given input file is null");
		}
		
		if(inputFile.trim().isEmpty())
		{
			throw new IllegalArgumentException("Name of the input file is empty");
		}
		
		final File in = new File(inputFile);
		if (!in.canRead())
		{
			throw new IllegalArgumentException("File "  + 
												inputFile + 
												" does not exist or is not readable.");
		}
	}
	
	/**
	 * Checks the output file
	 * 
	 * @param outputFile
	 * 				output file
	 * 
	 * @throws NullPointerException
	 * 				if the given output file is null
	 * @throws IllegalArgumentException
	 * 				if the given output file already exists
	 */
	protected void checkOuputFile(final File outputFile)
	{
		if(outputFile == null)
		{
			throw new NullPointerException("Given output file is null");
		}
		
		/*if(outputFile.exists())
		{
			throw new IllegalArgumentException("Given output file " + outputFile + " does already exist");
		}*/		
	}
	
	/**
	 * Converts the given input file to XML3D. 
	 * 
	 * @param  x3dFile
	 * 				file to be transformed to xml3d
	 * @param  outputFile
	 * 				target file for the conversion result
	 * @param  inHTML
	 * 				indicates whether the output is pure XML3D or
	 * 				XML3D embedded in XHTML
	 * 
	 * @throws TransformerException 
	 * 				if a problem during the XSLT transformation occurs
	 *
	 */
	public void run(final String inputFile, final File outputFile, final boolean inHTML) throws TransformerException
	{
		this.checkOuputFile(outputFile);
		this.checkInputFile(inputFile);
		this.transform(new File(inputFile), outputFile, inHTML);
	}
	/*
	 * This method will be called by Converter Webservice
	 * Performs the xsl transformation
	 * 
	 * @param fileData
	 * 		inputFile(x3d) in bytes to be converted
	 * @param resultData
	 * 		outputFile(xml3d) in bytes which is returned to the Webservice
	 * 
	 */
	public ByteArrayOutputStream TransfomForWS(ByteArrayInputStream fileData) throws ParserConfigurationException{
		ByteArrayOutputStream resultData=new ByteArrayOutputStream();
		final InputStream           xslStream   = XSLTLoader.class.getClassLoader().getResourceAsStream(this.xslStyleSheet);
		final TransformerFactory    f           = TransformerFactory.newInstance();
		
		
		 final DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		 
		f.setURIResolver(new URIResolver() {

			public Source resolve(String href, String base) throws TransformerException {
				try {
					ClassLoader cl = this.getClass().getClassLoader();
					java.io.InputStream in = cl.getResourceAsStream(href);

				    InputSource xslInputSource = new InputSource(in);
				    Document xslDoc;
					xslDoc = dBuilder.parse(xslInputSource);
					DOMSource xslDomSource = new DOMSource(xslDoc);
				    xslDomSource.setSystemId("xslt/" + href);
				    return xslDomSource;
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		Transformer transformer=null;
		try {
			transformer = f.newTransformer(new StreamSource(xslStream));
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			transformer.transform(new StreamSource(fileData), new StreamResult(resultData));
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultData;
	}
	/**
	 * Performs the actual XSLT transformation
	 * 
	 * @param  in		
	 * 				file to be transformed to xml3d
	 * @param  out
	 * 				target file for the conversion result
	 * @param  inHTML
	 * 				indicates whether the output is pure XML3D or
	 * 				XML3D embedded in XHTML
	 * 				
	 * @throws TransformerException 
	 * 				if a problem during the XSLT transformation occurs
	 */
	private void transform(final File in, final File out, final boolean inHTML) throws TransformerException 
	{
		final InputStream           xslStream   = XSLTLoader.class.getClassLoader().getResourceAsStream(this.xslStyleSheet);
		final TransformerFactory    f           = TransformerFactory.newInstance();
		final DocumentBuilder dBuilder;
		try {
			dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			throw new TransformerException(e1);
		}

		f.setURIResolver(new URIResolver() {

			public Source resolve(String href, String base) throws TransformerException {
				try {
					ClassLoader cl = this.getClass().getClassLoader();
					java.io.InputStream in = cl.getResourceAsStream(href);

				    InputSource xslInputSource = new InputSource(in);
				    Document xslDoc;
					xslDoc = dBuilder.parse(xslInputSource);
					DOMSource xslDomSource = new DOMSource(xslDoc);
				    xslDomSource.setSystemId("xslt/" + href);
				    return xslDomSource;
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		final Transformer           transformer = f.newTransformer(new StreamSource(xslStream));
		final ByteArrayOutputStream byteArrIn   = new ByteArrayOutputStream();

		transformer.transform(new StreamSource(in), new StreamResult(byteArrIn));
		
		BufferedWriter bWriter = null;
		
		try
		{
			bWriter = new BufferedWriter(new FileWriter(out));
			
			if(inHTML)
			{
				final String tplContent = Utils.getStringFromClassPathResource(XHTML_TPL);
				final String content    = byteArrIn.toString().replace(DOC_START, "");
				
				IOUtils.write(tplContent.replace(TPL_VAR, content), bWriter);
			}
			else
			{
				IOUtils.write(byteArrIn.toString(), bWriter);
			}
		}
		catch(final Exception e)
		{
			throw new TransformerException(e);
		}
		finally
		{
			IOUtils.closeQuietly(bWriter);
		}
	}
}