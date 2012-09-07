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
 * Extrusion.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960811
 *
 * changed: krosch, 19960812
 * changed: mpichler, 19961001
 * changed: apesen, 19970409
 *
 * $Id: Extrusion.java,v 1.3 1997/05/22 09:55:20 apesen Exp $
 */

 
package iicm.vrml.pw; 

import java.io.*;
import java.util.*;


// Extrusion

public class Extrusion extends Geometry
{
  public SFBool beginCap, ccw, convex, endCap, solid;
  public SFFloat creaseAngle;
  public MFVec2f crossSection, scale;
  public MFVec3f spine;
  public MFRotation orientation;

  public final static float[] defCrossSection =
  { 1.0f, 1.0f,  1.0f, -1.0f,  -1.0f, -1.0f,  -1.0f, 1.0f,  1.0f, 1.0f };
  public final static float[] defScale = { 1.0f, 1.0f };
  public final static float[] defSpine = { 0.0f, 0.0f, 0.0f,  0.0f, 1.0f, 0.0f };
  public final static float[] defOrientation = { 0.0f, 0.0f, 1.0f, 0.0f };

  public String nodeName ()
  {
    return NodeNames.NODE_EXTRUSION;
  }

  public void traverse (Traverser t)
  {
    t.tExtrusion (this);
  }

  Extrusion ()
  {
    addField ("set_crossSection", new MFVec2f (), Field.F_EVENTIN);    // eventIn set_crossSection
    addField ("set_orientation", new MFRotation (), Field.F_EVENTIN);  // eventIn set_orientation
    addField ("set_scale", new MFVec2f (), Field.F_EVENTIN);           // eventIn set_scale
    addField ("set_spine", new MFVec3f (), Field.F_EVENTIN);           // eventIn set_spine
    addField ("beginCap", beginCap = new SFBool (true), Field.F_FIELD); 
    addField ("ccw", ccw = new SFBool (true), Field.F_FIELD);
    addField ("convex", convex = new SFBool (true), Field.F_FIELD);
    addField ("endCap", endCap = new SFBool (true), Field.F_FIELD);
    addField ("solid", solid = new SFBool (true), Field.F_FIELD);
    addField ("creaseAngle", creaseAngle = new SFFloat (0.0f), Field.F_FIELD);
    addField ("crossSection", crossSection = new MFVec2f (defCrossSection), Field.F_FIELD);
    addField ("scale", scale = new MFVec2f (defScale), Field.F_FIELD);
    addField ("spine", spine = new MFVec3f (defSpine), Field.F_FIELD);
    addField ("orientation", orientation = new MFRotation (defOrientation), Field.F_FIELD);
  }

  public void writeSubfields (PrintStream os, Hashtable wrefs)
  {
    // ordered output
    writeSubfield ("beginCap", beginCap, os, wrefs);
    writeSubfield ("ccw", ccw, os, wrefs);
    writeSubfield ("convex", convex, os, wrefs);
    writeSubfield ("creaseAngle", creaseAngle, os, wrefs);
    writeSubfield ("crossSection", crossSection, os, wrefs);
    writeSubfield ("endCap", endCap, os, wrefs);
    writeSubfield ("orientation", orientation, os, wrefs);
    writeSubfield ("scale", scale, os, wrefs);
    writeSubfield ("solid", solid, os, wrefs);
    writeSubfield ("spine", spine, os, wrefs);
  }
} // Extrusion
