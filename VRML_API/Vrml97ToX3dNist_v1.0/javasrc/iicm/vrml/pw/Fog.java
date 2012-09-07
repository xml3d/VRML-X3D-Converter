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
 * Fog.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19961026
 *
 * changed: krosch, 19961102
 * changed: mpichler, 19970109
 * changed: apesen, 19970410
 *
 * $Id: Fog.java,v 1.3 1997/05/22 09:58:35 apesen Exp $
 */


// Fog

package iicm.vrml.pw;

public class Fog extends Bindable
{
  public SFColor color;
  public SFString fogType;
  public SFFloat visibilityRange;

  public String nodeName ()
  {
    return NodeNames.NODE_FOG;
  }

  public void traverse (Traverser t)
  {
    t.tFog (this);
  }

  Fog ()
  {
    addField ("color", color = new SFColor (1, 1, 1), Field.F_EXPOSEDFIELD);
    addField ("fogType", fogType = new SFString ("LINEAR"), Field.F_EXPOSEDFIELD);
    addField ("visibilityRange", visibilityRange = new SFFloat (0.0f), Field.F_EXPOSEDFIELD);
    addField ("set_bind", new SFBool (false), Field.F_EVENTIN);  // eventIn SFBool set_bind
    addField ("isBound", new SFBool (false), Field.F_EVENTOUT);  // eventOut SFBool isBound
  }

} // Fog
