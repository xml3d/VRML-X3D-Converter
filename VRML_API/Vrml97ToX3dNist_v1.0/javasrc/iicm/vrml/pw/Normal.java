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
 * Normal.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960827
 *
 * changed: krosch, 19960827
 * changed: apesen, 19970410
 *
 * $Id: Normal.java,v 1.3 1997/05/22 11:41:40 apesen Exp $
 */
 

package iicm.vrml.pw; 


// Normal

public class Normal extends Node
{
  public MFVec3f vector;

  public String nodeName ()
  {
    return NodeNames.NODE_NORMAL;
  }

  public void traverse (Traverser t)
  {
    t.tNormal (this);
  }

  Normal ()
  {
    addField ("vector", vector = new MFVec3f (), Field.F_EXPOSEDFIELD);
  }
} // Normal
