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
 * NormalInterpolator.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960824
 *
 * changed: krosch, 19960829
 * changed: apesen, 19970605
 * changed: mpichler, 19970929
 *
 * $Id: NormalInterpolator.java,v 1.6 1997/09/29 15:21:48 mpichler Exp $
 */


// NormalInterpolator
 
package iicm.vrml.pw; 

import iicm.utils3d.Vec3f;
public class NormalInterpolator extends Interpolator
{
  public MFFloat key;
  public MFVec3f keyValue;
  public SFFloat set_fraction;
  public MFVec3f value_changed;
  
  public String nodeName ()
  {
    return NodeNames.NODE_NORMALINTERPOLATOR;
  }
  
  public void traverse (Traverser t)
  {
    t.tNormalInterpolator (this);
  }

  NormalInterpolator ()
  {
    addField ("set_fraction", set_fraction = new SFFloat (0.0f), Field.F_EVENTIN);
    addField ("key", key = new MFFloat (), Field.F_EXPOSEDFIELD);
    addField ("keyValue", keyValue = new MFVec3f (), Field.F_EXPOSEDFIELD);
    addField ("value_changed", value_changed = new MFVec3f (), Field.F_EVENTOUT);
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
    int numnormals = valsperkey / 3;
    int val1offs = offs * valsperkey;
    int val2offs = (offs + 1) * valsperkey;
    for (int i = 0;  i < numnormals;  i++)
      Vec3f.slerpNorm (val, val1offs + i*3, val, val2offs + i*3, intfrac, value, i*3);
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
} // NormalInterpolator
