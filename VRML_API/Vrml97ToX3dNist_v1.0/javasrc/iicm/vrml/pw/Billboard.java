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
 * Billboard.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960828
 *
 * changed: mpichler, 19960917
 * changed: apesen, 19970409
 * changed: kwagen, 19970917
 *
 * $Id: Billboard.java,v 1.5 1997/09/17 09:51:20 kwagen Exp $
 */

 
package iicm.vrml.pw; 


// Billboard

public class Billboard extends GroupNode
{
  // MFNode children in GroupNode
  public SFVec3f axisOfRotation, bboxCenter, bboxSize;

  public String nodeName ()
  {
    return NodeNames.NODE_BILLBOARD;
  }

  public void traverse (Traverser t)
  {
    t.tBillboard (this);
  }

  Billboard ()
  {
    addField ("addChildren", addChildren = new MFNode (), Field.F_EVENTIN);     // eventIn addChildren
    addField ("removeChildren", removeChildren = new MFNode (), Field.F_EVENTIN);  // eventIn removeChildren
    addField ("axisOfRotation", axisOfRotation = new SFVec3f (0.0f, 1.0f, 0.0f), Field.F_EXPOSEDFIELD);
    addField ("children", children, Field.F_EXPOSEDFIELD);        // of GroupNode
    addField ("bboxCenter", bboxCenter = new SFVec3f (0.0f, 0.0f, 0.0f), Field.F_FIELD);
    addField ("bboxSize", bboxSize = new SFVec3f (-1.0f, -1.0f, -1.0f), Field.F_FIELD);
    addChildren.setEventCallback (this);
    removeChildren.setEventCallback (this);
  }
} // Billboard
