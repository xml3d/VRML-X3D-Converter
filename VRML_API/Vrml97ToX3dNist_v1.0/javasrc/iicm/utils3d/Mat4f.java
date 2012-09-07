/*
 * Mat4f - small utilities for 4D matrices
 * representing homogeneous transformations
 *
 * created: mpichler, 19970722
 *
 * changed: kwagen, 19970722
 * changed: mpichler, 19970729
 * changed: apesen, 19970924
 *
 * $Id: Mat4f.java,v 1.3 1997/09/25 15:17:33 apesen Exp $
 */


package iicm.utils3d;


/**
 * Mat4f - small utilities for 4D matrices.
 * Copyright (c) 1997 IICM
 *
 * @author Michael Pichler
 * @version 1.0, changed: 22 Jul 97
 */


public final class Mat4f
{
  // this class provides methods for manipulating 4D and 3D matrices
  // but does not encapsulate storage.
  //
  // The values are stored in a single dimensional float array:
  // 4D: [ 0] [ 1] [ 2] [ 3]  3D: [0] [1] [2]
  //     [ 4] [ 5] [ 6] [ 7]      [3] [4] [5]
  //     [ 8] [ 9] [10] [11]      [6] [7] [8]
  //     [12] [13] [14] [15]
  //
  // The notation assumes multiplying with row vectors from the left side
  // (row major order), thus elements 12 to 14 are the translation part;
  // the last column (3, 7, 11, 15) is assumed to hold the values (0, 0, 0, 1)
  // the remaining 3x3 submatrix describes rotation/scaling/shearing

  // no need ever to create an instance of this class
  private Mat4f ()  { }

  /** 3x3 identity matrix */
  public static float[] identity3d =
  {
    1.0f, 0.0f, 0.0f,
    0.0f, 1.0f, 0.0f,
    0.0f, 0.0f, 1.0f
  };

  /** 4x4 identity matrix */
  public static float[] identity4d =
  {
    1.0f, 0.0f, 0.0f, 0.0f,
    0.0f, 1.0f, 0.0f, 0.0f,
    0.0f, 0.0f, 1.0f, 0.0f,
    0.0f, 0.0f, 0.0f, 1.0f
  };

  /**
   * compute the inverse of a 3x3 matrix.
   * matrix storage: 3x3 float[]
   * mat and inv must not point to the same storage.
   * @return flag whether matrix was regular
   */

  public static boolean invertMatrix33 (float[/*9*/] mat, float[/*9*/] inv)
  {
    // by kwagen
    float a = mat[0] * mat[4];
    float b = mat[1] * mat[5];
    float c = mat[2] * mat[3];
    float d = mat[4] * mat[6];
    float e = mat[3] * mat[8];
    float f = mat[5] * mat[7];

    float det = a * mat[8] +
                b * mat[6] +
                c * mat[7] -
                mat[2] * d -
                mat[1] * e -
                mat[0] * f;

    if (det == 0.0f)
      return false;

    inv[0] = (mat[4] * mat[8] - f) / det;
    inv[1] = (mat[2] * mat[7] - mat[1] * mat[8]) / det;
    inv[2] = (b - mat[2] * mat[4]) / det;
    inv[3] = (mat[5] * mat[6] - e) / det;
    inv[4] = (mat[0] * mat[8] - mat[2] * mat[6]) / det;
    inv[5] = (c - mat[0] * mat[5]) / det;
    inv[6] = (mat[3] * mat[7] - d) / det;
    inv[7] = (mat[1] * mat[6] - mat[0] * mat[7]) / det;
    inv[8] = (a - mat[1] * mat[3]) / det;

    return true;
  } // invertMatrix33

  /**
   * compute the inverse of a 4x4 matrix, where the last column is
   * supposed to be (0, 0, 0, 1), i.e. without perspective transformation.
   * mat and inv must not point to the same storage.
   * @return flag whether matrix was regular
   */

  public static boolean invertMatrix44 (float[/*16*/] mat, float[/*16*/] inv)
  {
    if (!invertMatrix33inside44 (mat, inv))
      return false;

    // new translation part: negative old translation transformed by new inverse
    float[] tran = { -mat[12], -mat[13], -mat[14] };
    tran = transformVector3Mat44 (tran, inv).value_;
    inv[12] = tran[0];
    inv[13] = tran[1];
    inv[14] = tran[2];
    // inv [3, 7, 11] initialized with 0.0f
    inv[15] = 1.0f;

    return true;
  } // invertMatrix44

  /**
   * compute the inverse of a 3x3 subpart of a 4x4 matrix
   * in: 4x4 float[], out: 3x3 float[]
   * @return flag whether matrix was regular
   */

  public static boolean invertMatrix33of44 (float[/*16*/] mat, float[/*9*/] inv)
  {
    float[] mat33 = { mat[0], mat[1], mat[2],
                      mat[4], mat[5], mat[6],
                      mat[8], mat[9], mat[10] };
    return invertMatrix33 (mat33, inv);
  }

  /**
   * compute the inverse of the 3x3 subpart of a 4x4 matrix
   * without changing other elements.
   * mat and inv must not point to the same storage.
   * @return flag whether matrix was regular
   */

  public static boolean invertMatrix33inside44 (float[/*16*/] mat, float[/*16*/] inv)
  {
    float a = mat[0] * mat[5];
    float b = mat[1] * mat[6];
    float c = mat[2] * mat[4];
    float d = mat[5] * mat[8];
    float e = mat[4] * mat[10];
    float f = mat[6] * mat[9];

    float det = a * mat[10] +
                b * mat[8] +
                c * mat[9] -
                mat[2] * d -
                mat[1] * e -
                mat[0] * f;

    if (det == 0.0f)
      return false;

    inv[0] = (mat[5] * mat[10] - f) / det;
    inv[1] = (mat[2] * mat[9] - mat[1] * mat[10]) / det;
    inv[2] = (b - mat[2] * mat[5]) / det;
    inv[4] = (mat[6] * mat[8] - e) / det;
    inv[5] = (mat[0] * mat[10] - mat[2] * mat[8]) / det;
    inv[6] = (c - mat[0] * mat[6]) / det;
    inv[8] = (mat[4] * mat[9] - d) / det;
    inv[9] = (mat[1] * mat[8] - mat[0] * mat[9]) / det;
    inv[10] = (a - mat[1] * mat[4]) / det;

    return true;
  } // invertMatrix33inside44

  /**
   * transpose a 3x3 matrix
   */

  public static void transposeMatrix33 (float mat[/*9*/])
  {
    float tmp;
    tmp = mat[1];  mat[1] = mat[3];  mat[3] = tmp;
    tmp = mat[2];  mat[2] = mat[6];  mat[6] = tmp;
    tmp = mat[5];  mat[5] = mat[7];  mat[7] = tmp;
  }

  // transpose a 4x4 matrix
  // public static void transposeMatrix44 ()
  // on demand

  /**
   * transpose the 3x3 subpart of a 4x4 matrix
   * without changing other elements.
   */

  public static void transposeMatrix33inside44 (float mat[/*16*/])
  {
    float tmp;
    tmp = mat[1];  mat[1] = mat[4];  mat[4] = tmp;
    tmp = mat[2];  mat[2] = mat[8];  mat[8] = tmp;
    tmp = mat[6];  mat[6] = mat[9];  mat[9] = tmp;
  }

  /**
   * transform 3D point by 4D matrix (incl. translation)
   */

  public static Vec3f transformPoint3Mat44 (float[/*3*/] p, float[/*16*/] mat)
  {
    float x = p[0];
    float y = p[1];
    float z = p[2];
    return new Vec3f (
      x * mat[0] + y * mat[4] + z * mat[8]  + mat[12],
      x * mat[1] + y * mat[5] + z * mat[9]  + mat[13],
      x * mat[2] + y * mat[6] + z * mat[10] + mat[14]
    );
  }

  /**
   * transform 3D vector by 3x3 subpart of 4D matrix (w/o translation)
   */

  public static Vec3f transformVector3Mat44 (float[/*3*/] p, float[/*16*/] mat)
  {
    float x = p[0];
    float y = p[1];
    float z = p[2];
    return new Vec3f (
      x * mat[0] + y * mat[4] + z * mat[8],
      x * mat[1] + y * mat[5] + z * mat[9],
      x * mat[2] + y * mat[6] + z * mat[10]
    );
  }

  /**
   * transform 3D vector by the <i>transposed</i> 3x3 subpart of 4D matrix.
   * (typical operation for transforming normal vectors.)
   */

  public static Vec3f transformVector3Mat44transp (float[/*3*/] p, float[/*16*/] mat)
  {
    float x = p[0];
    float y = p[1];
    float z = p[2];
    return new Vec3f (
      x * mat[0] + y * mat[1] + z * mat[2],
      x * mat[4] + y * mat[5] + z * mat[6],
      x * mat[8] + y * mat[9] + z * mat[10]
    );
  }

  /**
   * multiply to 4x4 matrices, where the last column is (0, 0, 0, 1)
   */

  public static float[] multiplyMat43in44 (float[/*16*/] a, float[/*16*/] b)
  {
    float[] res = new float [16];

    res[ 0] = a[ 0]*b[0] + a[ 1]*b[4] + a[ 2]*b[ 8];
    res[ 1] = a[ 0]*b[1] + a[ 1]*b[5] + a[ 2]*b[ 9];
    res[ 2] = a[ 0]*b[2] + a[ 1]*b[6] + a[ 2]*b[10];
    res[ 4] = a[ 4]*b[0] + a[ 5]*b[4] + a[ 6]*b[ 8];
    res[ 5] = a[ 4]*b[1] + a[ 5]*b[5] + a[ 6]*b[ 9];
    res[ 6] = a[ 4]*b[2] + a[ 5]*b[6] + a[ 6]*b[10];
    res[ 8] = a[ 8]*b[0] + a[ 9]*b[4] + a[10]*b[ 8];
    res[ 9] = a[ 8]*b[1] + a[ 9]*b[5] + a[10]*b[ 9];
    res[10] = a[ 8]*b[2] + a[ 9]*b[6] + a[10]*b[10];
    res[12] = a[12]*b[0] + a[13]*b[4] + a[14]*b[ 8] + b[12];
    res[13] = a[12]*b[1] + a[13]*b[5] + a[14]*b[ 9] + b[13];
    res[14] = a[12]*b[2] + a[13]*b[6] + a[14]*b[10] + b[14];

    // res[3,7,11] remain 0
    res[15] = 1.0f;

    return res;
  }

  public static void printMat44 (float[/*16*/] mat)
  {
    System.out.println ("["+mat[ 0]+", "+mat[ 1]+", "+mat[ 2]+", "+mat[ 3]+"]");
    System.out.println ("["+mat[ 4]+", "+mat[ 5]+", "+mat[ 6]+", "+mat[ 7]+"]");
    System.out.println ("["+mat[ 8]+", "+mat[ 9]+", "+mat[10]+", "+mat[11]+"]");
    System.out.println ("["+mat[12]+", "+mat[13]+", "+mat[14]+", "+mat[15]+"]");
  }

  public static void printMat33 (float[/*9*/] mat)
  {
    System.out.println("["+mat[0]+","+mat[1]+","+mat[2]+"]");
    System.out.println("["+mat[3]+","+mat[4]+","+mat[5]+"]");
    System.out.println("["+mat[6]+","+mat[7]+","+mat[8]+"]");
  }
} // Mat4f
