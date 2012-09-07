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
 * Box.java
 * Copyright (c) 1997 IICM
 *
 * created: mpichler, 19960807
 *
 * changed: mpichler, 19970113
 * changed: apesen, 19970409
 *
 * $Id: Box.java,v 1.4 1997/05/22 09:35:50 apesen Exp $
 */


package iicm.vrml.pw;


// Box

public class Box extends Geometry
{
  public SFVec3f size;

  public String nodeName ()
  {
    return NodeNames.NODE_BOX;
  }

  public void traverse (Traverser t)
  {
    t.tBox (this);
  }

  Box ()
  {
    addField ("size", size = new SFVec3f (2.0f, 2.0f, 2.0f), Field.F_FIELD);
  }
} // Box
