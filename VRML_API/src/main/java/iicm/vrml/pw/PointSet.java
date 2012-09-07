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
 * PointSet.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960826
 *
 * changed: krosch, 19960826
 * changed: apesen, 19970410
 *
 * $Id: PointSet.java,v 1.3 1997/05/22 11:48:41 apesen Exp $
 */

 
package iicm.vrml.pw; 


// PointSet

public class PointSet extends Geometry
{
  public SFNode color, coord;

  public String nodeName ()
  {
    return NodeNames.NODE_POINTSET;
  }

  public void traverse (Traverser t)
  {
    t.tPointSet (this);
  }

  PointSet ()
  {
    addField ("color", color = new SFNode (), Field.F_EXPOSEDFIELD);
    addField ("coord", coord = new SFNode (), Field.F_EXPOSEDFIELD);
  }
} // PointSet
