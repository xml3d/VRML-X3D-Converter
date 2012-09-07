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
 * DecompressionStream.java
 * FilterInputStream variant of Decompression class.
 * currently not used
 *
 * created: mpichler, 19970414
 *
 * changed: mpichler, 19970604
 *
 * $Id: DecompressionStream.java,v 1.4 1997/06/04 12:09:10 mpichler Exp $
 */


package iicm.vrml.pw;

import java.io.*;


// DecompressionStream

public class DecompressionStream extends FilterInputStream
{
  /** magic number of compressed files */
  public final static int COMPRESSION_MAGIC = 0x1f;

  private Process gunzip_ = null;


  /**
   * check whether the InputStream contains compressed data. In this
   * case execute gunzip and return a stream of decompressed data.
   * If executing gunzip fails (e.g. not allowed for applets) or the
   * input data were not compressed, the original InputStream is
   * returned unchanged. Only works for standalone applications.
   */

  public DecompressionStream (String filename/*InputStream in*/) throws IOException
  {
    super (null);  // in set below

    if (filename.indexOf ('\'') >= 0)  // Unix: don't issue arbitrary sh commands
    {
      in = new FileInputStream (filename);
      return;
    }

    InputStream orig = new BufferedInputStream (new FileInputStream (filename));
    orig.mark (64);
    int firstchar = orig.read ();
    orig.reset ();  // all that stuff to simulate ungetc

    if (firstchar != COMPRESSION_MAGIC)  // check for compression
    {
      // System.out.println ("ordinary file");
      in = orig;
      return;
    }
    // might also check for compression method
    // gunzip supports 0x8b and 0x9d (gzip and compress)
    // System.out.println ("compressed file");

    Runtime runtime = Runtime.getRuntime ();

    // with the appropriate gunzip path this should work on other platforms too
    // String command = "/usr/local/bin/gunzip -c " + filename;  // only works with full path name
    String[] command = { "/bin/sh", "-c", "gunzip -c '" + filename + "'" };  // Unix specific
    // String command = "/bin/echo hello world#VRML V2.0 utf8\nCube {}";
    // String command = "/bin/echo #VRML V2.0 utf8\nCube {}";  // not legal VRML 2.0
    // String command = "/bin/cat";

    try
    {
      gunzip_ = runtime.exec (command);

      InputStream filtered = gunzip_.getInputStream ();
      // seems like it is not possible to redirect both process input and output
      // thus we need a file as input

      orig.close ();  // gunzip reads from file itself
      System.out.println ("+ " + cmdToString (command));
      in = filtered;
      return;
    }
    catch (Exception e)
    {
      System.err.println ("[Decompression] [Error] could not execute command: " + cmdToString (command));
      System.err.println ("got exception: " + e);  // SecurityException or WriteError
      // e.printStackTrace ();
    }

    in = orig;  // if gunzip call fails

  } // DecompressionStream


  /**
   * on close also destroy subprocess in case it is still running
   */

  public void close () throws IOException
  {
    super.close ();  // in.close ()
    if (gunzip_ != null)
    {
      // System.err.println ("destroying gunzip process");
      gunzip_.destroy ();
      gunzip_ = null;
    }
  } // close

  // tiny helper to print command line

  static final String cmdToString (String[] arr)
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

  static final String cmdToString (String s)
  {
    return s;
  }

} // Decompression
