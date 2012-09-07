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
 * Coordinate.java
 * Copyright (c) 1997 IICM
 *
 * created: mpichler, 19960808
 *
 * changed: mpichler, 19960808
 * changed: apesen, 19970409
 *
 * $Id: Coordinate.java,v 1.3 1997/05/22 09:42:18 apesen Exp $
 */


package iicm.vrml.pw;


// Coordinate

public class Coordinate extends Node
{
  public MFVec3f point;

  public String nodeName ()
  {
    return NodeNames.NODE_COORDINATE;
  }

  public void traverse (Traverser t)
  {
    t.tCoordinate (this);
  }

  Coordinate ()
  {
    addField ("point", point = new MFVec3f (), Field.F_EXPOSEDFIELD);
    // VRML 2.0: default empty array (VRML 1.0: [0 0 0])
  }
} // Coordinate
