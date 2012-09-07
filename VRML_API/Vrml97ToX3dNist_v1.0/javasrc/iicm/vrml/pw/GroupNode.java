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
 * GroupNode.java
 * base class for group nodes
 *
 * created: mpichler, 19960724
 *
 * changed: krosch, 19960724
 * changed: mpichler, 19960930
 * changed: kwagen, 19970917
 *
 * $Id: GroupNode.java,v 1.7 1997/09/17 12:51:08 mpichler Exp $
 */


package iicm.vrml.pw;

import java.io.*;
import java.util.*;


/**
 * GroupNode - grouping node base class
 * also used to manage root level children
 * Copyright (c) 1996,97 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.5, latest change: 30 Jan 97
 */


public class GroupNode extends Node implements GotEventCallback
{
  public MFNode children = new MFNode ();
  public MFNode addChildren, removeChildren;

  public String nodeName ()
  {
    return "<Group>";
  }

  public void traverse (Traverser t)
  {
    t.tGroupNode (this);
  }


  /**
   * read nodes at root level
   */

  public void readNodes (VRMLparser parser)
  {
    readNodes (parser, false);  // read until EOF
  }

  public void readNodes (VRMLparser parser, boolean stoponbrace)
  {
    StrTokenizer st = parser.istok;
    Vector nodes = children.getNodes ();

    try
    {
      while (st.skipCommentReturn(true) && !st.eof ())
      {
	  //st.skipCommentReturn (true);
	//if (st.OutputComment().length() > 0) os.printlin(st.Outputcomment());
        if (stoponbrace && st.nextChar () == '}')
        { // '}' not read
          break;
        }
        Node node = Node.readNode (parser);
        if (node != null)
          nodes.addElement (node);
      }
    }
    catch (IOException e)
    { parser.pout.error ("[GroupNode] [Error] IOException during parsing" + atCurrLine (st) + ", "+e);
      // read nodes may still be useful, not destroyed
    }
  }


  /**
   * write nodes at root level
   */

  public void writeNodes (PrintStream os)
  {
    Enumeration e = getChildrenEnumerator ();
    Hashtable writtenrefs = new Hashtable ();

    while (e.hasMoreElements ()) {
      ((Node) e.nextElement ()).writeNode (os, writtenrefs);
    }
  }

  /**
   * write X3d nodes at root level
   */

  public void writeX3dNodes (PrintStream os)
  {
    Enumeration e = getChildrenEnumerator ();
    Hashtable writtenrefs = new Hashtable ();

    while (e.hasMoreElements ()) {
      ((Node) e.nextElement ()).writeX3dNode (os, writtenrefs, 1);
    }
  }

  public void writeX3dNodes (PrintStream os, int depth)
  {
    Enumeration e = getChildrenEnumerator ();
    Hashtable writtenrefs = new Hashtable ();

    while (e.hasMoreElements ()) {
      ((Node) e.nextElement ()).writeX3dNode (os, writtenrefs, depth);
    }
  }

  public void writeX3dNodes (VRMLparser parser, PrintStream os) {
    Enumeration e = getChildrenEnumerator ();
    Hashtable writtenrefs = new Hashtable ();

    while (e.hasMoreElements ()) {
      ((Node) e.nextElement ()).writeX3dNode (parser, os, writtenrefs, 1);
    }
  }
  public void writeX3dNodes (VRMLparser parser, PrintStream os, int depth) {
    Enumeration e = getChildrenEnumerator ();
    Hashtable writtenrefs = new Hashtable ();

    while (e.hasMoreElements ()) {
      ((Node) e.nextElement ()).writeX3dNode (parser, os, writtenrefs, depth);
    }
  }
  public void writeX3dNodes (VRMLparser parser, PrintStream os, int depth,int proto) {
    Enumeration e = getChildrenEnumerator ();
    Hashtable writtenrefs = new Hashtable ();

    while (e.hasMoreElements ()) {
      ((Node) e.nextElement ()).writeX3dNode (parser, os, writtenrefs,1,proto);
    }
  }


  /**
   * get an Enumeration to process all children
   */

  public Enumeration getChildrenEnumerator ()
  {
    Vector nodes = children.getNodes ();
    return nodes.elements ();
    // number: nodes.size ();
  }


  /**
   * add/remove children nodes on incoming addChildren/removeChildren events
   */

  public void gotEventCB (Field field, double timestamp)
  {
    if (field == addChildren)
    {
      children.addNodes (addChildren.getNodes ());
      children.sendEvent (timestamp);
    }
    else if (field == removeChildren)
    {
      children.removeNodes (removeChildren.getNodes ());
      children.sendEvent (timestamp);
    }
  }
} // GroupNode
