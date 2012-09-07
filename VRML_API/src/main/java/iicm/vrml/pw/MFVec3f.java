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
 * MFVec3f.java
 *
 * created: mpichler, 19960808
 *
 * changed: apesen, 19970526
 * changed: mpichler, 19970721
 *
 * $Id: MFVec3f.java,v 1.10 1997/07/21 16:14:44 mpichler Exp $
 */


package iicm.vrml.pw;

import iicm.vrml.pwutils.FloatArray;
import java.io.*;
import java.util.Hashtable;


/**
 * MFVec3f - Field that holds an array of 3D vectors/points
 * Copyright (c) 1996,97 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.1, latest change:  9 Jan 97
 */


public class MFVec3f extends MultiField
{
  private FloatArray values;

  MFVec3f ()
  {
    values = new FloatArray ();
  }

  MFVec3f (float[] vals)
  {
    values = new FloatArray (vals);
  }

  MFVec3f (FloatArray vals)
  {
    values = new FloatArray (vals);
  }

  public String fieldName ()
  {
    return FieldNames.FIELD_MFVec3f;
  }

  Field newFieldInstance ()
  {
    return new MFVec3f (values);
  }

  // hint: use getValueCount to get no. of set vectors
  final public float[] getValueData ()
  {
    return values.getData ();
  }

  final public int getValueCount ()
  {
    return values.getCount () / 3;
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
    values.setData (((MFVec3f) source).values);
  }

  void setSize (int num)
  {
    values.setSize (num);  // no. of floats, not of vectors
  }

//   void appendValues (float x, float y, float z)
//   {
//     values.append (x);
//     values.append (y);
//     values.append (z);
//   }

  void read1Value (VRMLparser parser) throws IOException
  {
    StrTokenizer st = parser.istok;

    for (int i = 0;  i < 3;  i++)
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
    for (int i = 0;  i < num;  i++, j += 3)
      os.print (vals [j] + " " + vals [j+1] + " " + vals [j+2] + sep);

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
    for (int i = 0;  i < num;  i++, j += 3) {
      os.print (vals [j] + " " + vals [j+1] + " " + vals [j+2] + sep);
      //if ((i%4)==0 && num>4) os.print("\n");
    }
  }
} // MFVec3f
