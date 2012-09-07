package x3dConverter;

//import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.io.InputStreamReader;

public class FileReader {
	  FileInputStream fis;
	  // Get the object of DataInputStream
	  DataInputStream dis;
	  //BufferedReader br ;

	  public FileReader(String file) throws FileNotFoundException{
		  fis = new FileInputStream(file);
		  dis = new DataInputStream(fis);
		  //new BufferedReader(new InputStreamReader(dis));
	  }
	  
	  public byte[] readFileContent() throws IOException{
		  
		  byte content[] = new byte[dis.available()];
		  dis.read(content, 0, content.length);
		  dis.close();
		  return content;
		  
	  }
}
