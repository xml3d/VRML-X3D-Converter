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
 * ProximitySensor.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960824
 *
 * changed: krosch, 19960829
 * changed: apesen, 19970410
 *
 * $Id: ProximitySensor.java,v 1.3 1997/05/22 11:51:36 apesen Exp $
 */


// ProximitySensor
 
package iicm.vrml.pw; 

public class ProximitySensor extends Sensor
{
  public SFVec3f center, size;
  public SFBool enabled;
  
  public String nodeName ()
  {
    return NodeNames.NODE_PROXIMITYSENSOR;
  }
  
  public void traverse (Traverser t)
  {
    t.tProximitySensor (this);
  }

  ProximitySensor ()
  {
    addField ("center", center = new SFVec3f (0.0f, 0.0f, 0.0f), Field.F_EXPOSEDFIELD);
    addField ("size", size = new SFVec3f (0.0f, 0.0f, 0.0f), Field.F_EXPOSEDFIELD);
    addField ("enabled", enabled = new SFBool (true), Field.F_EXPOSEDFIELD);
    addField ("isActive", new SFBool (false), Field.F_EVENTOUT);  // eventOut SFBool isActive
    addField ("position_changed", new SFVec3f (0.0f, 0.0f, 0.0f), Field.F_EVENTOUT);  // eventOut position_changed
    addField ("orientation_changed", new SFRotation (0.0f, 0.0f, 0.0f, 0.0f), Field.F_EVENTOUT);  // eventOut
    addField ("enterTime", new SFTime (0.0), Field.F_EVENTOUT);  // eventOut SFTime enterTime
    addField ("exitTime", new SFTime (0.0), Field.F_EVENTOUT);  // eventOut SFTime exitTime 
  }
} // ProximitySensor
