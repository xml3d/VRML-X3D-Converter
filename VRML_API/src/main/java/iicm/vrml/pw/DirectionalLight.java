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
 * DirectionalLight.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960824
 *
 * changed: krosch, 19960829
 * changed: apesen, 19970409
 * changed: mpichler, 19970819
 *
 * $Id: DirectionalLight.java,v 1.5 1997/08/20 12:03:36 mpichler Exp $
 */


// DirectionalLight
 
package iicm.vrml.pw; 

public class DirectionalLight extends Light
{
  public SFFloat ambientIntensity, intensity;
  public SFColor color;
  public SFVec3f direction;  // parallels light direction
  public SFBool on;
  
  public String nodeName ()
  {
    return NodeNames.NODE_DIRECTIONALLIGHT;
  }
  
  public void traverse (Traverser t)
  {
    t.tDirectionalLight (this);
  }

  DirectionalLight ()
  {
    addField ("ambientIntensity", ambientIntensity = new SFFloat (0.0f), Field.F_EXPOSEDFIELD);
    addField ("color", color = new SFColor (1.0f, 1.0f, 1.0f), Field.F_EXPOSEDFIELD);
    addField ("direction", direction = new SFVec3f (0.0f, 0.0f, -1.0f), Field.F_EXPOSEDFIELD);
    addField ("intensity", intensity = new SFFloat (1.0f), Field.F_EXPOSEDFIELD);
    addField ("on", on = new SFBool (true), Field.F_EXPOSEDFIELD);
  }
} // DirectionalLight
