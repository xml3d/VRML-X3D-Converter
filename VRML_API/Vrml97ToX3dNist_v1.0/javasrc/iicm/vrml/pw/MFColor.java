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
 * MFColor.java
 *
 * created: krosch, 19960830
 *
 * changed: krosch, 19960920
 * changed: apesen, 19970526
 * changed: mpichler, 19970721
 *
 * $Id: MFColor.java,v 1.9 1997/07/21 16:11:39 mpichler Exp $
 */


package iicm.vrml.pw;

import iicm.vrml.pwutils.FloatArray;
import java.io.*;
import java.util.Hashtable;


/**
 * MFColor - Field that holds an array of RGB triples
 * Copyright (c) 1996,97 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.1, latest change:  9 Jan 97
 */


public class MFColor extends MultiField
{
  private FloatArray values;

  MFColor ()
  {
    values = new FloatArray ();
  }

  MFColor (float[] vals)
  {
    values = new FloatArray (vals);
  }

  MFColor (FloatArray vals)
  {
    values = new FloatArray (vals);
  }

  public String fieldName ()
  {
    return FieldNames.FIELD_MFColor;
  }

  Field newFieldInstance ()
  {
    return new MFColor (values);
  }

  // hint: use getValueCount to get no. of set colors
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
    values.setData (((MFColor) source).values);
  }

//   void appendValues (float R, float G, float B)
//   {
//     values.append (R);
//     values.append (G);
//     values.append (B);
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
      // TODO: range check (0.0 to 1.0)
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
    {sep = ", ";  // for better human reading
    //os.print ("[ ");
    }

    int j = 0;
    for (int i = 0;  i < num;  i++, j += 3) 
      os.print (vals [j] + " " + vals [j+1] + " " + vals [j+2] + sep);

    //if (num != 1)
      //os.print ("]");
  }
} // MFColor
