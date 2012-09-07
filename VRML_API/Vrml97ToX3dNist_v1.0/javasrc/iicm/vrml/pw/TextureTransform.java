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
 * TextureTransform.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19961026
 *
 * changed: krosch, 19961101
 * changed: mpichler, 19970109
 * changed: apesen, 19970411
 *
 * $Id: TextureTransform.java,v 1.3 1997/05/22 15:15:33 apesen Exp $
 */


// TextureTransform

package iicm.vrml.pw;

public class TextureTransform extends AppearNode
{
  public SFVec2f center, scale, translation;
  public SFFloat rotation;

  public String nodeName ()
  {
    return NodeNames.NODE_TEXTURETRANSFORM;
  }

  public void traverse (Traverser t)
  {
    t.tTextureTransform (this);
  }

  TextureTransform ()
  {
    addField ("center", center = new SFVec2f (0.0f, 0.0f), Field.F_EXPOSEDFIELD);
    addField ("rotation", rotation = new SFFloat (0.0f), Field.F_EXPOSEDFIELD);
    addField ("scale", scale = new SFVec2f (1.0f, 1.0f), Field.F_EXPOSEDFIELD);
    addField ("translation", translation = new SFVec2f (0.0f, 0.0f), Field.F_EXPOSEDFIELD);
  }

} // TextureTransform
