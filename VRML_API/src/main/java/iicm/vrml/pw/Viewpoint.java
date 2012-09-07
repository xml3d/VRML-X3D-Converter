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
 * Viewpoint.java
 * Copyright (c) 1997 IICM
 *
 * created: mpichler, 19961002
 *
 * changed: mpichler, 19961002
 * changed: apesen, 19970411
 *
 * $Id: Viewpoint.java,v 1.3 1997/05/22 15:29:44 apesen Exp $
 */


package iicm.vrml.pw;


// Viewpoint

public class Viewpoint extends Bindable
{
  public SFFloat fieldOfView;
  public SFBool jump;
  public SFRotation orientation;
  public SFVec3f position;
  public SFString description;

  public String nodeName ()
  {
    return NodeNames.NODE_VIEWPOINT;
  }

  public void traverse (Traverser t)
  {
    t.tViewpoint (this);
  }

  Viewpoint ()
  {
    // Bindable events: set_bind, bindTime, isBound
    addField ("set_bind", new SFBool (false), Field.F_EVENTIN);  // eventIn SFBool set_bind
    addField ("fieldOfView", fieldOfView = new SFFloat (0.785398f), Field.F_EXPOSEDFIELD);  // pi/4, 45 deg.
    addField ("jump", jump = new SFBool (true), Field.F_EXPOSEDFIELD);
    addField ("orientation", orientation = new SFRotation (0.0f, 0.0f, 1.0f, 0.0f), Field.F_EXPOSEDFIELD);
    addField ("position", position = new SFVec3f (0.0f, 0.0f, 10.0f), Field.F_EXPOSEDFIELD);
    addField ("description", description = new SFString (""), Field.F_FIELD);
    addField ("bindTime", new SFTime (0.0), Field.F_EVENTOUT);  // eventOut SFTime bindTime
    addField ("isBound", new SFBool (false), Field.F_EVENTOUT);  // eventOut SFBool isBound
  }

} // Viewpoint
