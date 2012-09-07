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
 * Sensor.java
 * base class for Sensor nodes
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960829
 * changed: apesen, 19970804
 *
 * $Id: Sensor.java,v 1.4 1997/08/04 10:03:11 apesen Exp $
 */


package iicm.vrml.pw;


/**
 * Sensor base class
 */

abstract public class Sensor extends Node
{
  /**
   * evaluate Sensor at frame time
   */

  public void evaluate (double timestamp)
  {
  }
}
