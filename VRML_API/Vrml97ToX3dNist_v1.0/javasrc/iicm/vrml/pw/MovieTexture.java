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
 * MovieTexture.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19961029
 *
 * changed: krosch, 19961102
 * changed: apesen, 19970410
 * changed: mpichler, 19970819
 *
 * $Id: MovieTexture.java,v 1.4 1997/08/20 12:04:45 mpichler Exp $
 */


// MovieTexture

package iicm.vrml.pw; 

public class MovieTexture extends Texture
{
  public SFBool loop, repeatS, repeatT;
  public SFFloat speed;
  public SFTime startTime, stopTime;
  public MFString url;
//  eventOut SFFloat duration_changed; 
//  eventOut SFBool isActive;

  public String nodeName ()
  {
    return NodeNames.NODE_MOVIETEXTURE;
  }

  public void traverse (Traverser t)
  {
    t.tMovieTexture (this);
  }

  MovieTexture ()
  {
    addField ("loop", loop = new SFBool (false), Field.F_EXPOSEDFIELD);
    addField ("speed", speed = new SFFloat (1.0f), Field.F_EXPOSEDFIELD);
    addField ("startTime", startTime = new SFTime (0), Field.F_EXPOSEDFIELD);
    addField ("stopTime", stopTime = new SFTime (0), Field.F_EXPOSEDFIELD);
    addField ("url", url = new MFString (), Field.F_EXPOSEDFIELD); 
    addField ("repeatS", repeatS = new SFBool (true), Field.F_FIELD);
    addField ("repeatT", repeatT = new SFBool (true), Field.F_FIELD);
    addField ("duration_changed", new SFFloat (0.0f), Field.F_EVENTOUT);  // eventOut SFFloat duration_changed
    addField ("isActive", new SFBool (false), Field.F_EVENTOUT);          // eventOut SFBool isActice 
  }

} // MovieTexture 
