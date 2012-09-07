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
 * MFFloat.java
 *
 * created: mpichler, 19960808
 *
 * changed: apesen, 19970526
 * changed: mpichler, 19970723
 *
 * $Id: MFFloat.java,v 1.6 1997/07/23 17:46:14 mpichler Exp $
 */


package iicm.vrml.pw;

import iicm.vrml.pwutils.FloatArray;
import java.io.*;
import java.util.Hashtable;


/**
 * MFFloat - Field that holds an array of floats
 * Copyright (c) 1996 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.1, latest change:  1 Oct 96
 */


public class MFFloat extends MultiField
{
  private FloatArray values;

  MFFloat ()
  {
    values = new FloatArray ();
  }

  MFFloat (float[] vals)
  {
    values = new FloatArray (vals);
  }

  MFFloat (FloatArray vals)
  {
    values = new FloatArray (vals);
  }

  public String fieldName ()
  {
    return FieldNames.FIELD_MFFloat;
  }

  Field newFieldInstance ()
  {
    return new MFFloat (values);
  }

  // hint: use getValueCount to get no. of set floats
  final public float[] getValueData ()
  {
    return values.getData ();
  }

  final public int getValueCount ()
  {
    return values.getCount ();
  }

  // get a copy of the values (proper array size)
  final public float[] getValueCopy ()
  {
    float[] return_vals = new float[values.getCount ()];

    System.arraycopy (values.getData (), 0, return_vals, 0, values.getCount ());
    return return_vals;
  }

  final public void setValue (float[] vals)
  {
    values.setData (vals);
  }

  void copyValue (Field source)
  {
    values.setData (((MFFloat) source).values);
  }

  void read1Value (VRMLparser parser) throws IOException
  {
    values.append ((float) readFloatValue (parser.istok));
  }

  boolean clearValues ()
  {
    return values.clearData ();
  }

  void writeValue (PrintStream os, Hashtable writtenrefs)
  {
    int num = values.getCount ();
    float[] vals = values.getData ();
    if (num != 1)
      os.print ("[");

    for (int i = 0;  i < num;  i++)
      os.print (" " + vals [i]);

    if (num != 1)
      os.print (" ]");
  }

  void writeX3dValue (PrintStream os, Hashtable writtenrefs)
  {
    int num = values.getCount ();
    float[] vals = values.getData ();

    for (int i = 0;  i < num;  i++) {
      os.print (" " + vals [i]);
      //if ((i%10)==0 && num>10) os.print("\n");
    }
  }
} // MFFloat
