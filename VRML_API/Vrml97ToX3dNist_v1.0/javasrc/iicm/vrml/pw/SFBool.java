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
 * SFBool.java
 *
 * created: mpichler, 19960807
 * changed: krosch, 19960812
 * changed: mpichler, 19961001
 * changed: apesen, 19970526
 *
 * $Id: SFBool.java,v 1.4 1997/05/28 16:59:11 apesen Exp $
 */


package iicm.vrml.pw;

import java.io.*;
import java.util.Hashtable;


/**
 * SFBool - Field that holds one boolean
 * Copyright (c) 1996 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.1, latest change:  8 Aug 96
 */


public class SFBool extends Field
{
  private boolean value;  // = false

  public String fieldName ()
  {
    return FieldNames.FIELD_SFBool;
  }

  Field newFieldInstance ()
  {
    return new SFBool (value);
  }

  SFBool (boolean val)
  {
    value = val;
  }

  final public boolean getValue ()
  {
    return value;
  }

  final public void setValue (boolean val)
  {
    value = val;
  }

  void copyValue (Field source)
  {
    value = ((SFBool) source).value;
  }

  void readValue (VRMLparser parser) throws IOException
  {
    value = readBoolValue (parser.istok);
  }

  void writeValue (PrintStream os, Hashtable writtenrefs)
  {
    os.print (value ? STR_TRUE : STR_FALSE);
  }

  void writeX3dValue (PrintStream os, Hashtable writtenrefs)
  {
    //os.print (value ? STR_TRUE : STR_FALSE);
    os.print(value ? "true" : "false");
  }
} // SFBool
