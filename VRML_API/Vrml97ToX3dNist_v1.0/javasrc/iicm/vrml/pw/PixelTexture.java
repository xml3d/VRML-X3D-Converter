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
 * PixelTexture.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19961026
 *
 * changed: krosch, 19961101
 * changed: apesen, 19970410
 * changed: mpichler, 19970819
 *
 * $Id: PixelTexture.java,v 1.4 1997/08/20 12:04:45 mpichler Exp $
 */


// PixelTexture

package iicm.vrml.pw; 

public class PixelTexture extends Texture
{
  public SFImage image;
  public SFBool repeatS, repeatT;

  public String nodeName ()
  {
    return NodeNames.NODE_PIXELTEXTURE;
  }

  public void traverse (Traverser t)
  {
    t.tPixelTexture (this);
  }

  PixelTexture ()
  {
    addField ("image", image = new SFImage (), Field.F_EXPOSEDFIELD);
    addField ("repeatS", repeatS = new SFBool (true), Field.F_FIELD);
    addField ("repeatT", repeatT = new SFBool (true), Field.F_FIELD);
  }

} // PixelTexture
