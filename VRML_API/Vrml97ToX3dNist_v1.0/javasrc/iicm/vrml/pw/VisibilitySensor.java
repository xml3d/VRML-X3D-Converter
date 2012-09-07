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
 * VisibilitySensor.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960824
 *
 * changed: krosch, 19960829
 * changed: apesen, 19970410
 *
 * $Id: VisibilitySensor.java,v 1.3 1997/05/22 15:30:46 apesen Exp $
 */


// VisibilitySensor
 
package iicm.vrml.pw; 

public class VisibilitySensor extends Sensor
{
  public SFVec3f center, size;
  public SFBool enabled;
  
  public String nodeName ()
  {
    return NodeNames.NODE_VISIBILITYSENSOR;
  }
  
  public void traverse (Traverser t)
  {
    t.tVisibilitySensor (this);
  }

  VisibilitySensor ()
  {
    addField ("center", center = new SFVec3f (0.0f, 0.0f, 0.0f), Field.F_EXPOSEDFIELD);
    addField ("enabled", enabled = new SFBool (true), Field.F_EXPOSEDFIELD);
    addField ("size", size = new SFVec3f (0.0f, 0.0f, 0.0f), Field.F_EXPOSEDFIELD);
    addField ("enterTime", new SFTime(0.0), Field.F_EVENTOUT);  // eventOut SFTime enterTime
    addField ("exitTime", new SFTime(0.0), Field.F_EVENTOUT);  // eventOut SFTime exitTime 
    addField ("isActive", new SFBool(false), Field.F_EVENTOUT);  // eventOut SFBool isActive
  }
} // VisibilitySensor
