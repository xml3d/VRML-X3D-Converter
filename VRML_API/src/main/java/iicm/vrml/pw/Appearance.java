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
 * Appearance.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960829
 *
 * changed: mpichler, 19961001
 * changed: apesen, 19970409
 *
 * $Id: Appearance.java,v 1.3 1997/05/22 09:27:13 apesen Exp $
 */

 
package iicm.vrml.pw; 

import java.io.*;
import java.util.*;


// Appearance

public class Appearance extends AppearNode
{
  public SFNode material, texture, textureTransform;

  public String nodeName ()
  {
    return NodeNames.NODE_APPEARANCE;
  }

  public void traverse (Traverser t)
  {
    t.tAppearance (this);
  }

  Appearance ()
  {
    addField ("material", material = new SFNode (), Field.F_EXPOSEDFIELD);
    addField ("texture", texture = new SFNode (), Field.F_EXPOSEDFIELD);
    addField ("textureTransform", textureTransform = new SFNode (), Field.F_EXPOSEDFIELD);
  }

  public void writeSubfields (PrintStream os, Hashtable wrefs)
  {
    // ordered output
    writeSubfield ("material", material, os, wrefs);
    writeSubfield ("texture", texture, os, wrefs);
    writeSubfield ("textureTransform", textureTransform, os, wrefs);
  }
} // Appearance
