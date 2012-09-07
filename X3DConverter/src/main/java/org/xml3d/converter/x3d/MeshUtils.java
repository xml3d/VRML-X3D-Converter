package org.xml3d.converter.x3d;

import java.util.List;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class MeshUtils {

	public static void addValueElement(Node parent,  String id, String type, String name, int[] values) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < values.length; i++)
			{
			buffer.append(values[i]);
			buffer.append(' ');
			}
		addValueElement(parent, id, type, name, buffer.toString());
	}
	
	public static void addValueElement(Node parent, String id, String type,
			String name, float[] values) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < values.length; i++)
			{
			buffer.append(values[i]);
			buffer.append(' ');
			}
		addValueElement(parent, id, type, name, buffer.toString());
	}
	
	public static void addValueElement(Node parent, String id, String type, String name, String values) {
		
		Element valueElement = parent.getOwnerDocument().createElementNS(ConverterUtils.XML3D_NAMESPACE, type);
		valueElement.setAttribute("name", name);
		if (id != null)
			valueElement.setAttribute("id", id);
		Text textNode = null;
		
		if (ConverterUtils.DEBUG)
			textNode = valueElement.getOwnerDocument().createTextNode(values.substring(0, 30).concat("..."));
		else
			textNode = valueElement.getOwnerDocument().createTextNode(values);
		
		valueElement.appendChild(textNode);
		parent.appendChild(valueElement);
	}

	public static void addValueElement(Node parent, String id, String type, String name, List<?> values) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < values.size(); i++)
			{
			buffer.append(values.get(i));
			buffer.append(' ');
			}
		addValueElement(parent, id, type, name, buffer.toString());
	}
	
	public static void addValueElementFromVector3d(Node parent, String id, String type, String name, List<Vector3d> values) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < values.size(); i++)
			{
			Vector3d v = values.get(i);
			buffer.append(v.x);
			buffer.append(' ');
			buffer.append(v.y);
			buffer.append(' ');
			buffer.append(v.z);
			buffer.append(' ');
			}
		addValueElement(parent, id, type, name, buffer.toString());
	}

	public static void addValueElementFromVector2d(Node parent, String id, String type, String name, List<Vector2d> values) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < values.size(); i++)
			{
			Vector2d v = values.get(i);
			buffer.append(v.x);
			buffer.append(' ');
			buffer.append(v.y);
			buffer.append(' ');
			}
		addValueElement(parent, id, type, name, buffer.toString());
	}
}
