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
 * ScalarInterpolator.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960824
 *
 * changed: krosch, 19961102
 * changed: apesen, 19970605
 *
 * $Id: ScalarInterpolator.java,v 1.5 1997/08/04 10:15:12 apesen Exp $
 */


// ScalarInterpolator
 
package iicm.vrml.pw; 

public class ScalarInterpolator extends Interpolator
{
  public MFFloat key;
  public MFFloat keyValue;
  public SFFloat set_fraction;
  public SFFloat value_changed;
  
  private float[] values = new float[1];

  public String nodeName ()
  {
    return NodeNames.NODE_SCALARINTERPOLATOR;
  }

  public void traverse (Traverser t)
  {
    t.tScalarInterpolator (this);
  }

  ScalarInterpolator ()
  {
    addField ("set_fraction", set_fraction = new SFFloat (0.0f), Field.F_EVENTIN);
    addField ("key", key = new MFFloat (), Field.F_EXPOSEDFIELD);
    addField ("keyValue", keyValue = new MFFloat (), Field.F_EXPOSEDFIELD);
    addField ("value_changed", value_changed = new SFFloat (0.0f), Field.F_EVENTOUT);
    set_fraction.setEventCallback (this);  // see Interpolator.gotEventCB
  }

  Field interpolate ()
  {
    if (value_changed.receiver_ == null)
      return null;
    interpolateFloatvalues (set_fraction.getValue (), key.getValueData (), key.getValueCount (),
       keyValue.getValueData (), keyValue.getValueCount (), values, 1);
    value_changed.setValue (values[0]);
    return value_changed;
  }

} // ScalarInterpolator
