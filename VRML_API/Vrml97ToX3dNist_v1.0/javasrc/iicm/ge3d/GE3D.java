/*
 * GE3D.java
 * Java interface to GE3D
 *
 * created: mpichler, 19961009
 *
 * changed: kwagen, 19970507
 * changed: mpichler, 19970609
 *
 * $Id: GE3D.java,v 1.16 1997/09/15 09:37:43 apesen Exp $
 */


package iicm.ge3d;


/**
 * GE3D - Java interface to the GE3D library.
 * Copyright (c) 1996,97 IICM. All rights reserved.
 *
 * @author Michael Pichler, Karl Heinz Wagenbrunn
 * @version 0.7, changed:  9 Jul 97
 */


abstract public class GE3D
{
  // no need ever to create an instance of this class
  private GE3D ()  { }

  /*
   * constants
   */
  // these are *not* machine generated
  // any inconsistencies with <ge3d/ge3d.h> may cause strange errors

  // drawing modes (enum ge3d_mode_t)
  public final static int ge3d_wireframe = 0;
  public final static int ge3d_hidden_line = 1;
  public final static int ge3d_flat_shading = 2;
  public final static int ge3d_smooth_shading = 3;
  public final static int ge3d_texturing = 4;

  // cylinder parts (enum ge3d_cylinder_t)
  public final static int cyl_sides = 0x1;
  public final static int cyl_bottom = 0x2;
  public final static int cyl_top = 0x4;
  public final static int cyl_all = 0x7;

  // hint flags (enum ge3d_hint_t)
  public final static int HINT_DEPTHBUFFER = 0;
  public final static int HINT_BACKFACECULLING = 1;
  public final static int HINT_LIGHTING = 2;
  public final static int HINT_TEXLIGHTING = 3;
  public final static int HINT_AMBIENTCOLOR = 4;
  public final static int HINT_QUADSLICES = 5;
  public final static int HINT_TRANSPARENCY = 6;
  public final static int HINT_CCW = 7;

  // anti-aliasing flags (enum ge3d_antialiasing_t)
  public final static int AA_LINES = 1;
  public final static int AA_POLYGONS = 2;
  public final static int AA_POLYGONS_FRONT2BACK = 4;

  // material bindings (enum ge3d_matbinding_t)
  public final static int MATB_OVERALL = 1;
  public final static int MATB_PERFACE = 4;
  public final static int MATB_PERFACEINDEXED = 5;
  public final static int MATB_PERVERTEXINDEXED = 7;

  // transparency method (enum ge3d_transparency_t)
  public final static int TRANSP_OFF = 0;
  public final static int TRANSP_STIPPLE = 1;
  public final static int TRANSP_BLEND = 2;

  // axis
  public final static float[] X_AXIS = { 1.0f, 0.0f, 0.0f };
  public final static float[] Y_AXIS = { 0.0f, 1.0f, 0.0f };
  public final static float[] Z_AXIS = { 0.0f, 0.0f, 1.0f };


  /*
   * funtions
   */

  // basic functions
  public static native void initGE3D ();
  public static native void setDrawMode (int mode);
  public static native void setBackgroundColor (int rgb);
  public static native void clearScreen ();
  public static native void hint (int flag, int value);
  public static native int antialiasingSupport ();
  public static native void antialiasing (int flags);

  // transformations
  public static native void rotatef3f (float[] axis, float angle);  // angle in radians
  public static native void translatefff (float x, float y, float z);
  public static native void ge3dTransformMcWc (float[] in, float[] out);

  // matrix operations
  public static native void loadIdentity ();
  public static native void pushMatrix ();
  public static native void pushThisMatrix (float[/*16*/] mat);
  public static native void popMatrix ();
  public static native void getMatrix (float[/*16*/] mat);

  // camera setting
  public static native void setPerspectiveCamera (float[/*3*/] position, float[/*4*/] axisangle,
    float fovy, float aspect, float hither, float yon);  // angle (axisangle[3]) and fovy in radians
  public static native void simpleOrthoCamera (float width, float height);

  // lighting
  public static native void deactivateLights (int from, int to);
  public static native void setHeadLight (float[] color);
  // set positional/directional light source and turn it on
  public static native void activateLightSource (int index, float[] color, float intensity,
    float[] position, float positional);

  // color, material
  public static native void fillColor3f (float[/*3*/] color);
  public static native void lineColorRGBi (int color);  // 0xRRGGBB
  public static native void lineColor3f (float[/*3*/] color);
  public static native void material (float ambient, float[] diffuse, float[] emissive, float shininess,
                                      float[] specular, float transparency);
  public static native void defaultMaterial ();
  public static native void lineStyle (short pattern);  // 16 bit line pattern

  // texturing
  public static native int createPixelTexture (int[] image);
  public static native int createImageTexture (int width, int height, int[] image);
  public static native void doTexturing (int on);
  public static native void applyTexture (int index);
  public static native void textureRepeat (int s, int t);
  public static native void freeTexture (int index);
  public static native void setTextureMipmapping (int quality);
  public static native void loadTextureIdentity ();
  public static native void loadTextureMatrix (float[/*16*/] mat);
  public static native void alphaTest (float threshold);
  public static native int getTextureAlpha ();

  // geometry
  public static native void drawCube (float[] min, float[] max);
  public static native void drawWireCube (float[] min, float[] max);
  public static native void drawCylinder (float botrad, float toprad, float bottom, float height, int parts);
  public static native void drawFaceSet (
    float[] verts, int numcoordinds, int[] coordinds, float[] fnormals, float[] texcoords, int numtexinds,
    int[] texcoordinds, int numcolor, float[] color, int numcolorinds, int[] colorinds, int matbinding,
    float[] normallist, int numnormalinds, int[] normalindex
  );
  public static native void drawLineSet (
    float[] verts, int numcoordinds, int[] coordinds,
    int numcolor, float[] color, int numcolorind, int[] colorind, int pervertex
  );
  public static native void drawPointSet (float[] verts, int numverts, float[] color, int numcolor);
  public static native void drawSphere (float radius);

  // other drawing functions (2D)
  public static native void drawLine2D (float x0, float y0, float x1, float y1);
  public static native void drawRect2D (float x0, float y0, float x1, float y1);
  public static native void drawPolyLines2D (float[] coord);  // { n (x, y) pairs } until n = 0.0f
  public static native void drawCircle (float x, float y, float r);

} // GE3D
