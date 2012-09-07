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
 * CylinderSensor.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960824
 *
 * changed: krosch, 19960829
 * changed: apesen, 19970926
 *
 * $Id: CylinderSensor.java,v 1.8 1997/09/29 08:59:09 apesen Exp $
 */


// CylinderSensor

package iicm.vrml.pw; 

import iicm.utils3d.Hitpoint;
import iicm.utils3d.Vec3f;
import iicm.utils3d.Quaternion;
import iicm.utils3d.Mat4f;
import iicm.utils3d.Ray;
import iicm.utils3d.PickUtil;

public class CylinderSensor extends Sensor implements PointerSensor
{
  public SFFloat offset, diskAngle, maxAngle, minAngle;
  public SFBool enabled, autoOffset, isActive;
  public SFRotation  rotation_changed;
  public SFVec3f trackPoint_changed;

  public float oldx_, oldy_;
  public float cylRadius_ = 0.0f;

  public int dragMode_ = 0;
  public final static int CYLINDERMODE = 1;
  public final static int DISKMODE = 2;

  float[] trfmat_;
  // float[] viewmat_;
  public Ray ray_;

  float trackingPlan_ = 0.0f;
  float oldTrackAng_ = 0.0f;
  final static int X = 0;
  final static int Y = 1;
  final static int Z = 2;
  final static int ANG = 3;

  public String nodeName ()
  {
    return NodeNames.NODE_CYLINDERSENSOR;
  }

  public void traverse (Traverser t)
  {
    t.tCylinderSensor (this);
  }

  CylinderSensor ()
  {
    addField ("autoOffset", autoOffset = new SFBool (true), Field.F_EXPOSEDFIELD);
    addField ("diskAngle", diskAngle = new SFFloat (0.262f), Field.F_EXPOSEDFIELD);
    addField ("enabled", enabled = new SFBool (true), Field.F_EXPOSEDFIELD);
    addField ("maxAngle", maxAngle = new SFFloat (-1.0f), Field.F_EXPOSEDFIELD);
    addField ("minAngle", minAngle = new SFFloat (0.0f), Field.F_EXPOSEDFIELD);
    addField ("offset", offset = new SFFloat (0.0f), Field.F_EXPOSEDFIELD);
    addField ("isActive", isActive = new SFBool (false), Field.F_EVENTOUT);  
    addField ("rotation_changed", rotation_changed = new SFRotation (0.0f, 0.0f, 0.0f, 0.0f), Field.F_EVENTOUT);
    addField ("trackPoint_changed", trackPoint_changed = new SFVec3f (0.0f, 0.0f, 0.0f), Field.F_EVENTOUT); 
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
        dragMode_ = 0;
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
        dragMode_ = 0;
        isActive.setValue (false);
        isActive.sendEvent (timestamp);
      }
      return;
    }

    // gets null for hitpoint when no longer hit
    if (hit == null)
      return;

    Vec3f hitoc = new Vec3f ();  // hit point in object coordinates
    float relativRotAng = 0.0f;

    if (dragMode_ == DISKMODE)
    {
      // hitoc.rayat (hit.raystartobj_.value_, hit.hittime_, hit.raydirobj_.value_);
      // transform hit from plane y=0 to plane z=trackPoint[Y]
      float diskhittime = (trackingPlan_ - hit.raystartobj_.value_[Y]) / hit.raydirobj_.value_[Y];
      hitoc.rayat (hit.raystartobj_.value_, diskhittime, hit.raydirobj_.value_);
      relativRotAng = diskrotangle (hitoc.value_[X], -hitoc.value_[Z]) - oldTrackAng_ + offset.getValue (); 
    }
    else
    {
      float[] worldstart = ray_.start_.value_;
      float[] worlddirection = ray_.direction_.value_;
      ray_.start_ = Mat4f.transformPoint3Mat44 (worldstart, trfmat_);
      ray_.direction_ = Mat4f.transformVector3Mat44 (worlddirection, trfmat_);

      if (PickUtil.rayhitscylinderside (ray_, 0.0f, cylRadius_, false, hit, true)) //not 2sided, no hightlimit
      {
        // hit.raystartobj_ = ray_.start_;
        // hit.raydirobj_ = ray_.direction_;
        hitoc.rayat (worldstart, hit.hittime_, worlddirection);
      }
      else
      {
        float[] start = ray_.start_.value_;
        float[] dir = ray_.direction_.value_;
        float a = dir[X] * dir[X] + dir[Z] * dir[Z];
        if (a != 0)  // should be in disk mode when parallel to y
        {
          float b = start[X] * dir[X] + start[Z] * dir[Z];
          float hittime = - b / a;
          hitoc.rayat (worldstart, hittime, worlddirection);
        }
      }
      relativRotAng = (oldx_ - dragx) *-3.1415f + offset.getValue ();;
    }

    float maxAng = maxAngle.getValue ();
    float minAng = minAngle.getValue ();
    if (maxAng >= minAng)
    {
      if (relativRotAng > maxAng)
        relativRotAng = maxAng;
      if (relativRotAng < minAng) 
        relativRotAng = minAng;
    }

    rotation_changed.setValue (0.0f, 1.0f, 0.0f, relativRotAng);
    rotation_changed.sendEvent (timestamp);

    trackPoint_changed.setValue (hitoc.value_[X], hitoc.value_[Y], hitoc.value_[Z]);
    trackPoint_changed.sendEvent (timestamp);
  } // mouseDrag

  /**
   * handle mouse down in interaction mode
   */

  public void mouseDown (float downx, float downy, Hitpoint hit, double timestamp)
  {
    if (!enabled.getValue ())
      return;

    trfmat_ = hit.hit_trfmat_;
    // viewmat_ = hit.viewingmat_;
    oldx_ = downx;
    oldy_ = downy;

    isActive.setValue (true);
    isActive.sendEvent (timestamp);

    Vec3f hitoc = new Vec3f ();  // hit point in object coordinates
    hitoc.rayat (hit.raystartobj_.value_, hit.hittime_, hit.raydirobj_.value_);

    if (dragMode_ == 0)
    {
      float[] raydir = hit.raydirobj_.value_;
      float xxzz = raydir[X] * raydir[X] + raydir[Z] * raydir[Z];
      float alpha = (float) Math.asin (Math.sqrt (xxzz) / Math.sqrt (xxzz + raydir[Y] * raydir[Y]));

      if (alpha < diskAngle.getValue())  // disk treatment
        dragMode_ = CylinderSensor.DISKMODE;
      else // cylinder treatment
        dragMode_ = CylinderSensor.CYLINDERMODE;
    }

    // System.out.println ("DragMode is set to " + dragMode_);

    if (dragMode_ == DISKMODE)
    {
      trackingPlan_ = hitoc.value_[Y];
      oldTrackAng_ = diskrotangle (hitoc.value_[X], -hitoc.value_[Z]);
    }
    else
    {
      float[] yaxis = { 0.0f, 1.0f, 0.0f };
      Quaternion q = new Quaternion (yaxis, Vec3f.getRotationAngle ( hit.raystartobj_ ));
      hitoc.value_ = q.rotateVector (hitoc.value_);
      cylRadius_ = (float) Math.sqrt (hitoc.value_[X]*hitoc.value_[X] + hitoc.value_[Z]*hitoc.value_[Z]);
    }

    float relativRotAng = offset.getValue (); 
    rotation_changed.setValue (0.0f, 1.0f, 0.0f, relativRotAng);
    rotation_changed.sendEvent (timestamp);
    trackPoint_changed.setValue (hitoc.value_[X], hitoc.value_[Y], hitoc.value_[Z]);
    trackPoint_changed.sendEvent (timestamp);
  } // mouseDown 

  /**
   * handle mouse up in interaction mode
   */

  public void mouseUp (double timestamp)
  {
    dragMode_ = 0;
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
      float[] oldRot = rotation_changed.getValue ();
      offset.setValue (oldRot[ANG]);
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
    dragMode_ = 0;
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

  /**
   * diskrotangle calculates the angle of the point (x,z) 
   * + angle = counterclockwise   z+ = up     x+ = right
   * - angle = clockwise          z- = down   x- - left
   */

  private float diskrotangle (float x, float z)
  {
  // System.out.println ("X=" + x + " Z=" + z);
    if (x == 0.0f)
      if (z >= 0)
      {
        return ((float) Math.PI/2);
      }
      else
      {
        return ((float) -Math.PI/2);
      }

    float alpha = (float) Math.atan(z/x);
    if ((alpha > 0) && (x < 0))
    {
      alpha = alpha - (float) Math.PI;
    }
    else if ((alpha <= 0) && (x < 0))
    {
      alpha = alpha + (float) Math.PI;
    }
    // System.out.println ("alpha = " + alpha");
    return (alpha);
  }
} // CylinderSensor
