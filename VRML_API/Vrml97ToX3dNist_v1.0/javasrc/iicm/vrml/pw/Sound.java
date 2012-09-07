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
 * Sound.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960824
 *
 * changed: krosch, 19960829
 * changed: apesen, 19970410
 *
 * $Id: Sound.java,v 1.3 1997/05/22 12:02:05 apesen Exp $
 */


// Sound
 
package iicm.vrml.pw; 

public class Sound extends Common
{
  public SFVec3f direction, location;
  public SFFloat intensity, maxBack, maxFront, minBack, minFront, priority;
  public SFNode source;
  public SFBool spatialize;
  
  public String nodeName ()
  {
    return NodeNames.NODE_SOUND;
  }
  
  public void traverse (Traverser t)
  {
    t.tSound (this);
  }

  Sound ()
  {
    addField ("direction", direction = new SFVec3f (0.0f, 0.0f, 1.0f), Field.F_EXPOSEDFIELD);
    addField ("intensity", intensity = new SFFloat (1.0f), Field.F_EXPOSEDFIELD);
    addField ("location", location = new SFVec3f (0.0f, 0.0f, 0.0f), Field.F_EXPOSEDFIELD);
    addField ("maxBack", maxBack = new SFFloat (10.0f), Field.F_EXPOSEDFIELD);
    addField ("maxFront", maxFront = new SFFloat (10.0f), Field.F_EXPOSEDFIELD);
    addField ("minBack", minBack = new SFFloat (1.0f), Field.F_EXPOSEDFIELD);
    addField ("minFront", minFront = new SFFloat (1.0f), Field.F_EXPOSEDFIELD);
    addField ("priority", priority = new SFFloat (0.0f), Field.F_EXPOSEDFIELD);
    addField ("source", source = new SFNode (), Field.F_EXPOSEDFIELD);
    addField ("spatialize", spatialize = new SFBool (true), Field.F_FIELD);
  }
} // Sound
