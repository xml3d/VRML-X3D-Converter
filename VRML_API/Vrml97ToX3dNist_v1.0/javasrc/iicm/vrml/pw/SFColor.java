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
 * SFColor.java
 *
 * created: krosch, 19960831
 * changed: krosch, 19960831
 * changed: mpichler, 19961001
 * changed: apesen, 19970526
 *
 * $Id: SFColor.java,v 1.5 1997/08/01 18:31:07 mpichler Exp $
 */


package iicm.vrml.pw;

import java.io.*;
import java.util.Hashtable;


/**
 * SFColor - Field that holds an RGB color (3 floats)
 * Copyright (c) 1996 IICM
 *
 * @author Michael Pichler, Karin Roschker
 *
 */


public class SFColor extends Field
{
  private float[] value = new float [3];

  public String fieldName ()
  {
    return FieldNames.FIELD_SFColor;
  }

  Field newFieldInstance ()
  {
    return new SFColor (value[0], value[1], value[2]);
  }

  public SFColor (float R, float G, float B)
  {
    value[0] = R;
    value[1] = G;
    value[2] = B;
  }

  final public float[] getValue ()
  {
    return value;
  }

  final public void setValue (float R, float G, float B)
  {
    value[0] = R;
    value[1] = G;
    value[2] = B;
  }

  void copyValue (Field source)
  {
    float[] val = ((SFColor) source).value;
    value[0] = val[0];
    value[1] = val[1];
    value[2] = val[2];
  }

  void readValue (VRMLparser parser) throws IOException
  {
    for (int i = 0;  i < 3;  i++)
    {
      // checks if the read floating point numbers are in the range
      // of 0.0 to 1.0. If not, they are set to 0.0

      value [i] = (float) readFloatValue (parser.istok);
      if (value[i] < 0.0f || value[i] > 1.0f) 
      {
        parser.pout.error ("RGB color out of range - value set to 0.0");
        value[i] = 0.0f;
      }
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
} // SFColor
