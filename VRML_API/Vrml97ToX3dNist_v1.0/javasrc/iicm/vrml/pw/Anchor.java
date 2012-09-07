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
 * Anchor.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960827
 *
 * changed: mpichler, 19960917
 * changed: apesen, 19970409
 * changed: kwagen, 19970917
 * 
 * $Id: Anchor.java,v 1.6 1997/09/17 09:49:58 kwagen Exp $
 */

 
package iicm.vrml.pw; 

import iicm.utils3d.Hitpoint;
// import iicm.utils3d.Vec3f;

// Anchor

public class Anchor extends GroupNode implements PointerSensor
{
  // MFNode children in GroupNode
  public SFString description;
  public MFString parameter, url;
  public SFVec3f bboxCenter, bboxSize;

  public String nodeName ()
  {
    return NodeNames.NODE_ANCHOR;
  }

  public void traverse (Traverser t)
  {
    t.tAnchor (this);
  }

  Anchor ()
  {
    addField ("addChildren", addChildren = new MFNode (), Field.F_EVENTIN);     // eventIn addChildren
    addField ("removeChildren", removeChildren = new MFNode (), Field.F_EVENTIN);  // eventIn removeChildren
    addField ("children", children, Field.F_EXPOSEDFIELD);        // of GroupNode
    addField ("description", description = new SFString (""), Field.F_EXPOSEDFIELD);
    addField ("parameter", parameter = new MFString (), Field.F_EXPOSEDFIELD);
    addField ("url", url = new MFString (), Field.F_EXPOSEDFIELD);
    addField ("bboxCenter", bboxCenter = new SFVec3f (0.0f, 0.0f, 0.0f), Field.F_FIELD);
    addField ("bboxSize", bboxSize = new SFVec3f (-1.0f, -1.0f, -1.0f), Field.F_FIELD);
    addChildren.setEventCallback (this);
    removeChildren.setEventCallback (this);
   }

  /**
   * Anchor does not react on pointer events in pw.
   * It only implements PointerSensor to allow uniform treatment
   * of Anchors like pointer sensors by an application.
   */

  public void mouseMove (float downx, float downy, Hitpoint hit, double timestamp)  { }
  public void mouseDrag (float downx, float downy, Hitpoint hit, double timestamp)  { }
  public void mouseDown (float downx, float downy, Hitpoint hit, double timestamp)  { }
  public void mouseUp (double timestamp)  { }
  public void mouseExit (double timestamp)  { }
}  
