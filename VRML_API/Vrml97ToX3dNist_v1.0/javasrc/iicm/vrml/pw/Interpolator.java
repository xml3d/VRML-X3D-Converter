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
 * Interpolator.java
 * base class for Interpolator nodes
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960829
 *
 * changed: krosch, 19960829
 * changed: apesen, 19970605
 * changed: mpichler, 19970929
 *
 * $Id: Interpolator.java,v 1.6 1997/09/29 15:20:31 mpichler Exp $
 */


package iicm.vrml.pw;

/**
 * Interpolator base class
 */

abstract public class Interpolator extends Node implements GotEventCallback
{
  private int interNo_ = 0;  // actual interpolation between key[interNo_] and key[interNo_+1]

  private float[] value = new float[1];


  /**
   * linear interpolation of valsperkey values. fraction frac lies between key1 and key2.
   * out: valsperkey interpolated values starting from index offs * valsperkey.
   */

  void linearInterpolation (float frac, float key1, float key2, float[] val, int offs,
    float[] value, int valsperkey)
  {
    float intfrac = (frac - key1)/(key2 - key1);
    int val1offs = offs * valsperkey;
    int val2offs = (offs + 1) * valsperkey;
    for (int i = 0;  i < valsperkey;  i++)
    {
      float val1 = val [val1offs++];
      float val2 = val [val2offs++];
      value[i] = val1 + intfrac * (val2 - val1);
    }
  }

  /**
   * derived class will call interpolateFloatvalues with appropriate arguments.
   * @return field to which event should be sent (values changed) or null,
   * if there are no event receivers.
   */

  abstract Field interpolate ();

  /**
   * calculates the next interpolation-step.
   * searches fraction in keys[] and sets value[] (valsperkey elements)
   * to the according interpolated values out of keyvals (groups of valsperkey elements each).
   * caller must ensure that value[] is large enough may hold valsperkey elements
   */

  void interpolateFloatvalues (float fraction, float[] keys, int lenKeys, float[] keyvals, int lenKVals,
    float[] value, int valsperkey)
  {
    int maxInter = Math.min (lenKeys, lenKVals) - 1;  // index of last key to consider

    if (fraction <= keys[0]) 
    {
      System.arraycopy (keyvals, 0, value, 0, valsperkey);
      return;
    }
    if (fraction >= keys[maxInter])
    {
      System.arraycopy (keyvals, maxInter * valsperkey, value, 0, valsperkey);
      return;
    }

    // interNo_: interval number
    if (interNo_ >= maxInter || fraction < keys[interNo_] || fraction >= keys[interNo_ + 1])
    {
      for (int j = 0;  j < maxInter;  j++)
        if (fraction >= keys[j] && fraction < keys[j + 1])
        {
          interNo_ = j;
          break;
        }
    }

    if (fraction == keys[interNo_])
      System.arraycopy (keyvals, interNo_ * valsperkey, value, 0, valsperkey);
    else if (keys[interNo_] != keys[interNo_ + 1])
      linearInterpolation (fraction, keys[interNo_], keys[interNo_ + 1], keyvals, interNo_, value, valsperkey);
    else
      System.err.println ("[Interpolator] [Error] internal error: interpolateFloatvalues: no appropriate interval found\n");
  } // interpolateFloatvalues


  public void gotEventCB (Field field, double timestamp)
  {
    // field will be set_fraction EventIn

    // do interpolation
    Field value_changed = interpolate ();

    // send value_changed event
    if (value_changed != null)
    {
      value_changed.sendEvent (timestamp);
    }
  }

} // Interpolator
