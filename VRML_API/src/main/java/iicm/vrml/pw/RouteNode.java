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
 * RouteNode.java
 * Copyright (c) 1996,97 IICM
 *
 * created: mpichler, 19960925
 * changed: mpichler, 19970131
 *
 * $Id: RouteNode.java,v 1.4 1997/05/22 15:36:14 apesen Exp $
 */


package iicm.vrml.pw; 

import java.io.*;
import java.util.*;


// RouteNode

public class RouteNode extends Node
{
  public Node fromNode, toNode;
  public String fromEvent, toEvent;

  public String nodeName ()
  {
    return Node.ROUTE_KEYWORD;
  }

  public void traverse (Traverser t)
  {
    t.tRoute (this);
  }

  RouteNode (Node fnode, Node tnode, String fevt, String tevt)
  {
    // ass.: all args non-null
    fromNode = fnode;
    toNode = tnode;
    // RouteNode does not need to know the eventin/eventout fields themselves
    // route has been established by caller (addReceiver)
    // event names
    fromEvent = fevt;
    toEvent = tevt;
  }

  /**
   * the RouteNode represents a ROUTE statement;
   * it currently only exists to be output again, voilla:
   */

  public void writeNode (PrintStream os, Hashtable writtenrefs)
  {
    // a ROUTE cannot be given a name via DEF
    os.print (Node.ROUTE_KEYWORD + " " + fromNode.objname + "." + fromEvent +
      " TO " + toNode.objname + "." + toEvent+"\n");
  }

  public void writeX3dNode (PrintStream os, Hashtable writtenrefs) 
  {
    os.print ("<"+Node.ROUTE_KEYWORD+" fromNode=\""+fromNode.objname+"\""+
		" fromField=\""+fromEvent+"\""+" toNode=\""+toNode.objname+"\""+
		" toField=\""+toEvent+"\"/>\n");
  }
} // RouteNode  
