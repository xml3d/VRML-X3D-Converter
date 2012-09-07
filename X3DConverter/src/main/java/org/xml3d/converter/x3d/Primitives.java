package org.xml3d.converter.x3d;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Primitives {

	private static final int nRings = 10;
	private static final int nSegments = 10;
	
	public static NodeList createBox(Attr size) {
		if (size == null)
			return createBox(2, 2, 2);

		String[] split = size.getNodeValue().trim().split(" ");
		return createBox(Float.parseFloat(split[0]),
				Float.parseFloat(split[1]),
				Float.parseFloat(split[2])
				);
	}
	
	public static NodeList createBox(float sx, float sy, float sz) {

	Node resultNode = ConverterUtils.createNewNode();
	
	sx /= 2.0;
	sy /= 2.0;
	sz /= 2.0;
	
	float[] positions = new float[] { -sx, -sy, -sz, -sx, sy, -sz, sx, sy, -sz,
			sx, -sy, -sz, -sx, -sy, sz, -sx, sy, sz, sx, sy, sz, sx,
			-sy, sz, -sx, -sy, -sz, -sx, -sy, sz, -sx, sy, sz, -sx, sy,
			-sz, sx, -sy, -sz, sx, -sy, sz, sx, sy, sz, sx, sy, -sz,
			-sx, sy, -sz, -sx, sy, sz, sx, sy, sz, sx, sy, -sz, -sx,
			-sy, -sz, -sx, -sy, sz, sx, -sy, sz, sx, -sy, -sz };
	
	
	float[] normals = new float[] { 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0,
			0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, -1, 0, 0, -1, 0, 0, -1, 0,
			0, -1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0,
			0, 1, 0, 0, 1, 0, 0, 1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0,
			-1, 0 };
	
	float[] texCoords = new float[] { 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1,
			1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1,
			0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0 };
	
	int[] indices = new int[] { 0, 1, 2, 2, 3, 0, 4, 7, 5, 5, 7, 6, 8, 9,
			10, 10, 11, 8, 12, 14, 13, 14, 12, 15, 16, 17, 18, 18, 19,
			16, 20, 22, 21, 22, 20, 23 };
	
	MeshUtils.addValueElement(resultNode, null, "int", "index", indices);
	MeshUtils.addValueElement(resultNode, null, "float3", "position", positions);
	MeshUtils.addValueElement(resultNode, null, "float3", "normal", normals);
	MeshUtils.addValueElement(resultNode, null, "float2", "texcoord", texCoords);
	return resultNode.getChildNodes();
	}

	public static NodeList createSphere(Attr radiusAttr) {
		double r = 1;
		if (radiusAttr != null)
		{
			r = Double.parseDouble(radiusAttr.getNodeValue());
		}
		Node resultNode = ConverterUtils.createNewNode();
		
		List<Double> positions = new ArrayList<Double>();
		List<Double> normals = new ArrayList<Double>();
		List<Double> texCoords = new ArrayList<Double>();
		List<Integer> indices = new ArrayList<Integer>();
		
		double fDeltaRingAngle = (Math.PI / nRings);
		double fDeltaSegAngle = (2 * Math.PI / nSegments);
		int wVerticeIndex = 0 ;

		// Generate the group of rings for the sphere
		for( int ring = 0; ring <= nRings; ring++ ) {
			double r0 = r * Math.sin (ring * fDeltaRingAngle);
			double y0 = r * Math.cos (ring * fDeltaRingAngle);

			// Generate the group of segments for the current ring
			for(int seg = 0; seg <= nSegments; seg++) {
				double x0 = r0 * Math.sin(seg * fDeltaSegAngle);
				double z0 = r0 * Math.cos(seg * fDeltaSegAngle);

				// Add one vertex to the strip which makes up the sphere
				positions.add(x0);
				positions.add(y0);
				positions.add(z0);
				
				double length = Math.sqrt(x0*x0 + y0*y0 + z0*z0);
				normals.add(x0 / length);
				normals.add(y0 / length);
				normals.add(z0 / length);
								
				texCoords.add((double) seg / (double) nSegments);
				texCoords.add((double) ring / (double) nRings);			
				
				if (ring != nRings) 
				{
					// each vertex (except the last) has six indices pointing to it
					indices.add(wVerticeIndex + nSegments + 1);
					indices.add(wVerticeIndex);               
					indices.add(wVerticeIndex + nSegments);
					indices.add(wVerticeIndex + nSegments + 1);
					indices.add(wVerticeIndex + 1);
					indices.add(wVerticeIndex);
					wVerticeIndex ++;
				}
			}; // end for seg
		} // end for ring

		MeshUtils.addValueElement(resultNode, null, "int", "index", indices);
		MeshUtils.addValueElement(resultNode, null, "float3", "position", positions);
		MeshUtils.addValueElement(resultNode, null, "float3", "normal", normals);
		MeshUtils.addValueElement(resultNode, null, "float2", "texcoord", texCoords);

		return resultNode.getChildNodes();
	}

	
	public static NodeList createCylinder(Attr height, Attr radius)
	{
		double fHeight = height == null ?  2.0 : Double.parseDouble(height.getNodeValue());
		double fRadius = radius == null ?  1.0 : Double.parseDouble(radius.getNodeValue());
		return createCylinder(fHeight, fRadius);
	}
	
	
	public static NodeList createCylinder(double height, double radius) {
		
		double beta, x, z;
		int sides = 24;
		double delta = 2.0 * Math.PI / sides;
		Node resultNode = ConverterUtils.createNewNode();
		
		List<Vector3d> positions = new ArrayList<Vector3d>();
		List<Vector3d> normals = new ArrayList<Vector3d>();
		List<Vector2d> texCoords = new ArrayList<Vector2d>();
		List<Integer> indices = new ArrayList<Integer>();

		for ( int j = 0, k = 0; j <= sides; j++) {
			beta = j * delta;
			x = Math.sin(beta);
			z = -Math.cos(beta);
			positions.add(new Vector3d(x * radius, -height / 2, z * radius));
			normals.add(new Vector3d(x, 0, z));
			texCoords.add(new Vector2d(1.0 - (double) j / (double) sides, 0));
			positions.add(new Vector3d(x * radius, height / 2, z * radius));
			normals.add(new Vector3d(x, 0, z));
			texCoords.add(new Vector2d(1.0 - (double) j / (double) sides, 1));
			if (j > 0) {
				indices.add(k + 0);
				indices.add(k + 1);
				indices.add(k + 2);
				indices.add(k + 2);
				indices.add(k + 1);
				indices.add(k + 3);
				k += 2;
			}
		}
		if (radius > 0) {
			int base = positions.size();
			for (int j = sides - 1; j >= 0; j--) {
				beta = j * delta;
				x = radius * Math.sin(beta);
				z = -radius * Math.cos(beta);
				positions.add(new Vector3d(x, height / 2, z));
				normals.add(new Vector3d(0, 1, 0));
				texCoords.add(new Vector2d(-x / radius / 2 + 0.5, z / radius / 2 + 0.5));
			}
			int h = base + 1;
			for (int j = 2; j < sides; j++) {
				indices.add(base);
				indices.add(h);
				h = base + j;
				indices.add(h);
			}
			base = positions.size();
			for (int j = sides - 1; j >= 0; j--) {
				beta = j * delta;
				x = radius * Math.sin(beta);
				z = -radius * Math.cos(beta);
				positions.add(new Vector3d(x, -height / 2, z));
				normals.add(new Vector3d(0, -1, 0));
				texCoords.add(new Vector2d(-x / radius / 2 + 0.5, -z / radius / 2 + 0.5));
			}
			h = base + 1;
			for (int j = 2; j < sides; j++) {
				indices.add(h);
				indices.add(base);
				h = base + j;
				indices.add(h);
			}
		}
		MeshUtils.addValueElement(resultNode, null, "int", "index", indices);
		MeshUtils.addValueElementFromVector3d(resultNode, null, "float3", "position", positions);
		MeshUtils.addValueElementFromVector3d(resultNode, null, "float3", "normal", normals);
		MeshUtils.addValueElementFromVector2d(resultNode, null, "float2", "texcoord", texCoords);

		return resultNode.getChildNodes();
	}

	public static NodeList createCylinder(Node cylinder) {
		return createCylinder(
				(Attr)cylinder.getAttributes().getNamedItem("height"), 
				(Attr)cylinder.getAttributes().getNamedItem("radius"));
	}
	
	
}
