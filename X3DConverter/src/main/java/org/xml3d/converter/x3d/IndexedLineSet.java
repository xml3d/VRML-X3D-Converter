package org.xml3d.converter.x3d;

import java.util.ArrayList;

import org.collaviz.jxiot.types.MFColor;
import org.collaviz.jxiot.types.MFVec3f;
import org.collaviz.jxiot.types.X3DXMLDatatypeFactoryImpl;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml3d.converter.x3d.IndexedFaceSet.VertexAttribute;

public class IndexedLineSet {
	static X3DXMLDatatypeFactoryImpl factory = new X3DXMLDatatypeFactoryImpl();

	public static NodeList convert(Node indexedLineSet, Node coordNode)
			throws Exception {

		if (indexedLineSet == null
				|| !"IndexedLineSet".equals(indexedLineSet.getNodeName()))
			return null;

		Node resultNode = ConverterUtils.createNewNode();
		System.err.println("Creating mesh from LineSet ("
				+ IndexedFaceSet.getAttribValue(indexedLineSet, "DEF") + ")");

		if (coordNode == null) {
			System.err.println("No Coordinate Node given");
			return null;
		}
		String nodeName = coordNode.getNodeName();
		if (!("Coordinate".equals(nodeName) || "GeoCoordinate".equals(nodeName))) {
			System.err.println("Unknown type for IndexLineSet Coordinates: "
					+ nodeName);
			return null;
		}

		VertexAttribute<MFVec3f> position = new VertexAttribute<MFVec3f>();
		position.name = IndexedFaceSet.getAttribValue(coordNode, "DEF");
		position.index = IndexedFaceSet.toInt32Array(IndexedFaceSet
				.getAttribValue(indexedLineSet, "coordIndex"));
		position.values = factory.getMFVec3fFromString(IndexedFaceSet
				.getAttribValue(coordNode, "point"));
		position.perVertex = true;

		if (position.index == null || position.index.length == 0) {
			System.err.println("No Coordinate index found");
			return null;
		}

		if (position.values == null || position.values.size() == 0) {
			System.err.println("No Coordinate values found");
			return null;
		}

		VertexAttribute<MFColor> color = new VertexAttribute<MFColor>();
		color.name = IndexedFaceSet.getAttribValue(IndexedFaceSet.findChild(
				indexedLineSet, "Color"), "DEF");
		color.index = IndexedFaceSet.toInt32Array(IndexedFaceSet
				.getAttribValue(indexedLineSet, "colorIndex"));
		color.values = factory.getMFColorFromString(IndexedFaceSet
				.getAttribValue(IndexedFaceSet.findChild(indexedLineSet,
						"Color"), "color"));
		String perVertexString = IndexedFaceSet.getAttribValue(indexedLineSet,
				"colorPerVertex");
		color.perVertex = !"false".equals(perVertexString);

		try {
			createMesh(resultNode, position, color);
		} catch (Exception e) {
			System.err.println("Failed.");
			e.printStackTrace();
			System.exit(1);
		}

		return resultNode.getChildNodes();
	}

	private static void createMesh(Node parent,
			VertexAttribute<MFVec3f> position, VertexAttribute<MFColor> color) {
		boolean hasColors = color.values != null && color.values.size() > 0;
		System.err.println(hasColors);
		if(!hasColors) {
			addSplitIndex(parent, position.index);
			MeshUtils.addValueElement(parent, null, "float3", "position", position.values);
		}
		else {
			if (!color.perVertex) {
				System.err.println("No support for color per line (yet).");
				return;
			}
			if (color.index != null && color.index.length > 0)
			{
				System.err.println("No support for indexed vertex colors (yet).");
				return;
			}
			addSplitIndex(parent, position.index);
			MeshUtils.addValueElement(parent, null, "float3", "position", position.values);
			MeshUtils.addValueElement(parent, null, "float3", "color", color.values);

		}

	}

	private static void addSplitIndex(Node parent, int[] index) {
		ArrayList<Integer> indices = new ArrayList<Integer>();
		ArrayList<Integer> sizes = new ArrayList<Integer>();

		int size = 0;
		for (int i = 0; i < index.length; i++) {
			if (index[i] == -1) {
				sizes.add(size);
				size = 0;
			} else {
				indices.add(index[i]);
				size++;
			}
		}
		MeshUtils.addValueElement(parent, null, "int", "index", indices);
		MeshUtils.addValueElement(parent, null, "int", "size", sizes);

	}

}
