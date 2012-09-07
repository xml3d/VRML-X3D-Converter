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
 * Traverser.java
 * base class for traversing the scene graph
 *
 * created: mpichler, 19960920
 * changed: mpichler, 19970402
 *
 * $Id: Traverser.java,v 1.6 1997/05/22 15:34:36 apesen Exp $
 */


package iicm.vrml.pw;

import java.util.Enumeration;


/**
 * Traverser - scene graph traverser base class
 * Copyright (c) 1996,97 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.1, latest change:  9 Jan 97
 */


abstract public class Traverser
{
  // there is one traverse function for each Node type;
  // they could all be named the same, but different names
  // increase program readability

  // this way any kind of scene graph traversal may be implemented
  // outside the parser classes (most important: without changing them)

  // group traversal by default traverses all children,
  // node traversal does nothing by default

  /**
   * traverse a group node by traversing all children; this function
   * will be typically called on the root node (should be non-null);
   * traversal of all nodes derived from GroupNode will call this
   * function by default; all other nodes traversals are abstract
   */

  public void tGroupNode (GroupNode group)
  {
    Enumeration e = group.getChildrenEnumerator ();
    // System.out.println ("--- Traverser::tGroupNode");

    while (e.hasMoreElements ())
    {
      ((Node) e.nextElement ()).traverse (this);
    }
  }

  // Grouping nodes --> GroupNode
  protected void tGroup (Group g)  { tGroupNode (g); }
  protected void tAnchor (Anchor g)  { tGroupNode (g); }
  protected void tBillboard (Billboard g)  { tGroupNode (g); }
  protected void tCollision (Collision g)  { tGroupNode (g); }
  protected void tTransform (Transform g)  { tGroupNode (g); }
  protected void tInline (Inline g)  { tGroupNode (g); }
  protected void tLOD (LOD g)  { tGroupNode (g); }
  protected void tSwitch (Switch g)  { tGroupNode (g); }

  // Common Nodes
  abstract protected void tAudioClip (AudioClip n);
  abstract protected void tDirectionalLight (DirectionalLight n);
  abstract protected void tPointLight (PointLight n);
  abstract protected void tShape (Shape n);
  abstract protected void tSound (Sound n);
  abstract protected void tSpotLight (SpotLight n);
  abstract protected void tScript (Script n);
  abstract protected void tWorldInfo (WorldInfo n);

  // Sensor Nodes
  abstract protected void tCylinderSensor (CylinderSensor n);
  abstract protected void tPlaneSensor (PlaneSensor n);
  abstract protected void tProximitySensor (ProximitySensor n);
  abstract protected void tSphereSensor (SphereSensor n);
  abstract protected void tTimeSensor (TimeSensor n);
  abstract protected void tTouchSensor (TouchSensor n);
  abstract protected void tVisibilitySensor (VisibilitySensor n);

  // Geometry Nodes
  abstract protected void tBox (Box n);
  abstract protected void tCone (Cone n);
  abstract protected void tCylinder (Cylinder n);
  abstract protected void tElevationGrid (ElevationGrid n);
  abstract protected void tExtrusion (Extrusion n);
  abstract protected void tIndexedFaceSet (IndexedFaceSet n);
  abstract protected void tIndexedLineSet (IndexedLineSet n);
  abstract protected void tPointSet (PointSet n);
  abstract protected void tSphere (Sphere n);
  abstract protected void tText (Text n);

  // Geometric Properties
  abstract protected void tColor (Color n);
  abstract protected void tCoordinate (Coordinate n);
  abstract protected void tNormal (Normal n);
  abstract protected void tTextureCoordinate (TextureCoordinate n);

  // Appearance Nodes
  abstract protected void tAppearance (Appearance n);
  abstract protected void tFontStyle (FontStyle n);
  abstract protected void tMaterial (Material n);
  abstract protected void tImageTexture (ImageTexture n);
  abstract protected void tMovieTexture (MovieTexture n);
  abstract protected void tPixelTexture (PixelTexture n);
  abstract protected void tTextureTransform (TextureTransform n);

  // Interpolator Nodes
  abstract protected void tColorInterpolator (ColorInterpolator n);
  abstract protected void tCoordinateInterpolator (CoordinateInterpolator n);
  abstract protected void tNormalInterpolator (NormalInterpolator n);
  abstract protected void tOrientationInterpolator (OrientationInterpolator n);
  abstract protected void tPositionInterpolator (PositionInterpolator n);
  abstract protected void tScalarInterpolator (ScalarInterpolator n);

  // Bindable Nodes
  abstract protected void tBackground (Background n);
  abstract protected void tFog (Fog n);
  abstract protected void tNavigationInfo (NavigationInfo n);
  abstract protected void tViewpoint (Viewpoint n);

  // ROUTE statement (node); usually not traversed
  void tRoute (RouteNode n)  { }

  // PROTO node declaration (usually not traversed)
  //void tProtoNode (ProtoNode n)  { }
  abstract protected void tProtoNode (ProtoNode n);

  // actual instance of a PROTO node (to be traversed)
  abstract protected void tProtoInstance (ProtoInstance n);

} // class Traverser
