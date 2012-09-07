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
 * SpotLight.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960824
 *
 * changed: krosch, 19960829
 * changed: apesen, 19970411
 * changed: mpichler, 19970819
 *
 * $Id: SpotLight.java,v 1.4 1997/08/20 12:04:00 mpichler Exp $
 */


// SpotLight
 
package iicm.vrml.pw; 

public class SpotLight extends Light
{
  public SFFloat ambientIntensity, beamWidth, cutOffAngle, intensity, radius;
  public SFVec3f attenuation, direction, location;
  public SFColor color;
  public SFBool on;
  
  public String nodeName ()
  {
    return NodeNames.NODE_SPOTLIGHT;
  }
  
  public void traverse (Traverser t)
  {
    t.tSpotLight (this);
  }

  SpotLight ()
  {
    addField ("ambientIntensity", ambientIntensity = new SFFloat (0.0f), Field.F_EXPOSEDFIELD);
    addField ("attenuation", attenuation = new SFVec3f (1.0f, 0.0f, 0.0f), Field.F_EXPOSEDFIELD);
    addField ("beamWidth", beamWidth = new SFFloat (1.570796f), Field.F_EXPOSEDFIELD);
    addField ("color", color = new SFColor (1.0f, 1.0f, 1.0f), Field.F_EXPOSEDFIELD);
    addField ("cutOffAngle", cutOffAngle = new SFFloat (0.785398f), Field.F_EXPOSEDFIELD);
    addField ("direction", direction = new SFVec3f (0.0f, 0.0f, -1.0f), Field.F_EXPOSEDFIELD);
    addField ("intensity", intensity = new SFFloat (1.0f), Field.F_EXPOSEDFIELD);
    addField ("location", location = new SFVec3f (0.0f, 0.0f, 0.0f), Field.F_EXPOSEDFIELD);
    addField ("on", on = new SFBool (true), Field.F_EXPOSEDFIELD);
    addField ("radius", radius = new SFFloat (100.0f), Field.F_EXPOSEDFIELD);
  }
} // SpotLight
