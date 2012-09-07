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
 * SFString.java
 *
 * created: krosch, 19960823
 * changed: krosch, 19960830
 * changed: mpichler, 19961001
 * changed: apesen, 19970526
 *
 * $Id: SFString.java,v 1.5 1997/05/28 16:57:07 apesen Exp $
 */


package iicm.vrml.pw;

import java.io.*;
import java.util.Hashtable;


/**
 * SFString - Field that holds one String
 * Copyright (c) 1996 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.1, latest change: 30 Aug 96
 */


public class SFString extends Field
{
  private String value;

  public String fieldName ()
  {
    return FieldNames.FIELD_SFString;
  }

  Field newFieldInstance ()
  {
    return new SFString (value);
  }

  SFString (String val)
  {
    value = val;
  }

  final public String getValue ()
  {
    return value;
  }

  final public void setValue (String val)
  {
    value = val;
  }

  void copyValue (Field source)
  {
    value = ((SFString) source).value;
  }

  void readValue (VRMLparser parser) throws IOException
  {
    value = readStringValue (parser.istok);
  }

  void writeValue (PrintStream os, Hashtable writtenrefs)
  {
    writeQuotedString (os, value);
  }

  void writeX3dValue (PrintStream os, Hashtable writtenrefs)
  {
    //writeQuotedString (os, value);
    writeX3dSFString(os, value);
  }

  /**
   * print with surrounding quotes, escape quotes and backslashes
   */

  public static void writeQuotedString (PrintStream os, String str)
  {
    os.write ('"');
    int len = (str == null) ? 0 : str.length ();
    for (int i = 0;  i < len;  i++)
    {
      char c = str.charAt (i);
      if (c == '\\' || c == '"')
        os.write ('\\');
      os.write (c);
    }
    os.write ('"');
  }

  public static void writeX3dSFString (PrintStream os, String str)
  {
    //os.write ('"');
    int len = (str == null) ? 0 : str.length ();
    for (int i = 0;  i < len;  i++)
    {
      char c = str.charAt (i);
      if (c == '\\' || c == '"')
        os.write ('\\');
      os.write (c);
    }
    //os.write ('"');
  }


  
} // SFString
