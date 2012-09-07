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
 * PointerSensor.java
 * interface class for Pointing-Device-Sensor nodes
 * Copyright (c) 1997 IICM
 *
 * created: apesen, 19970808
 *
 * changed: apesen, 19970819
 *
 * $Id: PointerSensor.java,v 1.2 1997/09/25 16:35:56 mpichler Exp $
 */


package iicm.vrml.pw;

import iicm.utils3d.Hitpoint;

/**
 * PointerSensor interface
 */

public interface PointerSensor 
{
  // all mouse coordinates given relative to window size
  // horizontally: 0 = left, 1 = right; vertically: 0 = bottom, 1 = top

  /**
   * handles mouse-move in interaction mode
   */

  public abstract void mouseMove (float downx, float downy, Hitpoint hit, double timestamp);

  /**
   * handles mouse-drag in interaction mode
   */

  public abstract void mouseDrag (float downx, float downy, Hitpoint hit, double timestamp);

  /**
   * handles mouse-button-down in interaction mode
   */

  public abstract void mouseDown (float downx, float downy, Hitpoint hit, double timestamp);

  /**
   * handles mouse-button-up in interaction mode
   */

  public abstract void mouseUp (double timestamp);

  /**
   * clears sensor, sends isOver false
   */

  public abstract void mouseExit (double timestamp);

}
