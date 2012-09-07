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
 * Cone.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960811
 *
 * changed: mpichler, 19961018
 * changed: apesen, 19970409
 *
 * $Id: Cone.java,v 1.4 1997/05/22 09:41:11 apesen Exp $
 */


package iicm.vrml.pw; 


// Cone

public class Cone extends Geometry
{
  public SFFloat bottomRadius, height;
  public SFBool bottom, side;

  public String nodeName ()
  {
    return NodeNames.NODE_CONE;
  }

  public void traverse (Traverser t)
  {
    t.tCone (this);
  }

  Cone ()
  {
    addField ("bottomRadius", bottomRadius = new SFFloat (1.0f), Field.F_FIELD);
    addField ("height", height = new SFFloat (2.0f), Field.F_FIELD);
    addField ("bottom", bottom = new SFBool (true), Field.F_FIELD);
    addField ("side", side = new SFBool (true), Field.F_FIELD);
  }
} // Cone
