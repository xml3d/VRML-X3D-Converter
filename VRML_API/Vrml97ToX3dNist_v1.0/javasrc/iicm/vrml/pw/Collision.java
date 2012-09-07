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
 * Collision.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960828
 *
 * changed: mpichler, 19960917
 * changed: apesen, 19970409
 *
 * $Id: Collision.java,v 1.4 1997/06/17 13:54:17 mpichler Exp $
 */

 
package iicm.vrml.pw; 


// Collision

public class Collision extends GroupNode
{
  // MFNode children in GroupNode
  public SFBool collide;
  public SFVec3f bboxCenter, bboxSize;
  public SFNode proxy;

  public String nodeName ()
  {
    return NodeNames.NODE_COLLISION;
  }

  public void traverse (Traverser t)
  {
    t.tCollision (this);
  }

  Collision ()
  {
    addField ("addChildren", new MFNode (), Field.F_EVENTIN);     // eventIn addChildren
    addField ("removeChildren", new MFNode (), Field.F_EVENTIN);  // eventIn removeChildren
    addField ("children", children, Field.F_EXPOSEDFIELD);        // of GroupNode
    addField ("collide", collide = new SFBool (true), Field.F_EXPOSEDFIELD);
    addField ("bboxCenter", bboxCenter = new SFVec3f (0.0f, 0.0f, 0.0f), Field.F_FIELD);
    addField ("bboxSize", bboxSize = new SFVec3f (-1.0f, -1.0f, -1.0f), Field.F_FIELD);
    addField ("proxy", proxy = new SFNode (), Field.F_FIELD);
    addField ("collideTime", new SFTime (0.0), Field.F_EVENTOUT);  // eventOut collideTime
  }
} // Collision
