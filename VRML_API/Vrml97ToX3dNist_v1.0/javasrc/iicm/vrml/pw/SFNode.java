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
 * SFNode.java
 *
 * created: mpichler, 19960808
 * changed: mpichler, 19970131
 * changed: apesen, 19970526
 *
 * $Id: SFNode.java,v 1.6 1997/05/28 17:06:50 apesen Exp $
 */


package iicm.vrml.pw;

import java.io.*;
import java.util.Hashtable;


/**
 * SFNode - Field that holds one node (or null)
 * Copyright (c) 1996,97 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.5, latest change: 31 Jan 97
 */


public class SFNode extends Field
{
  //private Node node;  // = null
  public Node node;

  public String fieldName ()
  {
    return FieldNames.FIELD_SFNode;
  }

  Field newFieldInstance ()
  {
    SFNode newinst = new SFNode ();
    newinst.node = node;  // seems fine, TODO: check it
    return newinst;
  }

  final public Node getNode ()
  {
    return node;
  }

  final public void setValue (Node val)
  {
    node = val;  // TODO: check this
  }

  void copyValue (Field source)
  {
    node = ((SFNode) source).node;  // TODO: check this
  }

  void readValue (VRMLparser parser) throws IOException
  {
    StrTokenizer st = parser.istok;

    String token = st.readIdentifier ();
    if (token != null && token.equals (STR_NULL))
      node = null;  // may also consider handling NULL directly in Node.readNode
    else
    {
      st.putbackString (token);
      // this is one of the 2 places where the parser must be able to put back a string
      node = Node.readNode (parser);
    }
    changed = true;
  }

  void writeValue (PrintStream os, Hashtable writtenrefs)
  {
    if (node == null)
      os.print (STR_NULL+"\n");
    else
      node.writeNode (os, writtenrefs);
  }

  void writeX3dValue (PrintStream os, Hashtable writtenrefs)
  {
    //if (node == null)
      //os.print (STR_NULL+"\n");
    //else
    if (node != null)
      node.writeX3dNode (os, writtenrefs,1);    
  }

  void writeX3dValue (PrintStream os, Hashtable writtenrefs, int depth)
  {
    //if (node == null)
      //os.print (STR_NULL+"\n");
    //else
    if (node != null) {
      /*System.out.println("node.objname="+node.objname);
      if (node.objname!=null) {
	if ((writtenrefs.get(node.objname)) != node)
	  {
	    os.print(" DEF=\""+node.objname+"\"");
	    writtenrefs.put(node.objname, node);
	  }
	else 
	  {
	    os.print(" USE=\""+node.objname+"\"/>\n");
	    return;
	  }
      }*/
      node.writeX3dNode (os, writtenrefs, depth);    
    }
  }

} // SFNode
