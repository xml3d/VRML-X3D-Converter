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
 * LOD.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960824
 *
 * changed: krosch, 19960829
 * changed: mpichler, 19960917
 * changed: apesen, 19970410
 *
 * $Id: LOD.java,v 1.3 1997/05/22 10:21:12 apesen Exp $
 */


package iicm.vrml.pw; 


// LOD

public class LOD extends GroupNode
{
  public MFNode level;
  public SFVec3f center;
  public MFFloat range;

  public String nodeName ()
  {
    return NodeNames.NODE_LOD;
  }

  public void traverse (Traverser t)
  {
    t.tLOD (this);
  }

  LOD ()
  {
    addField ("level", level = new MFNode (), Field.F_EXPOSEDFIELD);  // children
    addField ("center", center = new SFVec3f (0.0f, 0.0f, 0.0f), Field.F_FIELD);
    addField ("range", range = new MFFloat (), Field.F_FIELD);
  }
} // LOD
