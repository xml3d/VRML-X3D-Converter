package org.xml3d.merger;

import java.util.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * MergeFilter is utilized by {@link XML3DMerger} to ensure 
 * unique ID attributes and to keep the corresponding references
 * consistent over multiple processed XML3D files. If an id is found
 * in file B which is already used in file A this id is changed 
 * to <id_name>_<random number>. For example: id "myUniqueId" becomes
 * "myUniqueId_23236291". According to this, all corresponding references
 * are updated. Example:<b><br>
 * <b>From</b><br/>
 * <code>
 * 	 <group id="group" transform="#myUniqueId">
 *     	<use href="#mySphere"/>
 *   </group>
 * <br/><b>To</b><br/>
 * <code>
 * 	 <group id="group" transform="#myUniqueId_23236291">
 *     	<use href="#mySphere"/>
 *   </group> 
 * </code>
 * 
 * @author  Benjamin Friedrich
 * @version 1.0  08/2010
 */
final class MergeFilter extends XMLFilterImpl
{
	private final List<String>        knownIds;
	private final Map<String, String> replaceMap;
	private final Random  			  rand;
	
	/**
	 * Constructor of MergeFilter
	 */
	public MergeFilter()
	{
		this.knownIds   = new ArrayList<String>();
		this.replaceMap = new HashMap<String, String>();
		this.rand       = new Random();
	}
	
	/**
	 * Changes the value of an attribute.
	 * 
	 * @param attributes
	 * 				{@link Attributes} instance in which contains the attribute to be changed
	 * @param qName
	 * 				qualified name of the attribute
	 * @param value
	 * 				new attribute value
	 * 
	 * @return 
	 * 		{@link Attributes} instance with the changed attribute value
	 * 
	 * @throws IllegalArgumentException
	 * 				if attribute with the name specified in qName does not exist			
	 */
	private Attributes changeAttributeValue(final Attributes attributes, final String qName, final String value)
	{
		final AttributesImpl attributesImpl = new AttributesImpl(attributes);
		final int            attrIndex      = attributesImpl.getIndex(qName);
		
		if(attrIndex == -1)
		{
			throw new IllegalArgumentException("Value of attribute " + qName + " can not be changed because it does not exist");
		}
		
		attributesImpl.setValue(attrIndex, value);
		return attributesImpl;
	}
	
	/**
	 * Searches for id attributes and handles them as mentioned in the
	 * {@link MergeFilter} description
	 * 
	 * @param  attributes  
	 * 					Attributes
	 * 
	 * @return @link {@link Attributes} instance with eventually changed id values
	 */
	private Attributes handleIdAttribute(final Attributes attributes) 
	{
		final String id = attributes.getValue("id");
		if(id != null && ! id.trim().isEmpty())
		{
			if(this.knownIds.contains(id))
			{
				final String newId;

				if(this.replaceMap.containsKey(id))
				{
					// should never happen because an id must be unique and
					// the xml3d schema validation would fail, if an id is
					// duplicate
					newId = this.replaceMap.get(id);
				}
				else
				{
					newId = id + "_" + String.valueOf(this.rand.nextInt(Integer.MAX_VALUE));
					this.replaceMap.put(id, newId);
				}

				return this.changeAttributeValue(attributes, "id", newId);
			}
			else
			{
				this.knownIds.add(id);
			}
		}
		return attributes;
	}
	
	/**
	 * Searches for id references and handles them as mentioned in the
	 * {@link MergeFilter} description
	 * 
	 * @param  attributes  
	 * 					Attributes
	 * 
	 * @return @link {@link Attributes} instance with eventually changed references
	 */
	private Attributes handleReferencedAttributes(Attributes attributes)
	{
		for(int i = 0; i < attributes.getLength(); i++)
		{
			final String value = attributes.getValue(i);
			if(value.startsWith("#"))
			{
				final String refId = value.replace("#", "");
				if(this.replaceMap.containsKey(refId))
				{
					final String newId = "#" + this.replaceMap.get(refId);
					attributes = this.changeAttributeValue(attributes, attributes.getQName(i), newId);
				}
			}
		}
		
		
		return attributes;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException 
	{
		if(! qName.equals("xml3d"))
		{
			final Attributes attrsPrepId        = this.handleIdAttribute(attributes);
			final Attributes attrsPrepHrefAndId = this.handleReferencedAttributes(attrsPrepId);
			super.startElement(uri, localName, qName, attrsPrepHrefAndId);
		}
		else
		{
			super.startElement(uri, localName, qName, attributes);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endDocument() 
	{
		this.replaceMap.clear();
	}
}
