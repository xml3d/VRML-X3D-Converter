/*
 * Quaternion - qu. utils
 *
 * created: mpichler, 19961009
 *
 * changed: jwolte, 19970801
 * changed: mpichler, 19970805
 *
 * $Id: Quaternion.java,v 1.12 1997/08/06 13:10:56 apesen Exp $
 */


package iicm.utils3d;


/**
 * Quaternion - quaternion used to describe rotations/orientations
 * Copyright (c) 1996,97 IICM
 *
 * @author Michael Pichler
 * @version 0.2, changed: 14 Jan 97
 */

// quaternion Literature:
// "Using Quaternions for Coding 3D Transformations",
// P.G. Maillot, in: Graphics Gems, pp. 498--515


public class Quaternion
{
  /**
   * the val array is kept normalized: we deal with <b>unit</b> quaternions only;
   * val[0..2] store the vector part, val[3] is the scalar part
   */

  private float[/*4*/] val = { 0.0f, 0.0f, 0.0f, 1.0f };

  static final int X = Vec3f.X;
  static final int Y = Vec3f.Y;
  static final int Z = Vec3f.Z;
  static final int W = 3;

  /**
   * default: "identity" quaternion (angle 0, any axis)
   */

  public Quaternion ()  { }

  /**
   * create a quaternion with a normalized axis and angle
   */

  public Quaternion (float[/*3*/] axis, float angle)
  {
    float s = (float) Math.sin (angle / 2.0);
    // vector part: sin (phi/2) * axis
    val[0] = s * axis[0];
    val[1] = s * axis[1];
    val[2] = s * axis[2];
    // scalar part: cos (phi/2)
    val[3] = (float) Math.cos (angle / 2.0);
    // quaternion is normalized when axis was

    // System.out.println ("quaternion. axis: " + Vec3f.print (axis) + ", angle: " + angle +
    // "; vector: " + Vec3f.print (val) + ", scalar: " + val[3]);
  }

  /**
   * create a quaternion. take normalized axis and angle
   * out of an array, starting at offset position.
   */

  public Quaternion (int offset, float[] arr)
  {
    double halfangle = arr[offset + 3] / 2.0;

    float s = (float) Math.sin (halfangle);
    // vector part: sin (phi/2) * axis
    val[0] = s * arr[offset];
    val[1] = s * arr[offset + 1];
    val[2] = s * arr[offset + 2];
    // scalar part: cos (phi/2)
    val[3] = (float) Math.cos (halfangle);
    // quaternion is normalized when axis was
  }

  /**
   * copy constructor
   */

  public Quaternion (Quaternion q)
  {
    float[] v = q.val;

    val[0] = v[0];
    val[1] = v[1];
    val[2] = v[2];
    val[3] = v[3];
  }
    
  /**
   * create a quaternion in its internal representation;
   * float[4] array will then be handled by the quaternion
   */

  private Quaternion (float[/*4*/] vals)
  {
    val = vals;
  }

  /**
   * get the rotation (normalized axis and angle) that rotates vector a
   * to vector b. a and b should be normalized
   * @see #rotationAxisToVector
   */

  public static float[/*4*/] rotationBetweenVectors (float[/*3*/] a, float[/*3*/] b)
  {
    Vec3f rotaxis = new Vec3f ();
    // System.err.println ("    a: " + Vec3f.print (a) + ", b: " + Vec3f.print (b));
    rotaxis.cross (a, b);
    // System.err.println ("  cross product: " + rotaxis + ", dot product: " + Vec3f.dot (a, b));

    return rotationBetweenVectorsInternal (rotaxis, Vec3f.dot (a, b), b);
  }

  /**
   * get the rotation (normalized axis and angle) that rotates a
   * coordinate axis (given by number) to another (normalized) vector.
   * e.g. rotationAxisToVector (2, negativenormalizedlookatvector) gives
   * you the orientation axis/angle values of a VRML viewpoint.
   * @see #rotationBetweenVectors
   */

  public static float[/*4*/] rotationAxisToVector (int num, float[/*3*/] v)
  {
    Vec3f rotaxis = new Vec3f ();  // rotation axis: num X v
    switch (num)
    {
      case 0:
        rotaxis.assign (0, -v[2], v[1]);  // (0, -z, y)
      break;
      case 1:
        rotaxis.assign (v[2], 0, -v[0]);  // (z, 0, -x)
      break;
      case 2:
        rotaxis.assign (-v[1], v[0], 0);  // (-y, x, 0)
      break;
    }
    return rotationBetweenVectorsInternal (rotaxis, v[num], v);
  }

  /**
   * implementation of rotationBetweenVectors and rotationAxisToVector.
   * @param rotaxis cross product (rotation axis, not yet normalized)
   * @param dotpr dot product of the two vectors (cos of rotation angle)
   * @param b target vector (only needed if vectors were collinear)
   */

  private static float[/*4*/] rotationBetweenVectorsInternal (Vec3f rotaxis, double dotpr, float[/*3*/] b)
  {
    // the first version of this function directly returned a Quaternion;
    // it turned out that often one needs a rotation with a modified angle,
    // so it now just returns axis and angle (which can be used to construct
    // a Quaternion anyways).
    float[] axis = new float[4];

    float len = rotaxis.normalize ();

    // dotpr is cos alpha (vectors were normalized)
    if (len >= 0.0f && dotpr < 1.0 && dotpr > -1.0)  // rotaxis well defined
    {
      System.arraycopy (rotaxis.value_, 0, axis, 0, 3);
      axis [3] = (float) Math.acos (dotpr);  // angle

      return axis;
    }

    // else collinear vectors
    if (dotpr > 0)  // b was a positive multiple of a: no rotation needed
    { // zero angle, any normalized axis
      axis [0] = 1.0f;  // other fields 0
      return axis;
    }

    // else b was a negative multiple of a
    // rotate by PI around any vector normal to target
    axis[3] = (float) Math.PI;
    Vec3f.getOrthogonalVector (rotaxis.value_, axis);

    return axis;

  } // betweenVectorsInternal

  // Quaternion.printVec3f superseded by Vec3f.print

  /**
   * back to the "identity" quaternion
   */

  public final void reset ()
  {
    val[0] = val[1] = val[2] = 0.0f;  // vector part
    val[3] = 1.0f;  // scalar part
  }

  /**
   * make the quaternion represent a rotation around (0, +/-1, 0);
   * i.e. on the same "level"
   */

  public void levelize ()
  { // not mathematically derived, but seems to work fine
    val[0] = val[2] = 0.0f;
    renormalize ();
  }

  /**
   * make the quaternion represent tilt-free rotation (no z part)
   */

  public void untilt ()
  { // not mathematically derived, but seems to work fine
    val[2] = 0.0f;
    renormalize ();
  }

  /**
   * ensure the quaternion stays normalized.
   * Useful e.g. after several mulitplications
   */

  public final void renormalize ()
  {
    float norm = (float) Math.sqrt (val[0] * val[0] + val[1] * val[1] + val[2] * val[2] + val[3] * val[3]);
    if (norm == 0.0f)
    {  // irreparable damage
      System.out.println ("bad quaternion. reset.");
      reset ();
    }
    else
    {
      norm = 1.0f / norm;
      val[0] *= norm;
      val[1] *= norm;
      val[2] *= norm;
      val[3] *= norm;
    }
  } // renormalize

  /**
   * convert quaternion to normalized axis and angle
   */

  public final float[/*4*/] getAxisAngle ()
  {
    float[] axis = new float[4];
    double angle = Math.acos (val[3]);
    float s = (float) Math.sin (angle);
    if (s != 0.0f)
    { // rotation axis: vector / sin (acos (scalar))
      axis[0] = val[0] / s;
      axis[1] = val[1] / s;
      axis[2] = val[2] / s;
      // rotation angle: 2 * acos (scalar)
      axis[3] = 2.0f * (float) angle;
    }
    else
    { // zero angle (mod 2*pi): any normalized rotation axis
      axis[0] = 1.0f;  axis[1] = axis[2] = 0.0f;
      axis[3] = 0.0f;
    }

    return axis;
  } // getAxisAngle

  /**
   * calculate the product of two quaternions (both q0, q1 unchanged).
   * when q0 and q1 represent rotations, the result is q0 done <b>after</b> q1.
   * @return q0 * q1 (multiplication non commutative)
   */

  public final static Quaternion product (Quaternion q0, Quaternion q1)
  {
    return new Quaternion (doMultiplication (q0, q1));
  }

  /**
   * multiply this quaternion (q0) with another (q1) from the right side.
   * q0 = q0 * q1 (multiplication non commutative), having the effect of
   * preconcatening the rotation q1 to this one
   */

  public final void multiply (Quaternion q1)
  {
    val = doMultiplication (this, q1);
  }

  /**
   * multiply this quaternion (q0) with another (q1) from the left side.
   * q0 = q1 * q0 (multiplication non commutative), having the effect of
   * postconcatening the rotation q1 to this one
   */

  public final void multiplyLeft (Quaternion q1)
  {
    val = doMultiplication (q1, this);
  }

  // actually do the multiplication of two quaternions: q0 * q1
  // returns the internal representation of the product

  private final static float[/*4*/] doMultiplication (Quaternion q0, Quaternion q1)
  {
    float[] vals = new float[4];
    float[] v0 = q0.val;
    float[] v1 = q1.val;
    float s0 = q0.val[3];
    float s1 = q1.val[3];
    // vector part: s0 * v1 + s1 * v0 + v0 X v1
    vals[0] = s0 * v1[0] + s1 * v0[0] + v0[1] * v1[2] - v0[2] * v1[1];
    vals[1] = s0 * v1[1] + s1 * v0[1] + v0[2] * v1[0] - v0[0] * v1[2];
    vals[2] = s0 * v1[2] + s1 * v0[2] + v0[0] * v1[1] - v0[1] * v1[0];
    // scalar part: s0 * s1 - (v0 . v1)
    vals[3] = s0 * s1 - (v0[0] * v1[0] + v0[1] * v1[1] + v0[2] * v1[2]);
    // product is normalized when the multiplicands were
    return vals;  // may be used in any way
  } // doMultiplication

  static final float slerp_epsilon = 0.00001f;

  /**
   * spherical linear interpolation. return a quaternion that lies "at
   * t between q1 and q2", i.e. q1 for t == 0, q2 for t == 1 and an
   * interpolation of q1 and q2 for values between 0 and 1.
   * remind that q and -q describe the same rotation, thus interpolate
   * to -q2 if path to it is shorter than to q2.
   */

  public static Quaternion slerp (Quaternion q1, Quaternion q2, float t)
  {
    int i;
    float[] p = q1.val;
    float[] q = q2.val;
    float[] qt = new float[4];

    // Lit.: Quaternion Interpolation With Extra Spins, pp. 96f, 461f
    // Jack Morrison, Graphics Gems III, AP Professional

    double alpha, beta;
    double cosom = p[X]*q[X] + p[Y]*q[Y] + p[Z]*q[Z] + p[W]*q[W]; 

    boolean flip;
    if (flip = (cosom < 0))  // use -q2
      cosom = -cosom;

    if ((1.0 - cosom) > slerp_epsilon)
    {
      double omega = Math.acos (cosom);
      double sinom = Math.sin (omega);
      alpha = Math.sin ((1.0 - t) * omega) / sinom;
      beta = Math.sin (t * omega) / sinom;
    }
    else
    {
      alpha = 1.0 - t;
      beta = t;
    }

    if (flip)
      beta = -beta;

    for (i = 0; i < 4; i++)
      qt[i] = (float) (alpha*p[i] + beta*q[i]);

    return new Quaternion (qt);

  } // slerp

  /**
   * transform (rotate) an axis by the quaternion
   * @param i no. of the axis (0 for x, 1 for y, 2 for z)
   * @return the transformed axis (unit length)
   */

  public float[/*3*/] transformAxis (int num)
  {
    float[] retv = new float[3];
    float[] u = val;
    float c = val[3];
    // tval becomes (c*c - u.u)*v + 2*v.u*u + 2*c*uXv,
    // where c is the scalar part, u the vector part,
    // and v the unitvector with 1 at position num
    // (c*c - u.u = 2*c*c - 1)

    retv[num] = 2.0f * c * c - 1.0f;  // c * c - (u[0]*u[0] + u[1]*u[1] + u[2]*u[2]);
    c *= 2.0f;

    switch (num)
    {
      case 0:
        retv[1] =   c * u[2];
        retv[2] = - c * u[1];
      break;
      case 1:
        retv[0] = - c * u[2];
        retv[2] =   c * u[0];
      break;
      case 2:
        retv[0] =   c * u[1];
        retv[1] = - c * u[0];
      break;
    }

    c = 2 * u[num];  // 2*v.u
    retv[0] += c * u[0];
    retv[1] += c * u[1];
    retv[2] += c * u[2];

    // System.out.println ("Quaternion. Rotation axis " + num + " corresponds to "
    // + retv[0] + ", " + retv[1] + ", " + retv[2]);

    return retv;

  } // transformAxis

  /**
   * rotate a vector by the quaternion;
   * the result will be normalized if the input vector was;
   * the input vector will not be changed
   */

  public float[/*3*/] rotateVector (float[/*3*/] v)
  {
    float[] retv = new float[3];
    float[] u = val;
    float c = val[3];
    // v is transformed by q as q*v*q' (q' is the conjungate or inverse of q)
    // and v is seen as the "pure" quaternion [v, 0]
    // tval becomes (c*c - u.u)*v + 2*v.u*u + 2*c*uXv or
    // (2*c*c - 1)*v + 2*v.u*u + 2*c*uXv
    // where are the good old C macros ...

    float h3 = 2.0f * c;
    float h1 = h3 * c - 1.0f;
    float h2 = 2.0f * (v[0] * u[0] + v[1] * u[1] + v[2] * u[2]);

    retv[0] = h1 * v[0] + h2 * u[0] + h3 * (u[1] * v[2] - u[2] * v[1]);
    retv[1] = h1 * v[1] + h2 * u[1] + h3 * (u[2] * v[0] - u[0] * v[2]);
    retv[2] = h1 * v[2] + h2 * u[2] + h3 * (u[0] * v[1] - u[1] * v[0]);

    return retv;

  } // rotateVector (v)

  /**
   * rotate a point p about an arbitrary center c by the quaternion,
   * i.e. add (the rotated vector from c to p) to c.
   * @see #rotateVector
   */

  public float[/*3*/] rotatePointCenter (float [/*3*/] v, float [/*3*/] c)
  {
    float[] delta = new float[3];
    delta[0] = v[0] - c[0];
    delta[1] = v[1] - c[1];
    delta[2] = v[2] - c[2];

    delta = rotateVector (delta);

    delta[0] += c[0];
    delta[1] += c[1];
    delta[2] += c[2];

    return delta;

  } // rotateVectorCenter

} // Quaternion
