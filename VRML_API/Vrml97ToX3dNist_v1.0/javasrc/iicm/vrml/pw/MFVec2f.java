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
 * MFVec2f.java
 *
 * created: krosch, 19960812
 *
 * changed: apesen, 19970526
 * changed: mpichler, 19970721
 *
 * $Id: MFVec2f.java,v 1.9 1997/07/21 16:14:07 mpichler Exp $
 */


package iicm.vrml.pw;

import iicm.vrml.pwutils.FloatArray;
import java.io.*;
import java.util.Hashtable;


/**
 * MFVec2f - Field that holds an array of 2D vectors/points
 * Copyright (c) 1996,97 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.1, latest change:  9 Jan 97
 */


public class MFVec2f extends MultiField
{
  private FloatArray values;

  MFVec2f ()
  {
    values = new FloatArray ();
  }

  MFVec2f (float[] vals)
  {
    values = new FloatArray (vals);
  }

  MFVec2f (FloatArray vals)
  {
    values = new FloatArray (vals);
  }

  public String fieldName ()
  {
    return FieldNames.FIELD_MFVec2f;
  }

  Field newFieldInstance ()
  {
    return new MFVec2f (values);
  }

  // hint: use getValueCount to get no. of set vectors
  final public float[] getValueData ()
  {
    return values.getData ();
  }

  final public int getValueCount ()
  {
    return values.getCount () / 2;
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
    values.setData (((MFVec2f) source).values);
  }

//   void appendValues (float x, float y)
//   {
//     values.append (x);
//     values.append (y);
//   }

  void read1Value (VRMLparser parser) throws IOException
  {
    StrTokenizer st = parser.istok;

    for (int i = 0;  i < 2;  i++)
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
    for (int i = 0;  i < num;  i++, j += 2)
      os.print (vals [j] + " " + vals [j+1] + sep);

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
    for (int i = 0;  i < num;  i++, j += 2) {
      os.print (vals [j] + " " + vals [j+1] + sep);
      //if ((i%8)==0 && num>8) os.print("\n");
    }
  }
} // MFVec2f
