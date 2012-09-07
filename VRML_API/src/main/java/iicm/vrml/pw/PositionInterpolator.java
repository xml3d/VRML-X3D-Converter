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
 * PositionInterpolator.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960824
 *
 * changed: krosch, 19960829
 * changed: apesen, 19970605
 *
 * $Id: PositionInterpolator.java,v 1.4 1997/08/04 10:13:44 apesen Exp $
 */


// PositionInterpolator
 
package iicm.vrml.pw; 

public class PositionInterpolator extends Interpolator
{
  public MFFloat key;
  public MFVec3f keyValue;
  public SFFloat set_fraction;
  public SFVec3f value_changed;

  public String nodeName ()
  {
    return NodeNames.NODE_POSITIONINTERPOLATOR;
  }

  public void traverse (Traverser t)
  {
    t.tPositionInterpolator (this);
  }

  PositionInterpolator ()
  {
    addField ("set_fraction", set_fraction = new SFFloat (0.0f), Field.F_EVENTIN);  
    addField ("key", key = new MFFloat (), Field.F_EXPOSEDFIELD);
    addField ("keyValue", keyValue = new MFVec3f (), Field.F_EXPOSEDFIELD);
    addField ("value_changed", value_changed = new SFVec3f (0.0f, 0.0f, 0.0f), Field.F_EVENTOUT);  
    set_fraction.setEventCallback (this);  // see Interpolator.gotEventCB
  }

  Field interpolate ()
  {
    if (value_changed.receiver_ == null)
      return null;
    interpolateFloatvalues (set_fraction.getValue (), key.getValueData (), key.getValueCount (),
       keyValue.getValueData (), keyValue.getValueCount (), value_changed.getValue (), 3);
    return value_changed;
  }

} // PositionInterpolator
