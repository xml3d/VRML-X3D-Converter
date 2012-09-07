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
 * Background.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960824
 * changed: krosch, 19960829
 * changed: mpichler, 19961001
 * changed: apesen, 19970409
 *
 * $Id: Background.java,v 1.3 1997/05/22 09:33:09 apesen Exp $
 */


// Background
 
package iicm.vrml.pw; 

public class Background extends Bindable
{
  public MFFloat groundAngle, skyAngle;
  public MFColor groundColor, skyColor;
  public MFString backUrl, bottomUrl, frontUrl, leftUrl, rightUrl, topUrl;

  final static float[] defSkyColor = { 0.0f, 0.0f, 0.0f };

  public String nodeName ()
  {
    return NodeNames.NODE_BACKGROUND;
  }

  public void traverse (Traverser t)
  {
    t.tBackground (this);
  }

  Background ()
  {
    addField ("set_bind", new SFBool (false), Field.F_EVENTIN);  // eventIn SFBool set_bind
    addField ("groundAngle", groundAngle = new MFFloat (), Field.F_EXPOSEDFIELD);
    addField ("groundColor", groundColor = new MFColor (), Field.F_EXPOSEDFIELD);
    addField ("backUrl", backUrl = new MFString (), Field.F_EXPOSEDFIELD);
    addField ("bottomUrl", bottomUrl = new MFString (), Field.F_EXPOSEDFIELD);
    addField ("frontUrl", frontUrl = new MFString (), Field.F_EXPOSEDFIELD);
    addField ("leftUrl", leftUrl = new MFString (), Field.F_EXPOSEDFIELD);
    addField ("rightUrl", rightUrl = new MFString (), Field.F_EXPOSEDFIELD);
    addField ("topUrl", topUrl = new MFString (), Field.F_EXPOSEDFIELD);
    addField ("skyAngle", skyAngle = new MFFloat (), Field.F_EXPOSEDFIELD);
    addField ("skyColor", skyColor = new MFColor (defSkyColor), Field.F_EXPOSEDFIELD);
    addField ("isBound", new SFBool (false), Field.F_EVENTOUT);  // eventOut SFBool isBound
  }
} // Background
