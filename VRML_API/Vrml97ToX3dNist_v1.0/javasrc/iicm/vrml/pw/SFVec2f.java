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
 * SFVec2f.java
 *
 * created: krosch, 19960812
 * changed: krosch, 19960812
 * changed: apesen, 19970526
 *
 * $Id: SFVec2f.java,v 1.4 1997/05/28 16:56:20 apesen Exp $
 */


package iicm.vrml.pw;

import java.io.*;
import java.util.Hashtable;


/**
 * SFVec2f - Field that holds a 2D vector (2 floats)
 * Copyright (c) 1996 IICM
 *
 * @author Michael Pichler, Karin Roschker
 */


public class SFVec2f extends Field
{
  private float[] value = new float [2];

  public String fieldName ()
  {
    return FieldNames.FIELD_SFVec2f;
  }

  Field newFieldInstance ()
  {
    return new SFVec2f (value[0], value[1]);
  }

  SFVec2f (float x, float y)
  {
    value[0] = x;
    value[1] = y;
  }

  final public float[] getValue ()
  {
    return value;
  }

  final public void setValue (float x, float y)
  {
    value[0] = x;
    value[1] = y;
  }

  void copyValue (Field source)
  {
    float[] val = ((SFVec2f) source).value;
    value[0] = val[0];
    value[1] = val[1];
  }

  void readValue (VRMLparser parser) throws IOException
  {
    for (int i = 0;  i < 2;  i++)
    {
      value [i] = (float) readFloatValue (parser.istok);
      if (readerror)
        return;
    }
  }

  void writeValue (PrintStream os, Hashtable writtenrefs)
  {
    os.print (value [0] + " " + value [1]);
  }

  void writeX3dValue (PrintStream os, Hashtable writtenrefs)
  {
    os.print (value [0] + " " + value [1]);
  }
} // SFVec2f
