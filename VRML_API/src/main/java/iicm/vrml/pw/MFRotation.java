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
 * MFRotation.java
 *
 * created: krosch, 19960820
 *
 * changed: krosch, 19960831
 * changed: apesen, 19970526
 * changed: mpichler, 19970721
 *
 * $Id: MFRotation.java,v 1.9 1997/07/21 16:12:17 mpichler Exp $
 */


package iicm.vrml.pw;

import iicm.vrml.pwutils.FloatArray;
import java.io.*;
import java.util.Hashtable;


/**
 * MFRotation - Field that holds an array of rotations,
 * each represented by 4 floats (3D axis and rotation angle)
 * Copyright (c) 1996,97 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.1, latest change:  9 Jan 97
 */


public class MFRotation extends MultiField
{
  private FloatArray values;

  MFRotation ()
  {
    values = new FloatArray ();
  }

  MFRotation (float[] vals)
  {
    values = new FloatArray (vals);
  }

  MFRotation (FloatArray vals)
  {
    values = new FloatArray (vals);
  }

  public String fieldName ()
  {
    return FieldNames.FIELD_MFRotation;
  }

  Field newFieldInstance ()
  {
    return new MFRotation (values);
  }

  // hint: use getValueCount to get no. of set rotations
  final public float[] getValueData ()
  {
    return values.getData ();
  }

  final public int getValueCount ()
  {
    return values.getCount () / 4;
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
    values.setData (((MFRotation) source).values);
  }

//   void appendValues (float x, float y, float z, float a)
//   {
//     values.append (x);
//     values.append (y);
//     values.append (z);
//     values.append (a);
//   }

  void read1Value (VRMLparser parser) throws IOException
  {
    StrTokenizer st = parser.istok;

    for (int i = 0;  i < 4;  i++)
    {
      values.append ((float) readFloatValue (st));
      if (readerror)
        return;
      // readFloatValue will never eat ']'
    }
  }

  boolean clearValues ()
  {
    return values.clearData ();
  }

  void writeValue (PrintStream os, Hashtable writtenrefs)
  {
    int num = getValueCount ();
    float[] vals = values.getData ();
    String sep = " ";
    if (num != 1)
    { sep = ", ";  // for better human reading
      os.print ("[ ");
    }

    int j = 0;
    for (int i = 0;  i < num;  i++, j += 4)
      os.print (vals[j] + " " + vals[j+1] + " " + vals[j+2] + "  " + vals[j+3] + sep);

    if (num != 1)
      os.print ("]");
  }

  void writeX3dValue (PrintStream os, Hashtable writtenrefs)
  {
    int num = getValueCount ();
    float[] vals = values.getData ();
    String sep = " ";
    if (num != 1)
    { 
	sep = ", ";  // for better human reading
    }

    int j = 0;
    for (int i = 0;  i < num;  i++, j += 4)
      os.print (vals[j] + " " + vals[j+1] + " " + vals[j+2] + "  " + vals[j+3] + sep);

    //if (num != 1)
    //os.print ("]");
  }
} // MFRotation
