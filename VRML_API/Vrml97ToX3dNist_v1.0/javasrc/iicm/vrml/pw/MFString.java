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
 * MFString.java
 *
 * created: krosch, 19960823
 * changed: krosch, 19960830
 * changed: apesen, 19970526
 * changed: mpichler, 19970721
 *
 * $Id: MFString.java,v 1.7 1997/07/21 16:13:11 mpichler Exp $
 */


package iicm.vrml.pw;

import iicm.vrml.pwutils.StringArray;
import java.io.*;
import java.util.Hashtable;


/**
 * MFString - Field that holds an array of Strings
 * Copyright (c) 1996 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.1, latest change:  1 Oct 96
 */


public class MFString extends MultiField
{
  // should use an ordinary Vector of Strings here
  //private StringArray values;
  public StringArray values;

  MFString ()
  {
    values = new StringArray ();
  }

  MFString (String[] vals)
  {
    values = new StringArray (vals);
  }

  MFString (StringArray vals)
  {
    values = new StringArray (vals);
  }

  public String fieldName ()
  {
    return FieldNames.FIELD_MFString;
  }

  Field newFieldInstance ()
  {
    return new MFString (values);
  }

  // hint: use getValueCount to get no. of set strings
  final public String[] getValueData ()
  {
    return values.getData ();
  }

  final public int getValueCount ()
  {
    return values.getCount ();
  }

  // get a copy of the values (proper array size)
  final public String[] getValueCopy ()
  {
    String[] return_vals = new String[values.getCount ()];

    System.arraycopy (values.getData (), 0, return_vals, 0, values.getCount ());
    return return_vals;
  }

  final public void setValue (String[] vals)
  {
    values.setData (vals);
  }

  void copyValue (Field source)
  {
    values.setData (((MFString) source).values);
  }

  void read1Value (VRMLparser parser) throws IOException
  {
    values.append (readStringValue (parser.istok));
  }

  boolean clearValues ()
  {
    return values.clearData ();
  }

  void writeValue (PrintStream os, Hashtable writtenrefs)
  {
    int num = values.getCount ();
    String[] vals = values.getData ();
    if (num != 1)
      os.print ("[");

    for (int i = 0;  i < num;  i++)
    {
      os.print (" ");
      SFString.writeQuotedString (os, vals[i]);
    }

    if (num != 1)
      os.print (" ]");
  }

  void writeX3dValue (PrintStream os, Hashtable writtenrefs)
  {
    int num = values.getCount ();
    String[] vals = values.getData ();
    //if (num != 1)
    //os.print ("[");

    for (int i = 0;  i < num;  i++)
    {
      SFString.writeQuotedString (os, vals[i]);
      if (i!=num-1) os.print(" ");
      if ((i%10)==0 && num>10) os.print("\n");
    }
  }
} // MFString
