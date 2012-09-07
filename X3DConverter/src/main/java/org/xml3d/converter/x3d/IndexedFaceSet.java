package org.xml3d.converter.x3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.collaviz.jxiot.types.MFColor;
import org.collaviz.jxiot.types.MFColorRGBA;
import org.collaviz.jxiot.types.MFVec2f;
import org.collaviz.jxiot.types.MFVec3f;
import org.collaviz.jxiot.types.SFColor;
import org.collaviz.jxiot.types.SFColorRGBA;
import org.collaviz.jxiot.types.SFVec2f;
import org.collaviz.jxiot.types.SFVec3f;
import org.collaviz.jxiot.types.X3DXMLDatatypeFactoryImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class IndexedFaceSet {

	private static int meshCount = 0;
	
	public static class VertexAttribute<T> {
		String name;
		int[] index;
		T values;
		boolean perVertex;
	}
	
	public static class Face {
		public List<Integer> indices = new ArrayList<Integer>();
	}
	public static class Triangle {
		public int[] vertices = new int[3];
	}
	public static class Vertex {
		int position, normal, texCoord, color;

		@Override
		public boolean equals(Object obj) {
			if(this == obj)
				return true;
			if((obj == null) || (obj.getClass() != this.getClass()))
				return false;

			Vertex other = (Vertex) obj;
			return (this.position == other.position)
				   && (this.normal == other.normal)
				   && (this.texCoord == other.texCoord)
				   && (this.color == other.color);
		}

		@Override
		public int hashCode() {
			return position + 31* normal + 63*texCoord + 127*color;
		}
		
	}


	private static String       sSeparator     = "[,\\s]+";
    static final Pattern pattern = Pattern.compile(sSeparator);
	static X3DXMLDatatypeFactoryImpl factory = new X3DXMLDatatypeFactoryImpl();
    
    
    static NodeList convert(Node indexedFaceSet, Node coordNode) throws Exception
	{
		if (indexedFaceSet == null || !"IndexedFaceSet".equals(indexedFaceSet.getNodeName()))
			return null;
		meshCount++;
		
		Node resultNode = ConverterUtils.createNewNode();
		System.err.println("Creating mesh (" + getAttribValue(indexedFaceSet, "DEF") + ")");
		if(coordNode == null) {
			System.err.println("No Coordinate Node given");
			return null;
		}
		String nodeName = coordNode.getNodeName();
		if (!("Coordinate".equals(nodeName) || "GeoCoordinate".equals(nodeName)))
		{
			System.err.println("Unknown type for IndexFaceSet Coordinates: " + nodeName);
			return null;
		}
		
		if (ConverterUtils.DEBUG && meshCount > 1)
			return resultNode.getChildNodes();
		
		
		VertexAttribute<MFVec3f> position = new VertexAttribute<MFVec3f>();
		position.name = getAttribValue(coordNode, "DEF");
		position.index = toInt32Array(getAttribValue(indexedFaceSet, "coordIndex"));
		position.values = factory.getMFVec3fFromString(getAttribValue(coordNode, "point"));
		position.perVertex = true;
		
		if (position.index == null || position.index.length == 0)
		{
			System.err.println("No Coordinate index found");
			return null;
		}
		
		if (position.values == null || position.values.size() == 0)
		{
			System.err.println("No Coordinate values found");
			return null;
		}
		
		
		VertexAttribute<MFVec3f> normal = new VertexAttribute<MFVec3f>();
		normal.name = getAttribValue(findChild(indexedFaceSet, "Normal"), "DEF");
		normal.index = toInt32Array(getAttribValue(indexedFaceSet, "normalIndex"));
		normal.values = factory.getMFVec3fFromString(getAttribValue(findChild(indexedFaceSet, "Normal"), "vector"));
		String perVertexString = getAttribValue(indexedFaceSet, "normalPerVertex");
		normal.perVertex =  !"false".equals(perVertexString);

		VertexAttribute<MFColor> color = new VertexAttribute<MFColor>();
		color.name = getAttribValue(findChild(indexedFaceSet, "Color"), "DEF");
		color.index = toInt32Array(getAttribValue(indexedFaceSet, "colorIndex"));
		color.values = getColorValues(indexedFaceSet);
		perVertexString = getAttribValue(indexedFaceSet, "colorPerVertex");
		color.perVertex =  !"false".equals(perVertexString);

		VertexAttribute<MFVec2f> texCoord = new VertexAttribute<MFVec2f>();
		texCoord.name = getAttribValue(findChild(indexedFaceSet, "TextureCoordinate"), "DEF");
		texCoord.index = toInt32Array(getAttribValue(indexedFaceSet, "texCoordIndex"));
		texCoord.values = factory.getMFVec2fFromString(getAttribValue(findChild(indexedFaceSet, "TextureCoordinate"), "point"));
		texCoord.perVertex =  true;

		String ccwString = getAttribValue(indexedFaceSet, "ccw");
		Boolean ccw =  ccwString == null ? Boolean.TRUE : Boolean.getBoolean(ccwString);
		
		String creaseString = getAttribValue(indexedFaceSet, "creaseAngle");
		float creaseAngle = creaseString == null ? 0.0f : Float.parseFloat(creaseString);

		
		try {
		createMesh( resultNode, position, normal, color, texCoord, ccw, creaseAngle );
		} catch (Exception e) {
			System.err.println("Failed.");
			e.printStackTrace();
			System.exit(1);
		}
		
		return resultNode.getChildNodes();
	}


	private static MFColor getColorValues(Node indexedFaceSet) throws Exception {
		Node color = findChild(indexedFaceSet, "Color");
		if (color != null) {
			System.err.println("Found Color values");
			return factory.getMFColorFromString(getAttribValue(color, "color"));
		}
		color = findChild(indexedFaceSet, "ColorRGBA");
		if (color != null) {
			MFColorRGBA aColor = factory.getMFColorRGBAFromString(getAttribValue(color, "color"));
			if (color != null) {
				float[] colors = new float[aColor.size()*3];
				int i = 0;
				for (SFColorRGBA sfColorRGBA : aColor) {
					colors[i++] = sfColorRGBA.r;
					colors[i++] = sfColorRGBA.g;
					colors[i++] = sfColorRGBA.b;
				}
				System.err.println("Found ColorRGBA values");
				return new MFColor(colors);
			}
		}
		return new MFColor(new float[0]);
	}


	private static void createMesh(
			Node parent,
			VertexAttribute<MFVec3f> position,
			VertexAttribute<MFVec3f> normal,
			VertexAttribute<MFColor> color,
			VertexAttribute<MFVec2f> texCoord,
			boolean ccw, float creaseAngle) {
		
		boolean calcPointNormals = normal.values.size() == 0 && normal.perVertex;
		boolean hasTextureCoordinates = texCoord.values.size() != 0;
		
		if(calcPointNormals)
			System.err.println("Calculating point normals.");
		
		
		List<Face> faces = new ArrayList<Face>();
		Face f = new Face();
		
		for (int i = 0; i < position.index.length; i++) {
			if(position.index[i] == -1) {
				faces.add(f);
				f = new Face();
			}
			else
				f.indices.add(i);
		}
		// Not closing with -1
		if (!f.indices.isEmpty())
			faces.add(f);
		
		List<Vertex> vertices = new ArrayList<Vertex>();
		//List<Triangle> triangles = new ArrayList<Triangle>();
		
		Map<Vertex, Integer> vertexMap = new HashMap<Vertex, Integer>();

		List<Integer> indexArray = new ArrayList<Integer>();
		// triangulate and expand vertices
		int faceNr = 0;
		int indexCount = 0;
		for(Face face : faces) {
			int triVertNr = 0;
			int[] triVerts = new int[] { -1, -1 };
			
			triVertNr = 0;
			for (Integer index : face.indices) {
				Vertex vert = new Vertex();

				// get full indices for vertex data
				vert.position = position.index[index];
				vert.normal = normal.values.size() != 0 ? getIndex(position.index, normal.index, normal.perVertex, faceNr, index) : 0;
				vert.color = color.values.size() != 0 ? getIndex(position.index, color.index, color.perVertex, faceNr, index) : 0;
				vert.texCoord = hasTextureCoordinates ? getIndex(position.index, texCoord.index, true, faceNr, index) : 0;

				// avoid duplication
				int vpos;
				if (vertexMap.containsKey(vert))
				{
					vpos = vertexMap.get(vert);
				}
				else
				{
					 vpos = vertices.size();
					 vertexMap.put(vert, Integer.valueOf(vpos));
					 vertices.add(vert);
				}

				// emit triangle (maybe)
				if (triVertNr == 0)
					triVerts[0] = vpos;
				else if (triVertNr == 1)
					triVerts[1] = vpos;
				else {
					indexArray.add(triVerts[0]);
					if (ccw)
					{
						indexArray.add(triVerts[1]);
						indexArray.add(vpos);
					}
					else
					{
						indexArray.add(vpos);
						indexArray.add(triVerts[1]);
					}
					indexCount+=3;
					triVerts[1] = vpos;
				}
				triVertNr++;
			} // indices
			
			faceNr++;
		} // faces
		MeshUtils.addValueElement(parent, null, "int", "index", indexArray);
		
		System.out.println("Mesh has " + faces.size() + " faces.");
		System.out.println("Mesh has " + indexCount + " indices.");
		System.out.println("Mesh has " + vertices.size() + " vertices.");
		System.out.println("Mesh has " + texCoord.values.size() + " texCoords.");
		System.out.println("Mesh has " + color.values.size() + " colors.");
		

		
		List<SFVec3f> positionBuffer = new ArrayList<SFVec3f>();
		List<SFVec3f> normalBuffer = new ArrayList<SFVec3f>();
		List<SFColor> colorBuffer = new ArrayList<SFColor>();
		List<SFVec2f> texcoordBuffer = new ArrayList<SFVec2f>();
		
		long vertCount = 0;
		for ( Vertex v : vertices ) {
			positionBuffer.add(position.values.get(v.position));

			if (normal.values.size() != 0) {
				normalBuffer.add(normal.values.get(v.normal));
			}
			if (color.values.size() != 0) {
				colorBuffer.add(color.values.get(v.color));
			}
			if (texCoord.values.size() != 0) {
				texcoordBuffer.add(texCoord.values.get(v.texCoord));
			}
			vertCount++;
		}
		
		if(calcPointNormals) {
			NormalGenerator n = new NormalGenerator();
			TriMesh tm = new TriMesh();
			tm.setIndexBuffer(indexArray);
			tm.setVertexBuffer(positionBuffer);
			if (colorBuffer.size() > 0)
				tm.setColorBuffer(colorBuffer);
			if (texcoordBuffer.size() > 0)
				tm.setTextureCoords(texcoordBuffer);
			System.err.println("Generating point normals.");
			n.generateNormals(tm, creaseAngle);
			
			positionBuffer = tm.getVertexBuffer();
			normalBuffer = tm.getNormalBuffer();
			if (colorBuffer.size() > 0)
				colorBuffer = tm.getColorBuffer();
			if (texcoordBuffer.size() > 0)
				texcoordBuffer = tm.getTextureCoords();
			
		}
		
		addValueElement(parent, position.name, "float3", "position", positionBuffer);
		if (normalBuffer.size() != 0)
			addValueElement(parent, normal.name, "float3", "normal", normalBuffer);
		else
			addPointNormals(parent, indexArray, positionBuffer);
		if (color.values.size() != 0)
			addValueElement(parent, normal.name, "float3", "color", colorBuffer);
		if (texCoord.values.size() != 0)
			addValueElement(parent, normal.name, "float2", "texcoord", texcoordBuffer);

		
		
	}

	private static void addValueElement(Node parent, String id, String type,
			String name, List<?> values) {
		
		Element valueElement = parent.getOwnerDocument().createElementNS(ConverterUtils.XML3D_NAMESPACE, type);
		if (id != null)
			valueElement.setAttribute("id", id);
		valueElement.setAttribute("name", name);
		
		Text textNode = null;
		StringBuffer textStr = new StringBuffer();
		for(Object o : values) {
			textStr.append(o.toString());
			textStr.append(' ');
		}
		if (ConverterUtils.DEBUG)
			textNode = valueElement.getOwnerDocument().createTextNode(textStr.substring(0, 30).concat("..."));
		else
			textNode = valueElement.getOwnerDocument().createTextNode(textStr.toString());
		valueElement.appendChild(textNode);
		parent.appendChild(valueElement);
		
	}




	public static void addPointNormals(Node parent, List<Integer> indexArray, List<SFVec3f> positionsArray) {
		
		List<SFVec3f> normalArray = new ArrayList<SFVec3f>();
		List<List<SFVec3f>> faceNormals = new ArrayList<List<SFVec3f>>();
		for(int i = 0; i < positionsArray.size(); i++)
		{
			faceNormals.add(new ArrayList<SFVec3f>());
		}
		
		for (int i = 0; i < indexArray.size(); i+=3)
		{
			Integer index = indexArray.get(i);
			SFVec3f x = new SFVec3f(positionsArray.get(index));
			index = indexArray.get(i+1);
			SFVec3f y = new SFVec3f(positionsArray.get(index));
			index = indexArray.get(i+2);
			SFVec3f z = new SFVec3f(positionsArray.get(index));
			
			SFVec3f a = new SFVec3f();
			SFVec3f.sub(x, y, a);
			if (a.length() < 0.001)
			{
				// degenerated
				continue;
			}
			
			SFVec3f b = new SFVec3f();
			SFVec3f.sub(y, z, b);
			if (b.length() < 0.001)
			{
				// degenerated
				continue;
			}
			a.normalizeLocal();
			b.normalizeLocal();
		
			if(Math.abs(a.dot(b)) >= 1.0)
			{
				continue;
			}
			
			SFVec3f normal = new SFVec3f(a);
			normal.crossLocal(b);
			if (normal.length() < 0.001)
			{
				// degenerated
				continue;
			}
			normal.normalizeLocal();
			
			if (new Float(normal.x).equals(Float.NaN))
			{
				System.out.println("x NAN");
				System.exit(1);
			}
			if (new Float(normal.y).equals(Float.NaN))
			{
				System.out.println("y NAN");
				System.exit(1);
			}
			if (new Float(normal.z).equals(Float.NaN))
			{
				System.out.println("z NAN");
				System.exit(1);
			}
			faceNormals.get(indexArray.get(i)).add(normal);
			faceNormals.get(indexArray.get(i+1)).add(normal);
			faceNormals.get(indexArray.get(i+2)).add(normal);
		}
		
		// Now average over the collected face normals to get point normals
		for (int i = 0; i < positionsArray.size(); i++) {
			SFVec3f n = new SFVec3f();
			for (int j = 0; j < faceNormals.get(i).size(); j++) {
				n.addLocal(faceNormals.get(i).get(j));
			}
			n.normalizeLocal();
			
			normalArray.add(n);
		}
		MeshUtils.addValueElement(parent, null, "float3", "normal", normalArray);
	}




	private static int getIndex(int[] coordIndex, int[] vec,
			boolean perVertex, int faceNr, Integer index) {
		if (!perVertex) {
			if (vec.length != 0)
				return vec[faceNr];
			else
				return faceNr;
		} else {
			if (vec.length != 0)
				return vec[index];
			else
				return coordIndex[index];
		}
	}





	static int[] toInt32Array(String initialValue) {
		
		if (initialValue == null)
			return new int[0];
		
		initialValue = initialValue.trim();
        if (initialValue.isEmpty())
            return new int[0];
        
		String[] splits = pattern.split(initialValue);
        int[] data = new int[splits.length];
        for(int i = 0; i< splits.length; i++)
        {
            data[i] = Integer.parseInt(splits[i]);
        }
		return data;
	}



	public static String getAttribValue(Node node, String name) {
		if (node == null || name == null || name.isEmpty())
			return null;
		Node attr =	node.getAttributes().getNamedItem(name);
		return attr == null ? null : attr.getNodeValue();		
	}

	static Node findChild(Node parent, String name) {
		for (int i = 0; i < parent.getChildNodes().getLength(); i++) {
			Node child = parent.getChildNodes().item(i);
			if (child.getNodeName().equals(name))
				return child;
		}
		return null;
	}
	
	
	

}
