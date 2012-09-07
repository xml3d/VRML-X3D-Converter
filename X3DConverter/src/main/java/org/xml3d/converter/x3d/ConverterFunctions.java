package org.xml3d.converter.x3d;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConverterFunctions {

	public static NodeList parseORTOptions(Attr options)
	{
		return ORTFunctions.parseOptions(options);
	}
	
	public static NodeList convertIFS(Node indexedFaceSet, Node coordNode) throws Exception
	{
		return IndexedFaceSet.convert(indexedFaceSet, coordNode);
	}

	public static NodeList convertILS(Node indexedFaceSet, Node coordNode) throws Exception
	{
		return IndexedLineSet.convert(indexedFaceSet, coordNode);
	}

	
	public static NodeList createSphere(Attr radius)
	{
		return Primitives.createSphere(radius);
	}

	public static NodeList createCylinder(Node cylinder)
	{
		return Primitives.createCylinder(cylinder);
	}

	
	public static NodeList createBox(Attr size)
	{
		return Primitives.createBox(size);
	}
	
	public static Attr skipDefault(Attr attr)
	{
		return ConverterUtils.skipDefault(attr);
	}
	
	public static String firstOfMFString(Attr attr)
	{
		return ConverterUtils.firstOfMFString(attr);
	}
	
	public static String getHTTPMFString(Attr attr)
	{
		return ConverterUtils.getHTTPMFString(attr);
	}
	
	
	public static NodeList mergeORTShaderParameters(Node ortAppearance)
	{
		return ORTFunctions.mergeORTShaderParameters(ortAppearance);
	}
	
}
