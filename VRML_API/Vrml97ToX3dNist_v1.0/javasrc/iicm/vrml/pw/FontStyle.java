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
 * FontStyle.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960829
 *
 * changed: krosch, 19960829
 * changed: apesen, 19970410
 * changed: mpichler, 19970616
 *
 * $Id: FontStyle.java,v 1.4 1997/07/03 16:58:01 mpichler Exp $
 */

 
package iicm.vrml.pw; 


// FontStyle

public class FontStyle extends AppearNode
{
  public SFString language, style;
  public MFString justify, family;
  public SFBool horizontal, leftToRight, topToBottom;
  public SFFloat size, spacing;

  public String[] defFamily = { "SERIF" };
  public String[] defJustify = { "BEGIN" };

  public String nodeName ()
  {
    return NodeNames.NODE_FONTSTYLE;
  }

  public void traverse (Traverser t)
  {
    t.tFontStyle (this);
  }

  FontStyle ()
  {
    addField ("family", family = new MFString (defFamily), Field.F_FIELD);
    addField ("language", language = new SFString (""), Field.F_FIELD);
    addField ("style", style = new SFString ("PLAIN"), Field.F_FIELD);
    addField ("justify", justify = new MFString (defJustify), Field.F_FIELD);
    addField ("horizontal", horizontal = new SFBool (true), Field.F_FIELD);    
    addField ("leftToRight", leftToRight = new SFBool (true), Field.F_FIELD);    
    addField ("topToBottom", topToBottom = new SFBool (true), Field.F_FIELD);    
    addField ("size", size = new SFFloat (1.0f), Field.F_FIELD);    
    addField ("spacing", spacing = new SFFloat (1.0f), Field.F_FIELD);
  }
} // FontStyle
