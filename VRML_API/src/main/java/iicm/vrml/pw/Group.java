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
 * Group.java
 * Copyright (c) 1997 IICM
 *
 * created: mpichler, 19960808
 *
 * changed: mpichler, 19960808
 * changed: apesen, 19970410
 * changed: kwagen, 19970917
 *
 * $Id: Group.java,v 1.5 1997/09/17 09:52:56 kwagen Exp $
 */


package iicm.vrml.pw;


// Group

public class Group extends GroupNode
{
  // MFNode children in GroupNode
  public SFVec3f bboxCenter, bboxSize;

  public String nodeName ()
  {
    return NodeNames.NODE_GROUP;
  }

  public void traverse (Traverser t)
  {
    t.tGroup (this);
  }

  Group ()
  {
    addField ("addChildren", addChildren = new MFNode (), Field.F_EVENTIN);     // eventIn addChildren
    addField ("removeChildren", removeChildren = new MFNode (), Field.F_EVENTIN);  // eventIn removeChildren
    addField ("children", children, Field.F_EXPOSEDFIELD);        // of GroupNode
    addField ("bboxCenter", bboxCenter = new SFVec3f (0.0f, 0.0f, 0.0f), Field.F_FIELD);
    addField ("bboxSize", bboxSize = new SFVec3f (-1.0f, -1.0f, -1.0f), Field.F_FIELD);
    addChildren.setEventCallback (this);
    removeChildren.setEventCallback (this);
  }
} // Group
