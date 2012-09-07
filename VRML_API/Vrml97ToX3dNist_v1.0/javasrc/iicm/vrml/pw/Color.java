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
 * Color.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960827
 *
 * changed: krosch, 19960827
 * changed: apesen, 19970409
 *
 * $Id: Color.java,v 1.3 1997/05/22 09:38:32 apesen Exp $
 */

 
package iicm.vrml.pw; 


// Color

public class Color extends Node
{
  public MFColor color;

  public String nodeName ()
  {
    return NodeNames.NODE_COLOR;
  }

  public void traverse (Traverser t)
  {
    t.tColor (this);
  }

  Color ()
  {
    addField ("color", color = new MFColor (), Field.F_EXPOSEDFIELD);
  }
} // Color
