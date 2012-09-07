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
 * SFRotation.java
 *
 * created: krosch, 19960821
 * changed: krosch, 19960831
 * changed: mpichler, 19961001
 * changed: apesen, 19970526
 *
 * $Id: SFRotation.java,v 1.4 1997/05/28 16:58:55 apesen Exp $
 */


package iicm.vrml.pw;

import java.io.*;
import java.util.Hashtable;


/**
 * SFRotation - Field that holds an axis of rotation (3 floats)
 * followed by the amount of right-handed rotation about that axis
 * (in radians)
 * Copyright (c) 1996 IICM
 *
 * @author Michael Pichler, Karin Roschker
 *
 */


public class SFRotation extends Field
{
  private float[] value = new float [4];
  // axis must be normalized;
  // may have a quaternion around too

  public String fieldName ()
  {
    return FieldNames.FIELD_SFRotation;
  }

  Field newFieldInstance ()
  {
    return new SFRotation (value[0], value[1], value[2], value[3]);
  }

  SFRotation (float x, float y, float z, float r)
  {
    value[0] = x;
    value[1] = y;
    value[2] = z;
    value[3] = r;
  }

  final public float[] getValue ()
  {
    return value;
  }

  final public void setValue (float x, float y, float z, float r)
  {
    value[0] = x;
    value[1] = y;
    value[2] = z;
    value[3] = r;
  }

  void copyValue (Field source)
  {
    float[] val = ((SFRotation) source).value;
    value[0] = val[0];
    value[1] = val[1];
    value[2] = val[2];
    value[3] = val[3];
  }

  void readValue (VRMLparser parser) throws IOException
  {
    for (int i = 0;  i < 4;  i++)
    {
      value [i] = (float) readFloatValue (parser.istok);
      if (readerror)
        return;
    }
  }

  void writeValue (PrintStream os, Hashtable writtenrefs)
  {
    os.print (value[0] + " " + value[1] + " " + value[2] + "  " + value[3]);
  }

  void writeX3dValue (PrintStream os, Hashtable writtenrefs)
  {
    os.print (value[0] + " " + value[1] + " " + value[2] + "  " + value[3]);
  }
} // SFRotation
