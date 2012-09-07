/*
 * QuatTest - Quaternion test class
 * Copyright (c) 1997 IICM
 *
 * created: mpichler, 19961011
 * changed: mpichler, 19961011
 *
 * $Id: QuatTest.java,v 1.4 1997/07/29 15:34:20 mpichler Exp $
 */


package iicm.utils3d;


class QuatTest
{
  public static void main (String[] args)
  {
    float[] xaxis = { 1.0f, 0.0f, 0.0f };
    float[] yaxis = { 0.0f, 1.0f, 0.0f };
    float[] zaxis = { 0.0f, 0.0f, 1.0f };
    float[] zero = { 0.0f, 0.0f, 0.0f };
    float deg90 = (float) (Math.PI / 2.0);
    float[] result;

    result = new Quaternion (yaxis, deg90).rotatePointCenter (xaxis, zero);
    System.out.println ("rotate x axis by 90° around y axis: " + Vec3f.print (result));
    result = new Quaternion (xaxis, deg90).rotatePointCenter (yaxis, zero);
    System.out.println ("rotate y axis by 90° around x axis: " + Vec3f.print (result));
    result = new Quaternion (zaxis, deg90).rotatePointCenter (xaxis, zero);
    System.out.println ("rotate x axis by 90° around z axis: " + Vec3f.print (result));
  } // main

} // QuatTest
