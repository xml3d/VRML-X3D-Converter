/*
 * Ray - definition of a ray for picking purposes
 *
 * created: mpichler, 19970505
 * changed: mpichler, 19970505
 *
 * $Id: Ray.java,v 1.4 1997/07/25 11:41:15 mpichler Exp $
 */


package iicm.utils3d;


/**
 * Ray - definition of a ray for picking purposes.
 * Copyright (c) 1997 IICM
 *
 * @author Michael Pichler
 * @version 1.0, changed:  5 May 97
 */

public class Ray
{
  public Vec3f start_;
  public Vec3f direction_;
  public float near_;
  public float far_;

  /**
   * create a ray, given by start point, direction vector, and near/far picking ranges
   */

  public Ray (float[/*3*/] start, float[/*3*/] direction, float near, float far)
  {
    start_ = new Vec3f (start);
    direction_ = new Vec3f (direction);
    near_ = near;
    far_ = far;
  }

  /**
   * create a ray (as above). Vec3f references taken over by Ray class
   */

  public Ray (Vec3f start, Vec3f direction, float near, float far)
  {
    start_ = start;
    direction_ = direction;
    near_ = near;
    far_ = far;
  }

  /**
   * create a ray, given by start coordinates, direction, and near/far ranges
   */

  public Ray (float sx, float sy, float sz, float dx, float dy, float dz, float near, float far)
  {
    start_ = new Vec3f (sx, sy, sz);
    direction_ = new Vec3f (dx, dy, dz);
    near_ = near;
    far_ = far;
  }

  /**
   * tell where the ray is at a specific value of t.
   * ray equation: start + t * direction
   * @see Vec3f#rayat
   */

  public Vec3f at (float t)
  {
    float[] a = start_.value_;
    float[] b = direction_.value_;
    return new Vec3f (a[0] + t * b[0], a[1] + t * b[1], a[2] + t * b[2]);
  }

  /**
   * string rep (for debugging)
   */

  public String toString ()
  {
    return ("ray " + start_ + " dir " + direction_ + " range [" + near_ + ", " + far_ + "]");
  }

} // Ray
