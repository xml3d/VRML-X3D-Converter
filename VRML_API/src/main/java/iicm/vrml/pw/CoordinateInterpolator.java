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
 * CoordinateInterpolator.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960824
 *
 * changed: krosch, 19960829
 * changed: apesen, 19970605
 * changed: mpichler, 19970929
 *
 * $Id: CoordinateInterpolator.java,v 1.5 1997/09/29 15:25:34 mpichler Exp $
 */


// CoordinateInterpolator
 
package iicm.vrml.pw; 

public class CoordinateInterpolator extends Interpolator
{
  public MFFloat key;
  public MFVec3f keyValue;
  public SFFloat set_fraction;
  public MFVec3f value_changed;
  
  public String nodeName ()
  {
    return NodeNames.NODE_COORDINATEINTERPOLATOR;
  }
  
  public void traverse (Traverser t)
  {
    t.tCoordinateInterpolator (this);
  }

  CoordinateInterpolator ()
  {
    addField ("set_fraction", set_fraction = new SFFloat (0.0f), Field.F_EVENTIN);  // eventIn SFFloat set_fraction
    addField ("key", key = new MFFloat (), Field.F_EXPOSEDFIELD);
    addField ("keyValue", keyValue = new MFVec3f (), Field.F_EXPOSEDFIELD);
    addField ("value_changed", value_changed = new MFVec3f (), Field.F_EVENTOUT);  // eventOut MFVec3f value_changed
    set_fraction.setEventCallback (this);  // see Interpolator.gotEventCB
  }

  Field interpolate ()
  {
    if (value_changed.receiver_ == null)
      return null;
    int numkeys = key.getValueCount ();
    if (numkeys == 0)
      return null;
    int len = keyValue.getValueCount () * 3 / numkeys;  // no. of float output values
    if (len == 0)
      return null;

    value_changed.setSize (len);  // #floats

    interpolateFloatvalues (set_fraction.getValue (), key.getValueData (), key.getValueCount (),
       keyValue.getValueData (), keyValue.getValueCount (), value_changed.getValueData (), len);
    return value_changed;
  }
} // CoordinateInterpolator
