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
 * Sphere.java
 * Copyright (c) 1997 IICM
 *
 * created: mpichler, 19960806
 *
 * changed: mpichler, 19960807
 * changed: apesen, 19970410
 *
 * $Id: Sphere.java,v 1.3 1997/05/22 12:02:44 apesen Exp $
 */


package iicm.vrml.pw;


// Sphere

public class Sphere extends Geometry
{
  public SFFloat radius;

  public String nodeName ()
  {
    return NodeNames.NODE_SPHERE;
  }

  public void traverse (Traverser t)
  {
    t.tSphere (this);
  }

  Sphere ()
  {
    addField ("radius", radius = new SFFloat (1.0f), Field.F_FIELD);
  }
} // Sphere
