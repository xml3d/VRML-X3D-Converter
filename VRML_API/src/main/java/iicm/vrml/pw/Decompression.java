/*
 * <copyright>
 *
 * Copyright (c) 1996,97
 * Institute for Information Processing and Computer Supported New Media (IICM),
 * Graz University of Technology, Austria.
 *
 * This file is part of the `pw' VRML 2.0 parser.
 *
 * </copyright>
 */
/*
 * Decompression.java
 * stream decompression via external program call
 *
 * created: mpichler, 19970414
 *
 * changed: mpichler, 19970414
 *
 * $Id: Decompression.java,v 1.4 1997/08/01 12:46:24 mpichler Exp $
 */


package iicm.vrml.pw;

import java.io.*;

// Decompression

abstract public class Decompression
{
  // no need ever to create an instance of this class
  private Decompression ()  { }

  /** magic number of compressed files */
  public final static int COMPRESSION_MAGIC = 0x1f;


  /**
   * check whether the InputStream contains compressed data. In this
   * case execute gunzip and return a stream of decompressed data.
   * If executing gunzip fails (e.g. not allowed for applets) or the
   * input data were not compressed, the original InputStream is
   * returned unchanged. Only works for standalone applications.
   */

  public static InputStream filterfile (String filename/*InputStream in*/) throws IOException
  {
    InputStream in = new BufferedInputStream (new FileInputStream (filename));
    return(filter(in));

  }

  public static InputStream filter (InputStream in, String tmp) throws IOException 
  {
    DataInputStream in1 = new DataInputStream(in);

    in1.mark (64);
    int firstchar = in1.read();
    //System.out.print(firstchar+" "+COMPRESSION_MAGIC);
    in1.reset ();  // all that stuff to simulate ungetc
    

    if (firstchar != COMPRESSION_MAGIC)  // check for compression
    {
      //System.out.println ("ordinary file");
      return in;
    }
    // might also check for compression method
    // gunzip supports 0x8b and 0x9d (gzip and compress)
    System.out.println ("compressed file");
    File tmpfile = new File(tmp);
    //System.out.println("tmo="+tmp);
    
    try {
      DataOutputStream out1 = new DataOutputStream(new FileOutputStream(tmpfile));
      int c;
      while ((c = in1.read()) != -1) {
	out1.write(c);
      }
    }
    catch (IOException e) {
      System.out.print("[Decompression] [Error] write tmp error="+e+"\n");
    }


    Runtime runtime = Runtime.getRuntime ();

    String command = "/local/gnu/bin/gunzip -c " + tmp;

    try
    {
      Process gunzip = runtime.exec (command);

      InputStream filtered = gunzip.getInputStream ();
      // seems like it is not possible to redirect both process input and output
      // thus we need a file as input
	
      in.close ();  // gunzip reads from file itself
      //System.out.println ("+ " + cmdToString (command));
      return filtered;
    }
    catch (Exception e)
    {
      System.err.println ("[Decompression] [Error] could not execute command: " + cmdToString (command));
      System.err.println ("got exception: " + e);  // SecurityException or WriteError
      // e.printStackTrace ();
    }

    return in;

  } // filter

  public static InputStream filter (InputStream in) throws IOException
  {
    //if (filename.indexOf ('\'') >= 0)  // Unix: don't issue arbitrary sh commands
    //return new FileInputStream (filename);

    //InputStream in = new BufferedInputStream (new FileInputStream (filename));

    DataInputStream in1 = new DataInputStream(in);

    in1.mark (64);
    int firstchar = in1.read();
    //System.out.print(firstchar+" "+COMPRESSION_MAGIC);
    in1.reset ();  // all that stuff to simulate ungetc
    

    if (firstchar != COMPRESSION_MAGIC)  // check for compression
    {
      //System.out.println ("ordinary file");
      return in;
    }
    // might also check for compression method
    // gunzip supports 0x8b and 0x9d (gzip and compress)
    System.out.println ("compressed file");
    File tmp = new File("/home/wang/html/cgi/tools/tmp");
    
    try {
      DataOutputStream out1 = new DataOutputStream(new FileOutputStream(tmp));
      int c;
      while ((c = in1.read()) != -1) {
	out1.write(c);
      }
    }
    catch (IOException e) {
      System.out.print("[Decompression] [Error] write tmp error="+e);
    }


    Runtime runtime = Runtime.getRuntime ();

    // with the appropriate gunzip path this should work on other platforms too
    // String command = "/usr/local/bin/gunzip -c " + filename;  // only works with full path name
    //String[] command = { "/bin/sh", "-c", "gunzip -c '" + filename + "'" };  // Unix specific
    // String command = "/bin/echo hello world#VRML V2.0 utf8\nCube {}";
    //String command = "/bin/echo #VRML V2.0 utf8\nCube {}";  // not legal VRML 2.0
    //String command = "/bin/cat";
    String command = "/local/gnu/bin/gunzip -c /home/wang/html/cgi/tools/tmp";



    try
    {
      Process gunzip = runtime.exec (command);

      InputStream filtered = gunzip.getInputStream ();
      // seems like it is not possible to redirect both process input and output
      // thus we need a file as input
	
      in.close ();  // gunzip reads from file itself
      //System.out.println ("+ " + cmdToString (command));
      return filtered;
    }
    catch (Exception e)
    {
      System.err.println ("[Decompression] [Eerror] could not execute command: " + cmdToString (command));
      System.err.println ("got exception: " + e);  // SecurityException or WriteError
      // e.printStackTrace ();
    }

    return in;

  } // filter


  // tiny helper to print command line

  public static final String cmdToString (String[] arr)
  {
    StringBuffer buf = new StringBuffer ();
    int n = arr.length;
    for (int i = 0;  i < n;  i++)
    {
      buf.append (arr [i]);
      if (i < n-1)
        buf.append (" ");
    }
    return buf.toString ();
  }

  public static final String cmdToString (String s)
  {
    return s;
  }

} // Decompression




