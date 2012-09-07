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
 * SFFloat.java
 *
 * created: mpichler, 19960806
 * changed: mpichler, 19961001
 * changed: apesen, 19970526
 *
 * $Id: SFFloat.java,v 1.5 1997/08/01 18:31:39 mpichler Exp $
 */


package iicm.vrml.pw;

import java.io.*;
import java.util.Hashtable;


/**
 * SFFloat - Field that holds one float
 * Copyright (c) 1996 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.1, latest change:  7 Aug 96
 */


public class SFFloat extends Field
{
  //private float value;  // = 0.0;

  public float value;

  public String fieldName ()
  {
    return FieldNames.FIELD_SFFloat;
  }

  Field newFieldInstance ()
  {
    return new SFFloat (value);
  }

  public SFFloat (float val)
  {
    value = val;
  }

  final public float getValue ()
  {
    return value;
  }

  final public void setValue (float val)
  {
    value = val;
  }

  void copyValue (Field source)
  {
    value = ((SFFloat) source).value;
  }

  void readValue (VRMLparser parser) throws IOException
  {
    value = (float) readFloatValue (parser.istok);
  }

  void writeValue (PrintStream os, Hashtable writtenrefs)
  {
    os.print (value);
  }

  void writeX3dValue (PrintStream os, Hashtable writtenrefs)
  {
    os.print (value);
  }
} // SFFloat
