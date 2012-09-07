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
 * SFInt32.java
 *
 * created: krosch, 19960822
 * changed: krosch, 19960830
 * changed: mpichler, 19961001
 * changed: apesen, 19970526
 *
 * $Id: SFInt32.java,v 1.4 1997/05/28 16:55:14 apesen Exp $
 */


package iicm.vrml.pw;

import java.io.*;
import java.util.Hashtable;


/**
 * SFInt32 - Field that holds one int (32 bit)
 * Copyright (c) 1996 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.1, latest change:  30 Aug 96
 */


public class SFInt32 extends Field
{
  private int value;  // = 0

  public String fieldName ()
  {
    return FieldNames.FIELD_SFInt32;
  }

  Field newFieldInstance ()
  {
    return new SFInt32 (value);
  }

  SFInt32 (int val)
  {
    value = val;
  }

  final public int getValue ()
  {
    return value;
  }

  final public void setValue (int val)
  {
    value = val;
  }

  void copyValue (Field source)
  {
    value = ((SFInt32) source).value;
  }

  void readValue (VRMLparser parser) throws IOException
  {
    value = (int) readIntValue (parser.istok);
  }

  void writeValue (PrintStream os, Hashtable writtenrefs)
  {
    os.print (value);
  }

  void writeX3dValue (PrintStream os, Hashtable writtenrefs)
  {
    os.print (value);
  }
} // SFInt32
