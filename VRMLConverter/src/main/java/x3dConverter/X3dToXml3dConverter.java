package x3dConverter;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml3d.converter.x3d.X3DConverter;

public class X3dToXml3dConverter {
	FileReader fr;
	FileWriter fw;
	X3DConverter converter;
	
	public X3dToXml3dConverter(){
		converter = new X3DConverter();
	}
	
	public void convertFile(String fileName) throws ParserConfigurationException, IOException{
		fr = new FileReader(fileName);
		fw = new FileWriter(fileName.substring(0, fileName.indexOf(".x3d"))+".xml3d");
		byte[] content = new String(fr.readFileContent()).getBytes("UTF-8");
		System.out.println("Content is :" + new String(content));
		byte[] result = (converter.TransfomForWS(new ByteArrayInputStream(content))).toByteArray();
		fw.writeXML3DFile(result);
	}
	
	
}
