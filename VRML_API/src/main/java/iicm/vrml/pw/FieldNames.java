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
 * FieldNames.java
 * definiton of field names
 *
 * created: mpichler, 19960930
 * changed: mpichler, 19970109
 *
 * $Id: FieldNames.java,v 1.4 1997/05/22 15:38:10 apesen Exp $
 */


package iicm.vrml.pw;


/**
 * FieldNames - definition of field names; field creation by name
 * Copyright (c) 1996,97 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.1, latest change:  9 Jan 97
 */


abstract public class FieldNames
{
  // no need ever to create an instance of this class
  private FieldNames ()  { }

  // single valued fields (SF*)
  public final static String FIELD_SFBool = "SFBool";
  public final static String FIELD_SFColor = "SFColor";
  public final static String FIELD_SFFloat = "SFFloat";
  public final static String FIELD_SFImage = "SFImage";
  public final static String FIELD_SFInt32 = "SFInt32";
  public final static String FIELD_SFNode = "SFNode";
  public final static String FIELD_SFRotation = "SFRotation";
  public final static String FIELD_SFString = "SFString";
  public final static String FIELD_SFTime = "SFTime";
  public final static String FIELD_SFVec2f = "SFVec2f";
  public final static String FIELD_SFVec3f = "SFVec3f";

  // multi valued fields (MF*)
  public final static String FIELD_MFColor = "MFColor";
  public final static String FIELD_MFFloat = "MFFloat";
  public final static String FIELD_MFInt32 = "MFInt32";
  public final static String FIELD_MFNode = "MFNode";
  public final static String FIELD_MFRotation = "MFRotation";
  public final static String FIELD_MFString = "MFString";
  public final static String FIELD_MFVec2f = "MFVec2f";
  public final static String FIELD_MFVec3f = "MFVec3f";


  /**
   * create field from its name
   */

  final static Field createFieldFromName (String name)
  {
    // single valued fields (SF*)
    if (name.equals (FIELD_SFBool))      return new SFBool (false);
    if (name.equals (FIELD_SFColor))     return new SFColor (0.0f, 0.0f, 0.0f);
    if (name.equals (FIELD_SFFloat))     return new SFFloat (0.0f);
    if (name.equals (FIELD_SFImage))     return new SFImage ();
    if (name.equals (FIELD_SFInt32))     return new SFInt32 (0);
    if (name.equals (FIELD_SFNode))      return new SFNode ();
    if (name.equals (FIELD_SFRotation))  return new SFRotation (1.0f, 0.0f, 0.0f, 0.0f);
    if (name.equals (FIELD_SFString))    return new SFString ("");
    if (name.equals (FIELD_SFTime))      return new SFTime (0.0);
    if (name.equals (FIELD_SFVec2f))     return new SFVec2f (0.0f, 0.0f);
    if (name.equals (FIELD_SFVec3f))     return new SFVec3f (0.0f, 0.0f, 0.0f);

    // multi valued fields (MF*)
    if (name.equals (FIELD_MFColor))     return new MFColor ();
    if (name.equals (FIELD_MFFloat))     return new MFFloat ();
    if (name.equals (FIELD_MFInt32))     return new MFInt32 ();
    if (name.equals (FIELD_MFNode))      return new MFNode ();
    if (name.equals (FIELD_MFRotation))  return new MFRotation ();
    if (name.equals (FIELD_MFString))    return new MFString ();
    if (name.equals (FIELD_MFVec2f))     return new MFVec2f ();
    if (name.equals (FIELD_MFVec3f))     return new MFVec3f ();

    return null;  // no match

  } // createFieldFromName



} // FieldNames
