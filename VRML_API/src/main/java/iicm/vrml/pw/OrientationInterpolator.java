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
 * OrientationInterpolator.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960824
 *
 * changed: krosch, 19960829
 * changed: apesen, 19970605
 *
 * $Id: OrientationInterpolator.java,v 1.5 1997/08/06 13:14:33 apesen Exp $
 */


// OrientationInterpolator
 
package iicm.vrml.pw; 

import iicm.utils3d.Quaternion;

public class OrientationInterpolator extends Interpolator
{
  public MFFloat key;
  public MFRotation keyValue;
  public SFFloat set_fraction;
  public SFRotation value_changed;
  
  private float[] values = new float[4];
  
  public String nodeName ()
  {
    return NodeNames.NODE_ORIENTATIONINTERPOLATOR;
  }
  
  public void traverse (Traverser t)
  {
    t.tOrientationInterpolator (this);
  }

  OrientationInterpolator ()
  {
    addField ("set_fraction", set_fraction = new SFFloat (0.0f), Field.F_EVENTIN);
    addField ("key", key = new MFFloat (), Field.F_EXPOSEDFIELD);
    addField ("keyValue", keyValue = new MFRotation (), Field.F_EXPOSEDFIELD);
    addField ("value_changed", value_changed = new SFRotation (0.0f, 0.0f, 0.0f, 0.0f), Field.F_EVENTOUT);
    set_fraction.setEventCallback (this);  // see Interpolator.gotEventCB
  }

  /**
   * linear interpolation of valsperkey values. fraction frac lies between key1 and key2.
   * out: valsperkey interpolated values starting from index offs * valsperkey.
   */
  void linearInterpolation (float frac, float key1, float key2, float[] val, int offs, 
    float[] value, int valsperkey)

  {
    float intfrac = (frac - key1)/(key2 - key1);

    Quaternion quat1 = new Quaternion (offs * valsperkey, val);
    Quaternion quat2 = new Quaternion ((offs + 1) * valsperkey, val);
    Quaternion erg = Quaternion.slerp (quat1, quat2, intfrac);
    System.arraycopy(erg.getAxisAngle (),0,value,0,4);
  }  

  Field interpolate ()
  {
    if (value_changed.receiver_ == null)
      return null;
    interpolateFloatvalues (set_fraction.getValue (), key.getValueData (), key.getValueCount (),
       keyValue.getValueData (), keyValue.getValueCount (), values, 4);
    value_changed.setValue (values[0], values[1], values[2], values[3]);
    return value_changed;
  }
} // OrientationInterpolator
