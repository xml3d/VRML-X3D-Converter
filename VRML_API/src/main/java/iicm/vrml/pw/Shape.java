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
 * Shape.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960824
 *
 * changed: krosch, 19960829
 * changed: mpichler, 19961001
 * changed: apesen, 19970410
 *
 * $Id: Shape.java,v 1.3 1997/05/22 12:00:57 apesen Exp $
 */


package iicm.vrml.pw; 

import java.io.*;
import java.util.*;


// Shape

public class Shape extends Common
{
  public SFNode appearance, geometry;

  public String nodeName ()
  {
    return NodeNames.NODE_SHAPE;
  }

  public void traverse (Traverser t)
  {
    t.tShape (this);
  }

  Shape ()
  {
    addField ("appearance", appearance = new SFNode (), Field.F_EXPOSEDFIELD);
    addField ("geometry", geometry = new SFNode (), Field.F_EXPOSEDFIELD);
  }

  public void writeSubfields (PrintStream os, Hashtable wrefs)
  {
    // ordered output
    writeSubfield ("appearance", appearance, os, wrefs);
    writeSubfield ("geometry", geometry, os, wrefs);
  }
} // Shape
