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
 * WorldInfo.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960824
 *
 * changed: krosch, 19960829
 * changed: apesen, 19970411
 *
 * $Id: WorldInfo.java,v 1.3 1997/05/22 15:31:25 apesen Exp $
 */


// WorldInfo
 
package iicm.vrml.pw; 

public class WorldInfo extends Common
{
  public MFString info;
  public SFString title;

  public String nodeName ()
  {
    return NodeNames.NODE_WORLDINFO;
  }

  public void traverse (Traverser t)
  {
    t.tWorldInfo (this);
  }

  WorldInfo ()
  {
    addField ("info", info = new MFString (), Field.F_FIELD);
    addField ("title", title = new SFString (""), Field.F_FIELD);
  }
} // WorldInfo
