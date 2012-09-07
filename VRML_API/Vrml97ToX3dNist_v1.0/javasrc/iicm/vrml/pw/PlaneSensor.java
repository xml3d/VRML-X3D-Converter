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
 * PlaneSensor.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960824
 *
 * changed: mpichler, 19960920
 * changed: apesen, 19970826
 *
 * $Id: PlaneSensor.java,v 1.4 1997/09/15 13:22:49 apesen Exp $
 */


// PlaneSensor
 
package iicm.vrml.pw; 

import iicm.utils3d.Hitpoint;
import iicm.utils3d.Vec3f;

public class PlaneSensor extends Sensor implements PointerSensor
{
  public SFBool autoOffset, enabled, isActive;
  public SFVec2f maxPosition, minPosition;
  public SFVec3f offset, translation_changed, trackPoint_changed;
  float[] oldTrackPos = new float[3];
  static final int X = 0;
  static final int Y = 1;
  static final int Z = 2;
  public Hitpoint hitobj_;

  public String nodeName ()
  {
    return NodeNames.NODE_PLANESENSOR;
  }

  public void traverse (Traverser t)
  {
    t.tPlaneSensor (this);
  }

  PlaneSensor ()
  {
    addField ("autoOffset", autoOffset = new SFBool (true), Field.F_EXPOSEDFIELD);
    addField ("enabled", enabled = new SFBool (true), Field.F_EXPOSEDFIELD);
    addField ("maxPosition", maxPosition = new SFVec2f (-1.0f, -1.0f), Field.F_EXPOSEDFIELD);
    addField ("minPosition", minPosition = new SFVec2f (0.0f, 0.0f), Field.F_EXPOSEDFIELD);
    addField ("offset", offset = new SFVec3f (0.0f, 0.0f, 0.0f), Field.F_EXPOSEDFIELD);
    addField ("isActive", isActive = new SFBool (false), Field.F_EVENTOUT);
    addField ("trackPoint_changed", trackPoint_changed = new SFVec3f (0.0f, 0.0f, 0.0f), Field.F_EVENTOUT);  
    addField ("translation_changed", translation_changed = new SFVec3f (0.0f, 0.0f, 0.0f), Field.F_EVENTOUT); 
  }
  /**
   * handle mouse move in interaction mode
   */

  public void mouseMove (float downx, float downy, Hitpoint hit, double timestamp)
  {
    if (!enabled.getValue ())
    {
      if (isActive.getValue ())
      {
        isActive.setValue (false);
        isActive.sendEvent (timestamp);
      }
      return;
    }
  } // mouseMove

  /**
   * handle mouse drag in interaction mode
   */

  public void mouseDrag (float dragx, float dragy, Hitpoint hit, double timestamp)
  {
    if (!enabled.getValue ())
    {
      if (isActive.getValue ())
      {
        isActive.setValue (false);
        isActive.sendEvent (timestamp);
      }
      return;
    }

    // gets null for hitpoint when no longer hit
    if (hit == null)
      return;

    Vec3f hitoc = new Vec3f ();  // hit point in object coordinates
    hitoc.rayat (hit.raystartobj_.value_, hit.hittime_, hit.raydirobj_.value_);

    float[] maxPos = maxPosition.getValue ();
    float[] minPos = minPosition.getValue ();
    float[] offSet = offset.getValue ();
    
    float relativMoveX = hitoc.value_[X] - oldTrackPos[X] + offSet[X];
    float relativMoveY = hitoc.value_[Y] - oldTrackPos[Y] + offSet[Y];

    if (maxPos[X] >= minPos[X])
    {
      if (relativMoveX > maxPos[X])
        relativMoveX = maxPos[X];
      if (relativMoveX < minPos[X]) 
        relativMoveX = minPos[X];
    }
    if (maxPos[Y] >= minPos[Y])
    {
      if (relativMoveY > maxPos[Y])
        relativMoveY = maxPos[Y];
      if (relativMoveY < minPos[Y])
        relativMoveY = minPos[Y];
    }

    translation_changed.setValue (relativMoveX, relativMoveY, offSet[Z]);
    translation_changed.sendEvent (timestamp);

    trackPoint_changed.setValue (hitoc.value_[X], hitoc.value_[Y], hitoc.value_[Z]);
    trackPoint_changed.sendEvent (timestamp);

    // System.err.println ("PLAIN hitpoint WORLD: " + hit.hitpoint_);
    // System.err.println ("PLAIN hitpoint OBJ  : " + hitoc + " thit: " + hit.hittime_);
  } // mouseDrag

  /**
   * handle mouse down in interaction mode
   */

  public void mouseDown (float downx, float downy, Hitpoint hit, double timestamp)
  {
    if (!enabled.getValue ())
      return;

    hitobj_ = hit;

    isActive.setValue (true);
    isActive.sendEvent (timestamp);

    Vec3f hitoc = new Vec3f ();  // hit point in object coordinates
    hitoc.rayat (hit.raystartobj_.value_, hit.hittime_, hit.raydirobj_.value_);
    trackPoint_changed.setValue (hitoc.value_[X], hitoc.value_[Y], hitoc.value_[Z]);
    trackPoint_changed.sendEvent (timestamp);

    float[] tp = trackPoint_changed.getValue ();
    oldTrackPos[X] = tp[X];
    oldTrackPos[Y] = tp[Y];
    oldTrackPos[Z] = tp[Z];

    float[] offSet = offset.getValue ();
    float relativMoveX = hitoc.value_[X] - oldTrackPos[X] + offSet[X];
    float relativMoveY = hitoc.value_[Y] - oldTrackPos[Y] + offSet[Y];
    translation_changed.setValue (relativMoveX, relativMoveY, offSet[Z]);
    translation_changed.sendEvent (timestamp);
  } // mouseDown 

  /**
   * handle mouse up in interaction mode
   */

  public void mouseUp (double timestamp)
  {
    if (!enabled.getValue ())
    {
      if (isActive.getValue ())
      {
        isActive.setValue (false);
        isActive.sendEvent (timestamp);
      }
      return;
    }

    if (autoOffset.getValue ())
    {
      float[] oldPos = translation_changed.getValue ();
      offset.setValue (oldPos[X], oldPos[Y], oldPos[Z]);
      offset.sendEvent (timestamp);
    }

    isActive.setValue (false);
    isActive.sendEvent (timestamp);

  } // mouseUp

  /**
   * handle mouse exit
   */

  public void mouseExit (double timestamp)
  {
    if (!enabled.getValue ())
    {
      if (isActive.getValue ())
      {
        isActive.setValue (false);
        isActive.sendEvent (timestamp);
      }
      return;
    }
  } // mouseExit
} // PlaneSensor

