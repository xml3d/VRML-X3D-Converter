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
 * Switch.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960824
 * changed: krosch, 19960829
 * changed: mpichler, 19960917
 * changed: apesen, 19970411
 *
 * $Id: Switch.java,v 1.3 1997/05/22 15:11:57 apesen Exp $
 */


// Switch
 
package iicm.vrml.pw; 

public class Switch extends GroupNode
{
  public MFNode choice;
  public SFInt32 whichChoice;

  public String nodeName ()
  {
    return NodeNames.NODE_SWITCH;
  }

  public void traverse (Traverser t)
  {
    t.tSwitch (this);
  }

  Switch ()
  {
    //addField ("choice", choice = new MFNode (), Field.F_EXPOSEDFIELD);  // children
    addField ("choice", children, Field.F_EXPOSEDFIELD); //modified by wang on 04/07/2000
    addField ("whichChoice", whichChoice = new SFInt32 (-1), Field.F_EXPOSEDFIELD);
  }
} // Switch
