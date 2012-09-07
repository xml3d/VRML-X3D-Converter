
package main;

import java.io.*;
import java.text.SimpleDateFormat;
import java.net.*;
import java.lang.Math;
import java.util.*;
import iicm.vrml.vrml2x3d.*;

import javax.xml.parsers.ParserConfigurationException;

import x3dConverter.X3dToXml3dConverter;



public class VRMLConverter
{

  public static void main (String args[])
  {


    if (args.length > 1)
    {
      System.out.println ("usage: java -jar VRMLConverter.jar <fileName.wrl>");
      return;
    }
    else if(args.length == 0){
    	System.out.println ("usage: java -jar VRMLConverter.jar <fileName.wrl>");
        return;
    }
   
    System.out.println("****Vrml2 to XML3D Translator, Version 1.0***");
    //String wrl = args[0];
    //String x3d = args[0].substring(0, args[0].indexOf(".wrl")) + ".x3d";
    String[] argument = new String[2];
    argument[0] = args[0];
    argument[1] = args[0].substring(0, args[0].indexOf(".wrl")) + ".x3d";
    
    try
    {
    	vrml2x3d.main(argument);
    
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return;
    }
    try {
		convertX3dToXml3d(argument[1]);
	} catch (ParserConfigurationException e) {
		e.printStackTrace();
		return;
	} catch (IOException e) {
		e.printStackTrace();
		return;
	}
  }
  
  public static void convertX3dToXml3d(String x3dFile) throws ParserConfigurationException, IOException{
	  X3dToXml3dConverter x3dConverter = new X3dToXml3dConverter();
	  x3dConverter.convertFile(x3dFile);
  }

  
}
