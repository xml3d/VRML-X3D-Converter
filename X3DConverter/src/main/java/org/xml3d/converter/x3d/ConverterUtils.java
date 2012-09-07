package org.xml3d.converter.x3d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class ConverterUtils {
	
	public static final String XML3D_NAMESPACE = "http://www.xml3d.org/2009/xml3d";
	public static boolean DEBUG = false;
	
	
	static {
		if ("true".equals(System.getProperty("debug")))
			DEBUG = true;
	}
	
	public static Node createNewNode() {
		Node resultNode = null;
		try {
			resultNode = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().createElement("Test");
			//DocumentBuilderFactory newInstance = net.sf.saxon.dom.DocumentBuilderFactoryImpl.newInstance();
			//resultNode = newInstance.newDocumentBuilder().newDocument().createElement("Test");
			
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return resultNode;
	}

	public static Attr skipDefault(Attr attr) {
		if (attr == null || attr.getNodeValue().isEmpty() )
			return null;
		
		String[] strValues = attr.getNodeValue().trim().split("[,\\s]+");
		System.out.println("Node value: "+attr.getNodeValue().trim());
		for(int i = 0; i < strValues.length; i++){
			System.out.println(i+"th element: "+strValues[i]);
		}
		Float[] floatValues = new Float[strValues.length];
		for (int i = 0; i < strValues.length; i++)
		{
			floatValues[i] = Float.parseFloat(strValues[i]);
		}
		if (
				("translation".equals(attr.getName())
				|| "center".equals(attr.getName()))
				&& floatValues.length == 3
		   )
		{
			return floatValues[0].equals(0.0f) 
				&& floatValues[1].equals(0.0f)
				&& floatValues[2].equals(0.0f) ? null : attr;
		}
		if (("rotation".equals(attr.getName())
			|| "scaleOrientation".equals(attr.getName()))
				&& floatValues.length == 4)
		{
			return floatValues[0].equals(0.0f) 
				&& floatValues[1].equals(0.0f)
				&& floatValues[2].equals(1.0f) 
				&& floatValues[3].equals(0.0f) ? null : attr;
		}
		if ("scale".equals(attr.getName()) && floatValues.length == 3)
		{
			return floatValues[0].equals(1.0f) 
				&& floatValues[1].equals(1.0f)
				&& floatValues[2].equals(1.0f) ? null : attr;
		}
		return attr;
	}

	public static String firstOfMFString(Attr attr) {
		List<String> stringList = getMFStrings(attr);
		return stringList.size() > 0 ? stringList.get(0) : "";
	}

	private static List<String> getMFStrings(Attr attr) {
		if (attr == null || attr.getNodeValue().isEmpty())
			return Collections.emptyList();
		String value = attr.getValue();
		if (value.indexOf('"') == -1)
			return Collections.singletonList(value);
		
		ArrayList<String> result = new ArrayList<String>();
		while (value.indexOf('"') != -1)
		{
			value = value.substring(value.indexOf('"') +1);
			if (value.indexOf('"') != -1)
			{
				result.add(value.substring(0, value.indexOf('"')));
				value = value.substring(value.indexOf('"')+1);
			}
		}
		return result;
	}

	public static String getHTTPMFString(Attr attr) {
		if (attr == null)
			return "";
		List<String> stringList = getMFStrings(attr);
		for(String url : stringList)
		{
			if (url.startsWith("http://"))
				return url;
		}
		String result = stringList.size() > 0 ? stringList.get(0) : "";
		if (result.endsWith(".ppm"))
			result = result.trim().split("\\.")[0] + ".png";
		return result;
	}



}
