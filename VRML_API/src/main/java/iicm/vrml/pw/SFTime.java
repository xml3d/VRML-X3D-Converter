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
 * SFTime.java
 *
 * created: krosch, 19960822
 * changed: krosch, 19960830
 * changed: mpichler, 19961001
 * changed: apesen, 19970526
 *
 * $Id: SFTime.java,v 1.4 1997/05/28 16:55:55 apesen Exp $
 */


package iicm.vrml.pw;

import java.io.*;
import java.util.Hashtable;


/**
 * SFTime - Field that holds one douple
 * Copyright (c) 1996 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.1, latest change:  30 Aug 96
 */


public class SFTime extends Field
{
  private double value;  // = 0.0;

  public String fieldName ()
  {
    return FieldNames.FIELD_SFTime;
  }

  Field newFieldInstance ()
  {
    return new SFTime (value);
  }

  SFTime (double val)
  {
    value = val;
  }

  final public double getValue ()
  {
    return value;
  }

  final public void setValue (double val)
  {
    value = val;
  }

  void copyValue (Field source)
  {
    value = ((SFTime) source).value;
  }

  void readValue (VRMLparser parser) throws IOException
  {
    value = readFloatValue (parser.istok);  // reads double!!
  }

  void writeValue (PrintStream os, Hashtable writtenrefs)
  {
    os.print (value);
  }

  void writeX3dValue (PrintStream os, Hashtable writtenrefs)
  {
    os.print (value);
  }
} // SFTime
