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
 * TextureCoordinate.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960827
 *
 * changed: krosch, 19960827
 * changed: apesen, 19970411
 *
 * $Id: TextureCoordinate.java,v 1.3 1997/05/22 15:14:36 apesen Exp $
 */
 

package iicm.vrml.pw; 


// TextureCoordinate

public class TextureCoordinate extends Node
{
  public MFVec2f point;

  public String nodeName ()
  {
    return NodeNames.NODE_TEXTURECOORDINATE;
  }

  public void traverse (Traverser t)
  {
    t.tTextureCoordinate (this);
  }

  TextureCoordinate ()
  {
    addField ("point", point = new MFVec2f (), Field.F_EXPOSEDFIELD);
  }
} // TextureCoordinate
