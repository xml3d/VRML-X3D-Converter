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
 * Transform.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960828
 *
 * changed: mpichler, 19970114
 * changed: apesen, 19970506
 * changed: kwagen, 19970917
 *
 * $Id: Transform.java,v 1.5 1997/09/17 09:54:12 kwagen Exp $
 */

 
package iicm.vrml.pw; 


// Transform

public class Transform extends GroupNode
{
  // MFNode children in GroupNode
  public SFVec3f center, scale, translation, bboxCenter, bboxSize;
  public SFRotation rotation, scaleOrientation;

  public String nodeName ()
  {
    return NodeNames.NODE_TRANSFORM;
  }

  public void traverse (Traverser t)
  {
    t.tTransform (this);
  }

  Transform ()
  {
    addField ("addChildren", addChildren = new MFNode (), Field.F_EVENTIN);     // eventIn addChildren
    addField ("removeChildren", removeChildren = new MFNode (), Field.F_EVENTIN);  // eventIn removeChildren
    addField ("center", center = new SFVec3f (0.0f, 0.0f, 0.0f), Field.F_EXPOSEDFIELD);
    addField ("children", children, Field.F_EXPOSEDFIELD);  // of GroupNode
    addField ("rotation", rotation = new SFRotation (0.0f, 0.0f, 1.0f, 0.0f), Field.F_EXPOSEDFIELD);
    addField ("scale", scale = new SFVec3f (1.0f, 1.0f, 1.0f), Field.F_EXPOSEDFIELD);
    addField ("scaleOrientation", scaleOrientation = new SFRotation (0.0f, 0.0f, 1.0f, 0.0f), Field.F_EXPOSEDFIELD);
    addField ("translation", translation = new SFVec3f (0.0f, 0.0f, 0.0f), Field.F_EXPOSEDFIELD);
    addField ("bboxCenter", bboxCenter = new SFVec3f (0.0f, 0.0f, 0.0f), Field.F_FIELD);
    addField ("bboxSize", bboxSize = new SFVec3f (-1.0f, -1.0f, -1.0f), Field.F_FIELD);
    addChildren.setEventCallback (this);
    removeChildren.setEventCallback (this);
  }
} // Transform
