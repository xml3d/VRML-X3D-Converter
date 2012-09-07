package org.xml3d.converter.x3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class ORTFunctions {

	private static String       sSeparator     = "[,\\s]+";
    static final Pattern pattern = Pattern.compile(sSeparator);
    
	static class Option {
		public String type;
		public String value;
	}
	
	@SuppressWarnings("serial")
	static class OptionMap  extends HashMap<String, Option>
	{
		
	}
	
	public static NodeList parseOptions(Attr options) {
		Node parent = ConverterUtils.createNewNode();
		OptionMap optionList = parseOptions(options.getNodeValue().trim());
		for(Entry<String, Option> option : optionList.entrySet())
		{
			parent.appendChild(createBindFromOption(parent, option));
		}
		return parent.getChildNodes();
		
	}

	private static Node createBindFromOption(Node parent, Entry<String, Option> entry) {
		Element bind = parent.getOwnerDocument().createElementNS(ConverterUtils.XML3D_NAMESPACE, "bind");
		bind.setAttribute("semantic", entry.getKey());
		Element typeElement = parent.getOwnerDocument().createElementNS(ConverterUtils.XML3D_NAMESPACE, entry.getValue().type);
		bind.appendChild(typeElement);
		Text textNode = parent.getOwnerDocument().createTextNode(entry.getValue().value);
		typeElement.appendChild(textNode);
		return bind;
	}

	private static OptionMap parseOptions(String nodeValue) {
		OptionMap result = new OptionMap();
		List<String> tokens = new ArrayList<String>();
		
		boolean in = false;
		String token = "";
		for(int i = 0; i < nodeValue.length(); i++)
		{
			char c = nodeValue.charAt(i);
			
			if (in)
			{
				if (c == '"')
				{
					in = !in;
					//System.err.println("Adding token: " + token);
					tokens.add(token);
					token = "";
				}
				else
					token += c;
			}
			else
				if (c == '"')
				{
					in = !in;
				}	
		}
		for(String optionStr : tokens)
		{
			Option option = new Option();
			String[] s = optionStr.trim().split(sSeparator);
			if (s.length < 3)
			{
				System.err.println("Invalid option format: " + optionStr);
			}
			
			String name = s[1];
			option.type = convertType(s[0]);
			option.value = "";
			int attrLength = getTypeLength(option.type, s.length);
			for(int i = 2; i < attrLength;i++)
			{
				if (s[i] != null)
				{
					option.value += s[i];
					if (i!=attrLength-1)
						option.value += " ";
				}
			}
			result.put(name, option);
		}
		return result;
	}

	private static int getTypeLength(String type, int all) {
		if ("float".equals(type))
			return 3;
		if ("float3".equals(type))
			return 5;
		
		return all;
	}

	private static String convertType(String otype) {
		if ("1f".equals(otype))
			return "float";
		if ("3f".equals(otype) || "color".equals(otype))
			return "float3";
		if ("bool".equals(otype))
			return otype;
		System.err.println("Unknown type: " + otype);
		return otype;
	}

	public static NodeList mergeORTShaderParameters(Node ortAppearance) {
		if (ortAppearance == null || !"ORTAppearance".equals(ortAppearance.getNodeName()))
			return null;
		
		//System.err.println("Merging...");
		
		Node shader = null;
		Node material = null;
		NodeList childNodes = ortAppearance.getChildNodes();
	
		for (int i = 0; i < childNodes.getLength(); i++)
		{
			Node tmp = childNodes.item(i);
			if ("Material".equals(tmp.getNodeName()))
				material = tmp;
			if ("ORTGeneralShader".equals(tmp.getNodeName()))
				shader = tmp;
		}
		return merge(material, shader);
	}

	private static NodeList merge(Node material, Node shader) {
		Node options = shader == null ? null : shader.getAttributes().getNamedItem("options");
		OptionMap shaderParameter = new OptionMap();
		if (options != null)
			shaderParameter = parseOptions(options.getNodeValue().trim());
		
		OptionMap materialParameter = createMaterialParameters(material);
		materialParameter.putAll(shaderParameter); // Overwrites materialParameter with same key
		
		Node parent = ConverterUtils.createNewNode();
		for(Entry<String, Option> option : materialParameter.entrySet())
		{
			//System.err.println("Adding: " + option.getKey());
			parent.appendChild(createBindFromOption(parent, option));
		}
		return parent.getChildNodes();
	}

	private static OptionMap createMaterialParameters(Node material) {
		OptionMap materialParameter = new OptionMap();
		if (material != null)
		{
			addMaterialParameter(materialParameter, material, "ambientIntensity", "float");
			addMaterialParameter(materialParameter, material, "diffuseColor", "float3");
			addMaterialParameter(materialParameter, material, "emissiveColor", "float3");
			addMaterialParameter(materialParameter, material, "shininess", "float");
			addMaterialParameter(materialParameter, material, "specularColor", "float3");
			addMaterialParameter(materialParameter, material, "transparency", "float");
		}
		return materialParameter;
	}

	private static void addMaterialParameter(
			OptionMap map, Node material, String name, String type) {
		
		Node n = material.getAttributes().getNamedItem(name);
		if (n==null)
			return;
		Option o = new Option();
		o.type = type;
		o.value = n.getNodeValue();
		map.put(name, o);
	}

}
