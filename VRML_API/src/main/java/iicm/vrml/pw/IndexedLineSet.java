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
 * IndexedLineSet.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960826
 *
 * changed: krosch, 19960826
 * changed: apesen, 19970410
 *
 * $Id: IndexedLineSet.java,v 1.3 1997/05/22 10:19:20 apesen Exp $
 */


package iicm.vrml.pw; 


// IndexedLineSet

public class IndexedLineSet extends Geometry
{
  public SFNode color, coord;
  public MFInt32 colorIndex, coordIndex;
  public SFBool colorPerVertex;
  
  public String nodeName ()
  {
    return NodeNames.NODE_INDEXEDLINESET;
  }
  
  public void traverse (Traverser t)
  {
    t.tIndexedLineSet (this);
  }

  IndexedLineSet ()
  {
    addField ("set_colorIndex", new MFInt32 (), Field.F_EVENTIN);  // eventIn MFInt32 set_colorIndex
    addField ("set_coordIndex", new MFInt32 (), Field.F_EVENTIN);  // eventIn MFInt32 set_coordIndex
    addField ("color", color = new SFNode (), Field.F_EXPOSEDFIELD);
    addField ("coord", coord = new SFNode (), Field.F_EXPOSEDFIELD);
    addField ("colorIndex", colorIndex = new MFInt32 (), Field.F_FIELD);
    addField ("colorPerVertex", colorPerVertex = new SFBool (true), Field.F_FIELD);
    addField ("coordIndex", coordIndex = new MFInt32 (), Field.F_FIELD);
  }
} // IndexedLineSet
