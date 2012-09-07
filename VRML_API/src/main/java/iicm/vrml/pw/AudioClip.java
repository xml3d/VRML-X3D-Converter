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
 * AudioClip.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960824
 * changed: krosch, 19960829
 * changed: apesen, 19970409
 *
 * $Id: AudioClip.java,v 1.3 1997/05/22 09:29:53 apesen Exp $
 */


// AudioClip
 
package iicm.vrml.pw; 

public class AudioClip extends Common
{
  public SFTime startTime, stopTime;
  public SFFloat pitch;
  public MFString url;
  public SFString description;
  public SFBool loop;
  
  public String nodeName ()
  {
    return NodeNames.NODE_AUDIOCLIP;
  }
  
  public void traverse (Traverser t)
  {
    t.tAudioClip (this);
  }

  AudioClip ()
  {
    addField ("description", description = new SFString (""), Field.F_EXPOSEDFIELD);
    addField ("loop", loop = new SFBool (false), Field.F_EXPOSEDFIELD);
    addField ("pitch", pitch = new SFFloat (1.0f), Field.F_EXPOSEDFIELD);
    addField ("startTime", startTime = new SFTime (0.0), Field.F_EXPOSEDFIELD);
    addField ("stopTime", stopTime = new SFTime (0.0), Field.F_EXPOSEDFIELD);
    addField ("url", url = new MFString (), Field.F_EXPOSEDFIELD);
    addField ("duration_changed", new SFTime (0.0), Field.F_EVENTOUT);  // eventOut SFTime duration_changed
    addField ("isActive", new SFBool (false), Field.F_EVENTOUT);  // eventOut SFBool isActive
  }
} // AudioClip
