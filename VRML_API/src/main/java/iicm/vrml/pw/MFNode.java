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
 * MFNode.java
 *
 * created: mpichler, 19960809
 *
 * changed: apesen, 19970526
 * changed: mpichler, 19970528
 * changed: kwagen, 19970917
 *
 * $Id: MFNode.java,v 1.6 1997/09/17 10:18:33 kwagen Exp $
 */


package iicm.vrml.pw;

import java.io.*;
import java.util.*;


/**
 * MFNode - Field that holds a vector of nodes
 * Copyright (c) 1996 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.1, latest change:  1 Oct 96
 */


public class MFNode extends MultiField
{
  //private Vector nodes = new Vector ();

  public Vector nodes = new Vector ();
  public String fieldName ()
  {
    return FieldNames.FIELD_MFNode;
  }

  Field newFieldInstance ()
  {
    MFNode newinst = new MFNode ();
    //modified by wang, 4/24/2000. It will overwrite the nodes of previous 
    //proto instances
    //newinst.nodes = nodes;  // seems fine, TODO: check it 
    return newinst;
  }

  final public int getValueCount ()
  {
    return nodes.size ();
  }

  final public Vector getNodes ()
  {
    return nodes;  // size () tells no. of nodes
  }

  final public void setValue (Node[] nds)
  {
    nodes.removeAllElements ();
    nodes.ensureCapacity (nds.length);  // produce less garbage
    for (int i = 0;  i < nds.length;  i++)  // TODO: check this
      nodes.addElement (nds [i]);
  }

  void copyValue (Field source)
  {
    nodes = (Vector) ((MFNode) source).nodes.clone ();  // clone Vector, not Nodes
//     Vector nds = ((MFNode) source).nodes;
//     nodes.removeAllElements ();
//     for (int i = 0;  i < nds.size ();  i++)  // TODO: check this
//       nodes.addElement (nds.elementAt (i));
  }

  void read1Value (VRMLparser parser) throws IOException
  {
    Node node = Node.readNode (parser);
    // is NULL allowed here? (see also SFNode.readValue; take care in writeValue)
    if (node != null)
    {
      nodes.addElement (node);
      changed = true;
    }
    else
      readerror = true;
  }

  boolean clearValues ()
  {
    if (nodes.isEmpty ())
      return false;
    nodes.setSize (0);
    return true;
  }

  void writeValue (PrintStream os, Hashtable writtenrefs)
  {
    int num = nodes.size ();
    Enumeration e = nodes.elements ();

    if (num != 1)
      os.print ("[\n");

    while (e.hasMoreElements ()) 
      ((Node) e.nextElement ()).writeNode (os, writtenrefs);

    if (num != 1)
      os.print ("\t]");
  }

  void writeX3dValue (PrintStream os, Hashtable writtenrefs)
  {
    int num = nodes.size ();
    Enumeration e = nodes.elements ();

    //if (num != 1)
    //os.print ("[\n");

    while (e.hasMoreElements ()) 
      ((Node) e.nextElement ()).writeX3dNode (os, writtenrefs,1);

    //if (num != 1)
    //os.print ("\t]");
  }

  void writeX3dValue (PrintStream os, Hashtable writtenrefs, int depth)
  {
    int num = nodes.size ();
    Enumeration e = nodes.elements ();

    //if (num != 1)
    //os.print ("[\n");

    while (e.hasMoreElements ()) 
      ((Node) e.nextElement ()).writeX3dNode (os, writtenrefs,depth);

    //if (num != 1)
    //os.print ("\t]");
  }

  public void addNodes (Vector nds)
  {
    for (int i = 0; i < nds.size (); i++)
      if (!nodes.contains (nds.elementAt (i)))
        nodes.addElement (nds.elementAt (i));
  }

  public void removeNodes (Vector nds)
  {
    for (int i = 0; i < nds.size (); i++)
      nodes.removeElement (nds.elementAt (i));
  }

} // MFNode
