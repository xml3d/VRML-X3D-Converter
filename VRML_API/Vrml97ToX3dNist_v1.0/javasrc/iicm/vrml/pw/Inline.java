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
 * Inline.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960824
 *
 * changed: mpichler, 19960917
 * changed: apesen, 19970410
 *
 * $Id: Inline.java,v 1.3 1997/05/22 10:20:11 apesen Exp $
 */


// Inline

package iicm.vrml.pw;

public class Inline extends GroupNode
{
  // children (GroupNode) filled out after reading
  public MFString url;
  public SFVec3f bboxCenter, bboxSize;

  public String nodeName ()
  {
    return NodeNames.NODE_INLINE;
  }

  public void traverse (Traverser t)
  {
    t.tInline (this);
  }

  Inline ()
  {
    addField ("url", url = new MFString (), Field.F_EXPOSEDFIELD);
    addField ("bboxCenter", bboxCenter = new SFVec3f (0.0f, 0.0f, 0.0f), Field.F_FIELD);
    addField ("bboxSize", bboxSize = new SFVec3f (-1.0f, -1.0f, -1.0f), Field.F_FIELD);
  }
} // Inline
