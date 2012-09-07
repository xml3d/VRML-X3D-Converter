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
 * ElevationGrid.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960811
 * changed: mpichler, 19960917
 * changed: apesen, 19970409
 *
 * $Id: ElevationGrid.java,v 1.3 1997/05/22 09:53:04 apesen Exp $
 */

 
package iicm.vrml.pw; 


// ElevationGrid

public class ElevationGrid extends Geometry
{
  public SFNode color, normal, texCoord;
  public MFFloat height;
  public SFBool ccw, colorPerVertex, normalPerVertex, solid;
  public SFFloat creaseAngle, xSpacing, zSpacing;
  public SFInt32 xDimension, zDimension;

  public String nodeName ()
  {
    return NodeNames.NODE_ELEVATIONGRID;
  }

  public void traverse (Traverser t)
  {
    t.tElevationGrid (this);
  }

  ElevationGrid ()
  {
    addField ("set_height", new MFFloat (), Field.F_EVENTIN);  // eventIn MFFloat set_height
    addField ("color", color = new SFNode (), Field.F_EXPOSEDFIELD);
    addField ("normal", normal = new SFNode (), Field.F_EXPOSEDFIELD);
    addField ("texCoord", texCoord = new SFNode (), Field.F_EXPOSEDFIELD);
    addField ("height", height = new MFFloat (), Field.F_FIELD);
    addField ("ccw", ccw = new SFBool (true), Field.F_FIELD);
    addField ("colorPerVertex", colorPerVertex = new SFBool (true), Field.F_FIELD);
    addField ("creaseAngle", creaseAngle = new SFFloat (0.0f), Field.F_FIELD);
    addField ("normalPerVertex", normalPerVertex = new SFBool (true), Field.F_FIELD);
    addField ("solid", solid = new SFBool (true), Field.F_FIELD);
    addField ("xDimension", xDimension = new SFInt32 (0), Field.F_FIELD);
    addField ("xSpacing", xSpacing = new SFFloat (0.0f), Field.F_FIELD);
    addField ("zDimension", zDimension = new SFInt32 (0), Field.F_FIELD);
    addField ("zSpacing", zSpacing = new SFFloat (0.0f), Field.F_FIELD);
  }
} // ElevationGrid
