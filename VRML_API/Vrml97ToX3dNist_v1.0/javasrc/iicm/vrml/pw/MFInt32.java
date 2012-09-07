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
 * MFInt32.java
 *
 * created: krosch, 19960822
 *
 * changed: krosch, 19961001
 * changed: mpichler, 19970723
 *
 * $Id: MFInt32.java,v 1.8 1997/07/23 17:46:31 mpichler Exp $
 */


package iicm.vrml.pw;

import iicm.vrml.pwutils.IntArray;
import java.io.*;
import java.util.Hashtable;


/**
 * MFInt32 - Field that holds an array of ints (32 bit)
 * Copyright (c) 1996 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.1, latest change:  1 Oct 96
 */


public class MFInt32 extends MultiField
{
  /*private*/ protected IntArray values;

  MFInt32 ()
  {
    values = new IntArray ();
  }

  MFInt32 (int[] vals)
  {
    values = new IntArray (vals);
  }

  MFInt32 (IntArray vals)
  {
    values = new IntArray (vals);
  }

  public String fieldName ()
  {
    return FieldNames.FIELD_MFInt32;
  }

  Field newFieldInstance ()
  {
    return new MFInt32 (values);
  }

  // hint: use getValueCount to get no. of set ints
  final public int[] getValueData ()
  {
    return values.getData ();
  }

  final public int getValueCount ()
  {
    return values.getCount ();
  }

  // get a copy of the values (proper array size)
  final public int[] getValueCopy ()
  {
    int[] return_vals = new int[values.getCount ()];

    System.arraycopy (values.getData (), 0, return_vals, 0, values.getCount ());
    return return_vals;
  }

  final public void setValue (int[] vals)
  {
    values.setData (vals);
  }

  void copyValue (Field source)
  {
    values.setData (((MFInt32) source).values);
  }

  void read1Value (VRMLparser parser) throws IOException
  {
    values.append (readIntValue (parser.istok));
  }

  boolean clearValues ()
  {
    return values.clearData ();
  }

  void writeValue (PrintStream os, Hashtable writtenrefs)
  {
    int num = values.getCount ();
    int[] vals = values.getData ();
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
    int[] vals = values.getData ();
    //if (num != 1)
    //os.print ("[");

    for (int i = 0;  i < num;  i++) 
      os.print (" " + vals [i]);

    //if (num != 1)
    //os.print (" ]");
  }
} // MFInt32
