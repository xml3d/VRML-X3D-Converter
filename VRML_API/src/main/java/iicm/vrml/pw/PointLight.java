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
 * PointLight.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960824
 *
 * changed: krosch, 19960829
 * changed: apesen, 19970410
 * changed: mpichler, 19970819
 *
 * $Id: PointLight.java,v 1.4 1997/08/20 12:03:50 mpichler Exp $
 */


// PointLight
 
package iicm.vrml.pw; 

public class PointLight extends Light
{
  public SFFloat ambientIntensity, intensity, radius;
  public SFVec3f attenuation, location;
  public SFColor color;
  public SFBool on;
  
  public String nodeName ()
  {
    return NodeNames.NODE_POINTLIGHT;
  }
  
  public void traverse (Traverser t)
  {
    t.tPointLight (this);
  }

  PointLight ()
  {
    addField ("ambientIntensity", ambientIntensity = new SFFloat (0.0f), Field.F_EXPOSEDFIELD);
    addField ("attenuation", attenuation = new SFVec3f (1.0f, 0.0f, 0.0f), Field.F_EXPOSEDFIELD);
    addField ("color", color = new SFColor (1.0f, 1.0f, 1.0f), Field.F_EXPOSEDFIELD);
    addField ("intensity", intensity = new SFFloat (1.0f), Field.F_EXPOSEDFIELD);
    addField ("location", location = new SFVec3f (0.0f, 0.0f, 0.0f), Field.F_EXPOSEDFIELD);
    addField ("on", on = new SFBool (true), Field.F_EXPOSEDFIELD);
    addField ("radius", radius = new SFFloat (100.0f), Field.F_EXPOSEDFIELD);
  }
} // PointLight
