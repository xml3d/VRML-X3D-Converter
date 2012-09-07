/*
 * Hitpoint - hit point information for picking
 *
 * created: mpichler, 19970505
 *
 * changed: jwolte, 19970509
 * changed: mpichler, 19970708
 * changed: apesen, 19970924
 *
 * $Id: Hitpoint.java,v 1.5 1997/09/25 15:16:41 apesen Exp $
 */


package iicm.utils3d;


/**
 * Hitpoint - hit point information for picking
 * Copyright (c) 1997 IICM
 *
 * @author Michael Pichler
 * @version 1.0, changed:  5 May 97
 */

public class Hitpoint
{
  /**
   * ray hittime. always computed on picking.
   * @see iicm.utils3d.Ray#at
   */
  public float hittime_;
  public Vec3f hitpoint_;  // may be calculated as ray.at (hittime_); not done by default

  public final static int PICK_NORMAL = 0x1;  // calculate normal on picking
  public Vec3f normal_;
  // will add texture coordinates and possibly others here

  // Ray equation in coordinate system of object hit
  public Vec3f raystartobj_;
  public Vec3f raydirobj_;
  public Vec3f normalobj_ = new Vec3f ();

  public float[] hit_trfmat_ = new float[16]; 
  public float[] viewingmat_ = new float[16];

  /**
   * hit point info for picking.
   * @arg flags which information should be calculated and updated out during picking.
   */

  public Hitpoint (int flags)
  {
    // internally, all additional information that is non-null will be set
    if ((flags & PICK_NORMAL) != 0)
      normal_ = new Vec3f ();
  }
} // Hitpoint
