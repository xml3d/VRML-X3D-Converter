/*
 * Vec3f - small utilities for 3D vectors
 *
 * created: mpichler, 19970505
 *
 * changed: kwagen, 19970709
 * changed: mpichler, 19970724
 *
 * $Id: Vec3f.java,v 1.8 1997/09/23 12:52:45 apesen Exp $
 */


package iicm.utils3d;


/**
 * Vec3f - small utilities for 3D vectors.
 * Copyright (c) 1997 IICM
 *
 * @author Michael Pichler
 * @version 1.0, changed:  5 May 97
 */


public final class Vec3f
{
  /**
   * 3D vector of values
   */
  public float[] value_ = new float [3];
  // thus compatible with native code calls

  // array indices
  public static final int X = 0;
  public static final int Y = 1;
  public static final int Z = 2;

  /**
   * constructor (0, 0, 0)
   */
  public Vec3f ()
  { // initialized by new statement
  }

  /**
   * constructor (x, y, z)
   */
  public Vec3f (float x, float y, float z)
  {
    // value_ = { x, y, z };  // illegal syntax
    float[] v = value_;
    v[0] = x;  v[1] = y;  v[2] = z;
  }

  /**
   * constructor (a[0], a[1], a[2]). values are copied into internal array.
   */
  public Vec3f (float[/*3*/] a)
  {
    float[] v = value_;
    v[0] = a[0];  v[1] = a[1];  v[2] = a[2];
  }

  /**
   * assignment (x, y, z)
   */
  public void assign (float x, float y, float z)
  {
    float[] v = value_;
    v[0] = x;  v[1] = y;  v[2] = z;
  }

  /**
   * assignment (a[0], a[1], a[2]). values are copied into internal array.
   */
  public void assign (float[/*3*/] a)
  {
    float[] v = value_;
    v[0] = a[0];  v[1] = a[1];  v[2] = a[2];
  }

  /**
   * assignment (copying, v = b)
   */
  public void assign (Vec3f b)
  {
    float[] v = value_;
    float[] a = b.value_;
    v[0] = a[0];  v[1] = a[1];  v[2] = a[2];
  }

  /**
   * negate (v = -v)
   */
  public void negate ()
  {
    float[] v = value_;
    v[0] = - v[0];
    v[1] = - v[1];
    v[2] = - v[2];
  }

  /**
   * increase (v += b)
   */
  public void increase (Vec3f b)
  {
    float[] v = value_;
    float[] w = b.value_;
    v[0] += w[0];  v[1] += w[1];  v[2] += w[2];
  }

  public void increase (float[] w)
  {
    float[] v = value_;
    v[0] += w[0];  v[1] += w[1];  v[2] += w[2];
  }

  /**
   * decrease (v -= b)
   */
  public void decrease (Vec3f b)
  {
    float[] v = value_;
    float[] w = b.value_;
    v[0] -= w[0];  v[1] -= w[1];  v[2] -= w[2];
  }

  public void decrease (float[] w)
  {
    float[] v = value_;
    v[0] -= w[0];  v[1] -= w[1];  v[2] -= w[2];
  }

  /**
   * sincrease (v += f * b). increase by a scaled vector
   */
  public void sincrease (float f, Vec3f b)
  {
    float[] v = value_;
    float[] w = b.value_;
    v[0] += f * w[0];  v[1] += f * w[1];  v[2] += f * w[2];
  }

  public void sincrease (float f, float[] w)
  {
    float[] v = value_;
    v[0] += f * w[0];  v[1] += f * w[1];  v[2] += f * w[2];
  }

  /**
   * ray equation (a + t * b)
   */
  public void rayat (float[] a, float t, float[] b)
  {
    float[] v = value_;
    v[0] = a[0] + t * b[0];
    v[1] = a[1] + t * b[1];
    v[2] = a[2] + t * b[2];
  }

  /**
   * scale by a scalar
   */
  public void scale (float f)
  {
    float[] v = value_;
    v[0] *= f;  v[1] *= f;  v[2] *= f;
  }

  public static void scale (float[/*3*/] v, float f)
  {
    v[0] *= f;  v[1] *= f;  v[2] *= f;
  }

  /**
   * dot product. &lt; a . b &gt; E.g. dot (a, a) is the square norm of a
   */
  public static float dot (Vec3f a, Vec3f b)
  {
    float[] u = a.value_;
    float[] v = b.value_;
    return u[0] * v[0] + u[1] * v[1] + u[2] * v[2];
  }

  public static float dot (float[] u, float[] v)
  {
    return u[0] * v[0] + u[1] * v[1] + u[2] * v[2];
  }

  /**
   * assign cross product a X b to this vector.
   * do not call with "this" as either argument.
   */
  public void cross (Vec3f a, Vec3f b)
  {
    cross (a.value_, b.value_);
  }

  public void cross (float[] u, float[] v)
  {
    value_[0] = u[1] * v[2] - u[2] * v[1];
    value_[1] = u[2] * v[0] - u[0] * v[2];
    value_[2] = u[0] * v[1] - u[1] * v[0];
  }

  /**
   * normalize. return old length
   */
  public float normalize ()
  {
    float length = (float) Math.sqrt (dot (this, this));
    if (length > 0.0f)
      scale (1.0f / length);
    return length;
  }

  /**
   * find a vector that is orthogonal to non-zero vector v.
   * write result (normalized) into axis
   */
  public static void getOrthogonalVector (float[] v, float[] axis)
  {
    // choose vector which comes close to (0, 1, 0)
    float x = v[0];
    float y = v[1];
    float z = v[2];

    if (Math.abs (x) > Math.abs (z))  // (-y, x, 0) or (y, -x, 0)
    {
      if (x > 0.0f)
      { axis[0] = -y;  axis[1] = x;  axis[2] = 0;
      }
      else
      { axis[0] = y;  axis[1] = -x;  axis[2] = 0;
      }
    }
    else  // (0, z, -y) or (0, -z, y)
    {
      if (z > 0.0f)
      { axis[0] = 0;  axis[1] = z;  axis[2] = -y;
      }
      else
      { axis[0] = 0;  axis[1] = -z;  axis[2] = y;
      }
    }
    float length = (float) Math.sqrt (dot (axis, axis));
    if (length > 0.0f)
      scale (axis, 1.0f / length);
    else
      System.err.println ("getOrthogonalVector. internal error on vector " + print (v));
      // might return (1, 0, 0) in this case

  } // getOrthogonalVector

  /**
   * get the angle which the xy plane must be rotatated about the
   * Y-axis (unchanged) such that the new normal vector (Z-axis)
   * points towards start
   * @return angle off rotation
   */

  public static float getRotationAngle (Vec3f start)
  {
    Vec3f x_axis = new Vec3f ();
    Vec3f y_axis = new Vec3f (0.0f, 1.0f, 0.0f);  // Y axis in local coordinatesystem
    Vec3f z_axis = new Vec3f ();
    x_axis.cross (y_axis, start);  // finde new X axis normal to plan through Y axis and start
    z_axis.cross (x_axis, y_axis);

    x_axis.normalize ();
    z_axis.normalize ();

    Vec3f p = new Vec3f (x_axis.value_[2], y_axis.value_[2], z_axis.value_[2]);

    double alpha = Math.acos (p.value_[2]);
    if (p.value_[0] < 0)
      alpha = -alpha;

    return ((float) alpha);
  } // getRotationAngle

  static final float slerpN_epsilon = 0.00001f;

  /**
   * spherical linear interpolation of 3D vector. return a vector that
   * lies "at t between a1 and a2", i.e. a1 for t == 0, a2 for t == 1
   * and an interpolation of a1 and a2 for values between 0 and 1.
   * a1offs and a2offs allow a1 and a2 to start at an offset.
   * result is written at aoffs into array a.
   */

  public static void slerpNorm (float[] a1, int a1offs, float[] a2, int a2offs, float t, float[] a, int aoffs)
  {
    float[] p = { a1 [a1offs], a1 [a1offs+1], a1 [a1offs+2] };
    float[] q = { a2 [a2offs], a2 [a2offs+1], a2 [a2offs+2] };

    double alpha, beta;
    double cosom = dot (p, q);

    if ((cosom + 1.0) > slerpN_epsilon)
    {
      if ((1.0 - cosom) > slerpN_epsilon)  // normal case: slerp
      {
        double omega = Math.acos (cosom);
        double sinom = Math.sin (omega);
        alpha = Math.sin ((1.0 - t) * omega) / sinom;
        beta = Math.sin (t * omega) / sinom;
      }
      else  // vectors (nearly) coincide: linear interpolation
      {
        alpha = 1.0 - t;
        beta = t;
      }
    }
    else  // vectors (nearly) opposite: go over orthogonal vector in between
    {
      float[] v = new float[3];
      getOrthogonalVector (p, v);

      if (t < 0.5)
      {
        alpha = Math.sin ((1 - 2*t)*Math.PI/2);
        beta  = Math.sin (t*Math.PI);
        q = v;
      }
      else
      {
        alpha = Math.sin ((1 - t)*Math.PI);
        beta  = Math.sin ((2*t - 1)*Math.PI/2);
        p = v;
      }
    }

    for (int i = 0; i < 3; i++)
      a[aoffs+i] = (float) (alpha*p[i] + beta*q[i]);

  } // slerpNorm

  /**
   * conversion to string: "(x, y, z)". E.g.: System.out.println (v);
   */
  public String toString ()
  {
    return print (value_);
  }

  /**
   * print a 3D float array (debugging tool)
   */
  public static String print (float[/*3*/] v)
  {
    return "(" + v[0] + ", " + v[1] + ", " + v[2] + ")";
  }

} // Vec3f
