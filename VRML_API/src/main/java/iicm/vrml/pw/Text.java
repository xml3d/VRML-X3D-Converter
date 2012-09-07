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
 * Text.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960826
 *
 * changed: krosch, 19960826
 * changed: apesen, 19970411
 *
 * $Id: Text.java,v 1.3 1997/05/22 15:13:58 apesen Exp $
 */

 
package iicm.vrml.pw; 


// Text

public class Text extends Geometry
{
  public MFString string;
  public SFNode fontStyle;
  public MFFloat length;
  public SFFloat maxExtent;

  public String nodeName ()
  {
    return NodeNames.NODE_TEXT;
  }

  public void traverse (Traverser t)
  {
    t.tText (this);
  }

  Text ()
  {
    addField ("string", string = new MFString (), Field.F_EXPOSEDFIELD);
    addField ("fontStyle", fontStyle = new SFNode (), Field.F_EXPOSEDFIELD);
    addField ("length", length = new MFFloat (), Field.F_EXPOSEDFIELD);
    addField ("maxExtent", maxExtent = new SFFloat (0.0f), Field.F_EXPOSEDFIELD);
  }
} // Text
