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
 * Cylinder.java
 * Copyright (c) 1997 IICM
 *
 * created: mpichler, 19960807
 *
 * changed: mpichler, 19970114
 * changed: apesen, 19970409
 *
 * $Id: Cylinder.java,v 1.4 1997/05/22 09:45:47 apesen Exp $
 */


package iicm.vrml.pw;


// Cylinder

public class Cylinder extends Geometry
{
  public SFBool bottom, side, top;
  public SFFloat height, radius;

  public String nodeName ()
  {
    return NodeNames.NODE_CYLINDER;
  }

  public void traverse (Traverser t)
  {
    t.tCylinder (this);
  }

  Cylinder ()
  {
    addField ("bottom", bottom = new SFBool (true), Field.F_FIELD);
    addField ("height", height = new SFFloat (2.0f), Field.F_FIELD);
    addField ("radius", radius = new SFFloat (1.0f), Field.F_FIELD);
    addField ("side",   side = new SFBool (true), Field.F_FIELD);
    addField ("top",    top = new SFBool (true), Field.F_FIELD);
  }
} // Cylinder
