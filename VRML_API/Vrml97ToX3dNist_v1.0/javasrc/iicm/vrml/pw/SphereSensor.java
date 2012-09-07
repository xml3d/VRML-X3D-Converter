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
 * SphereSensor.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960824
 *
 * changed: krosch, 19960829
 * changed: apesen, 19970926
 *
 * $Id: SphereSensor.java,v 1.6 1997/09/29 08:17:57 apesen Exp $
 */


// SphereSensor
 
package iicm.vrml.pw; 

import iicm.utils3d.Hitpoint;
import iicm.utils3d.Vec3f;
import iicm.utils3d.Quaternion;
import iicm.utils3d.Mat4f;
import iicm.utils3d.Ray;
import iicm.utils3d.PickUtil;

public class SphereSensor extends Sensor implements PointerSensor
{
  public SFBool autoOffset, enabled, isActive;
  public SFRotation offset, rotation_changed;
  public SFVec3f trackPoint_changed;

  public float sphRadius_ = 0.0f;
  public float oldx_, oldy_;

  public float[] rotate = new float [16];

  float[] xaxis_ = { 1.0f, 0.0f, 0.0f };
  float[] yaxis_ = { 0.0f, 1.0f, 0.0f };

  float[] trfmat_;
  // float[] viewmat_;
  public Ray ray_;

  final static int X = 0;
  final static int Y = 1;
  final static int Z = 2;
  final static int ANG = 3;

  public String nodeName ()
  {
    return NodeNames.NODE_SPHERESENSOR;
  }
  
  public void traverse (Traverser t)
  {
    t.tSphereSensor (this);
  }


  SphereSensor ()
  {
    addField ("autoOffset", autoOffset = new SFBool (true), Field.F_EXPOSEDFIELD);
    addField ("enabled", enabled = new SFBool (true), Field.F_EXPOSEDFIELD);
    addField ("offset", offset = new SFRotation (0.0f, 1.0f, 0.0f, 0.0f), Field.F_EXPOSEDFIELD);
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
    float[] worldstart = ray_.start_.value_;
    float[] worlddirection = ray_.direction_.value_;
    ray_.start_ = Mat4f.transformPoint3Mat44 (worldstart, trfmat_);
    ray_.direction_ = Mat4f.transformVector3Mat44 (worlddirection, trfmat_);

    if (PickUtil.rayhitssphere (ray_, sphRadius_, false, hit))
    {
      // hit.raystartobj_ = ray_.start_;
      // hit.raydirobj_ = ray_.direction_;
      hitoc.rayat (worldstart, hit.hittime_, worlddirection);
    }
    else
    {
      float[] start = ray_.start_.value_;
      float hittime = (float) Math.sqrt (Vec3f.dot (start, start) - sphRadius_ * sphRadius_);
      hitoc.rayat (worldstart, hittime, worlddirection);
    }

    float relativXRotAng = (oldx_ - dragx) *-3.1415f;
    float relativYRotAng = (oldy_ - dragy) * 1.6707f;

    float[] offs = offset.getValue ();
    Quaternion q = new Quaternion (offs, offs[ANG]);
    q.multiply (new Quaternion (xaxis_, relativYRotAng));
    q.multiply (new Quaternion (yaxis_, relativXRotAng));
    q.renormalize ();  // prevent accumulation of rounding errors
    float[] rotation = q.getAxisAngle ();
    rotation_changed.setValue (rotation[X], rotation[Y], rotation[Z], rotation[ANG]);
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
    float[] trfviewmat = Mat4f.multiplyMat43in44 (hit.hit_trfmat_, hit.viewingmat_);

    float[] inv = new float[9];
    if (Mat4f.invertMatrix33of44 (trfviewmat, inv))  // non-singular viewing matrix
    {
      // rows of inverse transformation matrix tell camera coordinates
      // in terms of the sensor's local coordinate system
      xaxis_[X] = inv[0];  xaxis_[Y] = inv[1];  xaxis_[Z] = inv[2];
      yaxis_[X] = inv[3];  yaxis_[Y] = inv[4];  yaxis_[Z] = inv[5];
    }

    oldx_ = downx;
    oldy_ = downy;

    isActive.setValue (true);
    isActive.sendEvent (timestamp);

    Vec3f hitoc = new Vec3f ();  // hit point in object coordinates
    hitoc.rayat (hit.raystartobj_.value_, hit.hittime_, hit.raydirobj_.value_);

    sphRadius_ = (float) Math.sqrt (hitoc.value_[X]*hitoc.value_[X] + hitoc.value_[Y]*hitoc.value_[Y] 
                                  + hitoc.value_[Z]*hitoc.value_[Z]);

    rotation_changed.copyValue (offset);
    rotation_changed.sendEvent (timestamp);

    trackPoint_changed.setValue (hitoc.value_[X], hitoc.value_[Y], hitoc.value_[Z]);
    trackPoint_changed.sendEvent (timestamp);
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
      offset.copyValue (rotation_changed);
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
} // SphereSensor
