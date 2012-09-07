package org.xml3d.converter.x3d;

import java.util.List;

import org.collaviz.jxiot.types.SFColor;
import org.collaviz.jxiot.types.SFVec2f;
import org.collaviz.jxiot.types.SFVec3f;

public class TriMesh {

	private List<Integer> indexBuffer = null;
	private List<SFVec3f> vertexBuffer = null;
	private List<SFColor> colorBuffer = null;;
	private List<SFVec2f> texCoordBuffer = null;;
	private List<SFVec3f> normalBuffer = null;;

	public List<Integer> getIndexBuffer() {
		return indexBuffer;
	}

	public List<SFVec3f> getVertexBuffer() {
		return vertexBuffer;
	}
	
	public List<SFVec3f> getNormalBuffer() {
		return normalBuffer;
	}

	public List<SFColor> getColorBuffer() {
		return colorBuffer;
	}

	public List<SFVec2f> getTextureCoords() {
		return texCoordBuffer;
	}

	public void setVertexBuffer(List<SFVec3f> vertexBuffer) {
		this.vertexBuffer = vertexBuffer;
		
	}

	public void setNormalBuffer(List<SFVec3f> normalBuffer) {
		this.normalBuffer = normalBuffer;
		
	}

	public void setColorBuffer(List<SFColor> colorBuffer) {
		this.colorBuffer = colorBuffer;
	}

	public void setTextureCoords(List<SFVec2f> texCoordStringBuffer) {
		this.texCoordBuffer = texCoordStringBuffer;
		
	}

	public void setIndexBuffer(List<Integer> indexArray) {
		this.indexBuffer = indexArray;
		
	}


}
