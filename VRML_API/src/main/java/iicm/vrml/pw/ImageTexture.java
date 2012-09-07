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
 * ImageTexture.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960824
 *
 * changed: krosch, 19960829
 * changed: apesen, 19970410
 * changed: mpichler, 19970819
 *
 * $Id: ImageTexture.java,v 1.4 1997/08/20 12:04:45 mpichler Exp $
 */


// ImageTexture

package iicm.vrml.pw; 

public class ImageTexture extends Texture
{
  public MFString url;
  public SFBool repeatS, repeatT;

  public String nodeName ()
  {
    return NodeNames.NODE_IMAGETEXTURE;
  }

  public void traverse (Traverser t)
  {
    t.tImageTexture (this);
  }

  ImageTexture ()
  {
    addField ("url", url = new MFString (), Field.F_EXPOSEDFIELD);
    addField ("repeatS", repeatS = new SFBool (true), Field.F_FIELD);
    addField ("repeatT", repeatT = new SFBool (true), Field.F_FIELD);
  }

} // ImageTexture 
