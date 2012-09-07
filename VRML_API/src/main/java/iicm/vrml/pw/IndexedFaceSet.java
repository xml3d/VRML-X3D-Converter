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
 * IndexedFaceSet.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960812
 *
 * changed: mpichler, 19970114
 * changed: apesen, 19970410
 *
 * $Id: IndexedFaceSet.java,v 1.5 1997/05/22 10:06:23 apesen Exp $
 */


package iicm.vrml.pw; 

import java.io.*;
import java.util.*;


// IndexedFaceSet

public class IndexedFaceSet extends Geometry
{
  public SFNode color, coord, normal, texCoord;
  public SFBool ccw, colorPerVertex, convex, normalPerVertex, solid;
  public MFInt32 colorIndex, coordIndex, normalIndex, texCoordIndex;
  public SFFloat creaseAngle;

  public String nodeName ()
  {
    return NodeNames.NODE_INDEXEDFACESET;
  }

  public void traverse (Traverser t)
  {
    t.tIndexedFaceSet (this);
  }

  IndexedFaceSet ()
  {
    addField ("set_colorIndex", new MFInt32 (), Field.F_EVENTIN);     // eventIn MFInt32 set_colorIndex
    addField ("set_coordIndex", new MFInt32 (), Field.F_EVENTIN);     // eventIn MFInt32 set_coordIndex 
    addField ("set_normalIndex", new MFInt32 (), Field.F_EVENTIN);    // eventIn MFInt32 set_normalIndex
    addField ("set_texCoordIndex", new MFInt32 (), Field.F_EVENTIN);  // eventIn MFInt32 set_texcoordIndex
    addField ("color", color = new SFNode (), Field.F_EXPOSEDFIELD);
    addField ("coord", coord = new SFNode (), Field.F_EXPOSEDFIELD);
    addField ("normal", normal = new SFNode (), Field.F_EXPOSEDFIELD);
    addField ("texCoord", texCoord = new SFNode (), Field.F_EXPOSEDFIELD);
    addField ("ccw", ccw = new SFBool (true), Field.F_FIELD);
    addField ("colorIndex", colorIndex = new MFInt32 (), Field.F_FIELD);
    addField ("colorPerVertex", colorPerVertex = new SFBool (true), Field.F_FIELD);
    addField ("convex", convex = new SFBool (true), Field.F_FIELD);
    addField ("coordIndex", coordIndex = new MFInt32 (), Field.F_FIELD);
    addField ("creaseAngle", creaseAngle = new SFFloat (0.0f), Field.F_FIELD);
    addField ("normalIndex", normalIndex = new MFInt32 (), Field.F_FIELD);
    addField ("normalPerVertex", normalPerVertex = new SFBool (true), Field.F_FIELD);
    addField ("solid", solid = new SFBool (true), Field.F_FIELD);
    addField ("texCoordIndex", texCoordIndex = new MFInt32 (), Field.F_FIELD);
  }

  public void writeSubfields (PrintStream os, Hashtable wrefs)
  {
    // ordered output
    writeSubfield ("color", color, os, wrefs);
    writeSubfield ("coord", coord, os, wrefs);
    writeSubfield ("normal", normal, os, wrefs);
    writeSubfield ("texCoord", texCoord, os, wrefs);
    writeSubfield ("ccw", ccw, os, wrefs);
    writeSubfield ("colorIndex", colorIndex, os, wrefs);
    writeSubfield ("colorPerVertex", colorPerVertex, os, wrefs);
    writeSubfield ("convex", convex, os, wrefs);
    writeSubfield ("coordIndex", coordIndex, os, wrefs);
    writeSubfield ("creaseAngle", creaseAngle, os, wrefs);
    writeSubfield ("normalIndex", normalIndex, os, wrefs);
    writeSubfield ("normalPerVertex", normalPerVertex, os, wrefs);
    writeSubfield ("solid", solid, os, wrefs);
    writeSubfield ("texCoordIndex", texCoordIndex, os, wrefs);
  }
} // IndexedFaceSet
