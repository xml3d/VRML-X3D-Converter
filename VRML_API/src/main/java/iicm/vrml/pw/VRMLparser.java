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
 * VRMLparser.java
 * VRML parser
 *
 * created: mpichler, 19960730
 *
 * changed: krosch, 19960905
 * changed: mpichler, 19960931
 *
 * $Id: VRMLparser.java,v 1.11 1997/09/15 12:06:29 kwagen Exp $
 */


package iicm.vrml.pw;


import java.io.*;
import java.util.*;


// HeaderInfo - helper struct

class HeaderInfo
{
  HeaderInfo (String h, float v)
  { header = h; version = v; }

  String header;
  float version;
} // HeaderInfo



/**
 * VRMLparser - VRML parser class
 * Copyright (c) 1996,97 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.5, changed: 31 Jan 97
 */


public class VRMLparser
{
  // constants

  /** recognized header lines */
  final static HeaderInfo[] headers =
  {
    new HeaderInfo ("#VRML V1.0 ascii", 1.0f),  // VRML 1.0 (not supported)
    new HeaderInfo ("#VRML V2.0 utf8", 2.0f),  // VRML 2.0
    new HeaderInfo ("#VRML Draft #1 V2.0 utf8", 2.0001f),  // draft 1
    new HeaderInfo ("#VRML Draft #2 V2.0 utf8", 2.0002f),  // draft 2
    new HeaderInfo ("#VRML Draft #3 V2.0 utf8", 2.0003f),  // draft 3
  };
  private static final int HDR_VRML_1_0 = 0;
  private static final int HDR_VRML_2_0 = 1;
  private static int HDR_MAXLEN = 64;

  // variables

  /** input stream tokenizer */
  StrTokenizer istok;

  /** callbacks for parser messages */
  ParserOutput pout;

  /** current PROTO node for reading IS fields */
  ProtoNode curproto = null;


  // private variables

  private float version;     // version no.
  // node names (DEF/USE); file (stream) local
  private Hashtable nodeNames = new Hashtable ();
  // PROTO node names
  private Hashtable protoNodes = new Hashtable ();


  // methods

  /**
   * create VRMLparser for specific input stream to read from.
   * (underlying parser buffers input stream itself.)
   * Note: will not return before 1st byte of InputStream is read (blocks)
   */

  public VRMLparser (InputStream input)
  {
    this (input, new DefParserOutput ());
  }

  /**
   * VRMLparser constructor with message callbacks
   */

  public VRMLparser (InputStream input, ParserOutput po)
  {
    istok = new StrTokenizer (input);
    pout = po;
  }

  /**
   * set VRML version for headerless VRML stream
   */

  public void setVersion (float ver)
  {
    version = ver;
  }

  /**
   * get VRML version; only valid after checkHeader or setVersion
   */

  public float getVersion ()
  {
    return version;
  }

  /**
   * this parser is currently designed for VRML 2.0 only; this method
   * could be used to distinguish Moving World Scenes from VRML 1.0
   * @return flag, whether data is in Moving Worlds (VRML 2.0) format
   */

  public boolean isMovingWorlds ()
  {
    return (version >= 2.0);
  }

  /** add node reference (DEF) */

  void addReference (String refname, Node node)
  {
    nodeNames.put (refname, node);
  }

  /** get node reference (USE) */

  Node getReference (String name)
  {
    return ((Node) nodeNames.get (name));
  }

  /** add a PROTO node name */

  void addProtoNode (String pname, ProtoNode pnode)
  {
    protoNodes.put (pname, pnode);
  }

  /** get a PROTO node name */

  ProtoNode getProtoNode (String name)
  {
    return ((ProtoNode) protoNodes.get (name));
  }


  /**
   * parse complete VRML stream (header + body)
   * @return root node
   * @see readHeader, readBody
   */

  public GroupNode readStream ()
  {
    if (readHeader () > 0)
      return readBody ();
    return null;
  }

  /**
   * check header of VRML data stream
   * @return VRML version identifier or 0 on failure
   */

  public float readHeader ()
  {
    version = 0.0f;
    char[] headerbuf = new char[HDR_MAXLEN];

    try
    {
      int headerlen = istok.readLine (headerbuf);
      String headerline = new String (headerbuf, 0, headerlen);

      //System.out.println ("header line read from file: " + headerline);
      if (pout.debug_)
        pout.debug ("VRML header: " + headerline);
      for (int i = 0;  i < headers.length;  i++)
      {
        if (headerline.startsWith (headers [i].header))
        { // VRML spec: trailing stuff on same line to be ignored
          version = headers [i].version;
          return version;
        }
      }
      pout.error ("[VRMLparser] [Error] invalid header " + headerline);
    }
    catch (IOException e)
    { pout.error ("[VRMLparser] [Error] IOException on reading VRML header");
      return version;
    }

    // parse header and return version number
    return version;
  } // readHeader

  /**
   * read VRML body
   * should not be called before readHeader or setVersion
   */

  public GroupNode readBody ()
  {
    if (version == 0)  // bad header or version not set
      return null;

    //pout.verbose ("valid header. version: " + version);

    // istok now dedicated VRML stream tokenizer

    // read all root level children
    GroupNode root = new GroupNode ();
    try {
      root.readNodes (this);
    }
    catch (Exception e)
      {pout.error ("[VRMLParser] [Error] Exception " + e);
      }

    return root;

    // return Node.readNode (this);

  } // readBody


  /**
   * write VRML header line
   */

  public void writeHeader (PrintStream os)
  {
    // might keep original file header on reading
    os.println (headers [isMovingWorlds () ? HDR_VRML_2_0 : HDR_VRML_1_0].header);
  }

  public Hashtable getNodeNames ()
  {
    return nodeNames;
  }

} // class VRMLparser




