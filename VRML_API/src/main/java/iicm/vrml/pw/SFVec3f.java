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
 * SFVec3f.java
 *
 * created: mpichler, 19960807
 * changed: mpichler, 19960807
 * changed: apesen, 19970526
 *
 * $Id: SFVec3f.java,v 1.5 1997/08/01 18:31:18 mpichler Exp $
 */


package iicm.vrml.pw;

import java.io.*;
import java.util.Hashtable;


/**
 * SFVec3f - Field that holds a 3D vector (3 floats)
 * Copyright (c) 1996 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.1, latest change:  7 Aug 96
 */


public class SFVec3f extends Field
{
  private float[] value = new float [3];

  public String fieldName ()
  {
    return FieldNames.FIELD_SFVec3f;
  }

  Field newFieldInstance ()
  {
    return new SFVec3f (value[0], value[1], value[2]);
  }

  public SFVec3f (float x, float y, float z)
  {
    value[0] = x;
    value[1] = y;
    value[2] = z;
  }

  final public float[] getValue ()
  {
    return value;
  }

  final public void setValue (float x, float y, float z)
  {
    value[0] = x;
    value[1] = y;
    value[2] = z;
  }

  void copyValue (Field source)
  {
    float[] val = ((SFVec3f) source).value;
    value[0] = val[0];
    value[1] = val[1];
    value[2] = val[2];
  }

  void readValue (VRMLparser parser) throws IOException
  {
    for (int i = 0;  i < 3;  i++)
    {
      value [i] = (float) readFloatValue (parser.istok);
      if (readerror)
        return;
    }
  }

  void writeValue (PrintStream os, Hashtable writtenrefs)
  {
    os.print (value [0] + " " + value [1] + " " + value [2]);
  }

  void writeX3dValue (PrintStream os, Hashtable writtenrefs)
  {
    os.print (value [0] + " " + value [1] + " " + value [2]);
  }
} // SFVec3f
