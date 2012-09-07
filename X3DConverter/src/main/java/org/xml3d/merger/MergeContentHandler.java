package org.xml3d.merger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml3d.utils.Utils;

/**
 * MergeContentHandler is utilized by {@link XML3DMerger} to merge the data
 * prepared by {@link MergeFilter} and to output them as indented XML3D or
 * XHTML file. The final outcome is influenced by the templates xhtml.tpl and
 * xml3d.tpl which provide appropriate basic skeletons.
 * 
 * @author  Benjamin Friedrich
 * @version 1.0  08/2010
 */
final class MergeContentHandler extends DefaultHandler
{
	private final StringBuilder 	   representation;
	private final Map<String, Boolean> parentMap;
	private       int           	   level;
	private 	  String			   lastElement;
	
	/** Start of the XML3D document */
	private static final String DOC_START = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	
	/** XHTML output template */
	private static final String XHTML_TPL = "xhtml.tpl";
	
	/** XML3D output template */
	private static final String XML3D_TPL = "xml3d.tpl";
	
	/** Template place holder */
	private static final String TPL_VAR = "${xml3d}";
	
	
	/** number of blanks for one indentation step */
	private static final int INDENT_STEP = 2;
	
	/**
	 * Constructor of MergeContentHandler
	 */
	public MergeContentHandler()
	{
		this.representation = new StringBuilder();
		this.level 			= 0;
		this.parentMap 		= new HashMap<String, Boolean>();
	}
	
	/**
	 * Indents the following output according to the current nesting level
	 * and INDENT_STEP.
	 */
	private void indent()
	{
		for(int i = 0; i < this.level * INDENT_STEP; i++)
		{
			this.representation.append(" ");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException 
	{
		if(qName.equals("xml3d"))
		{
			return;
		}

		if(this.lastElement != null)
		{
			// check necessary due to not well formed xml
		   if(Boolean.FALSE.equals(this.parentMap.get(this.lastElement + this.level)))
		   {
			   this.parentMap.put(this.lastElement + this.level, Boolean.TRUE);
			   this.representation.append(">\n"); 
		   }
		}
		
	    this.level++;

	    this.lastElement = qName;
	    this.parentMap.put(this.lastElement + this.level, Boolean.FALSE);
	    
	    this.indent();
		
	    this.representation.append("<").append(qName);
	    
	    for ( int i = 0; i < attributes.getLength(); i++ )
	    {          
  		   this.representation.append(" ")
  		   					  .append(attributes.getQName(i))
  		    				  .append("=\"")
  		    				  .append(attributes.getValue(i))
  		    				  .append("\"");
	    }
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endElement(final String uri, final String localName, final String qName)
	{
		if(qName.equals("xml3d"))
		{
			return;
		}
		
		if(Boolean.TRUE.equals(this.parentMap.get(qName + this.level)))
		{
			this.indent();
			this.representation.append("</").append(qName).append(">\n");
		}
		else
		{
			this.representation.append("/>\n");
		}

		this.level--;
		this.lastElement = null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void characters(final char[] ch, final int start, final int length ) 
	{ 
		/*
		 * Note that no formatting is performed since this could change the meaning of the data 
		 */
		final String content = String.valueOf(Arrays.copyOfRange(ch, start, start + length));
		
		if(! content.isEmpty())
		{
		    if(Boolean.FALSE.equals(this.parentMap.get(this.lastElement + this.level)))
		    {
		    	this.parentMap.put(this.lastElement + this.level, Boolean.TRUE);
		    	this.representation.append(">\n");
		    }
			
			this.representation.append(content);
		}
	} 
	
	/**
	 * Returns the merged content as XML representation
	 *  
	 * @return XML3D as pure XML representation
	 */
	public String toXML()
	{
		return DOC_START + this.toString();
	}
	
	/**
	 * Returns the merged content embedded in XHTML
	 *  
	 * @return XML3D embedded in XHTML
	 */
	public String toXHTML()
	{
		final String tplContent = Utils.getStringFromClassPathResource(XHTML_TPL);
		return tplContent.replace(TPL_VAR, this.toString());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		final String tplContent = Utils.getStringFromClassPathResource(XML3D_TPL);
		return tplContent.replace(TPL_VAR, this.representation.toString());
	}
}
