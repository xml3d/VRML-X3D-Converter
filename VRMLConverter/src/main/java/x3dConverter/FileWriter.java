package x3dConverter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileWriter {
	FileOutputStream fos;
	
	public FileWriter(String filePath) throws FileNotFoundException{
		fos = new FileOutputStream(filePath);
	}
	
	public void writeXML3DFile(byte[] data) throws IOException{
		fos.write(data);
		fos.close();
	}
	
	
}
