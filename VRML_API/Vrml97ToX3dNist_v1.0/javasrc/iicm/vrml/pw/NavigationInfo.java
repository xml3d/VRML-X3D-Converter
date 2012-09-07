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
 * NavigationInfo.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19961024
 *
 * changed: krosch, 19961031
 * changed: mpichler, 19970109
 * changed: apesen, 19970410
 *
 * $Id: NavigationInfo.java,v 1.3 1997/05/22 11:40:10 apesen Exp $
 */


// NavigationInfo

package iicm.vrml.pw; 

public class NavigationInfo extends Bindable
{
  //  eventIn SFBool set_bind;  
  public MFFloat avatarSize;
  public SFBool headlight;
  public SFFloat speed, visibilityLimit;
  public MFString type;
  //  eventOut SFBool isBound;

  final static float[] defAvatarSize = { 0.25f, 1.6f, 0.75f };
  final static String[] defType = { "WALK" };

  public String nodeName ()
  {
    return NodeNames.NODE_NAVIGATIONINFO;
  }

  public void traverse (Traverser t)
  {
    t.tNavigationInfo (this);
  }

  NavigationInfo ()
  {
    addField ("set_bind", new SFBool (false), Field.F_EVENTIN);  // eventIn SFBool set_bind
    addField ("avatarSize", avatarSize = new MFFloat (defAvatarSize), Field.F_EXPOSEDFIELD);
    addField ("headlight", headlight = new SFBool (true), Field.F_EXPOSEDFIELD);
    addField ("speed", speed = new SFFloat (1.0f), Field.F_EXPOSEDFIELD);
    addField ("type", type = new MFString (defType), Field.F_EXPOSEDFIELD);
    addField ("visibilityLimit", visibilityLimit = new SFFloat (0.0f), Field.F_EXPOSEDFIELD);
    addField ("isBound", new SFBool (false), Field.F_EVENTOUT);  // eventOut SFBool isBound
  }

} // NavigationInfo 
