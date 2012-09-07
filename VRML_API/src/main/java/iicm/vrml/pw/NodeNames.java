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
 * NodeNames.java
 * definiton of node names
 *
 * created: mpichler, 19960930 (pulled out of Node.java)
 * changed: krosch, 19961104
 * changed: mpichler, 19970108
 *
 * $Id: NodeNames.java,v 1.5 1997/08/01 18:30:52 mpichler Exp $
 */


package iicm.vrml.pw;


/**
 * NodeNames - definiton of node names; node creation by name
 * Copyright (c) 1996,97 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.1, latest change:  8 Jan 97
 */


abstract public class NodeNames
{
  // no need ever to create an instance of this class
  private NodeNames ()  { }

  // Grouping nodes --> GroupNode
  public final static String NODE_GROUP = "Group";
  public final static String NODE_ANCHOR = "Anchor";
  public final static String NODE_BILLBOARD = "Billboard";
  public final static String NODE_COLLISION = "Collision";
  public final static String NODE_TRANSFORM = "Transform";
  public final static String NODE_INLINE = "Inline";
  public final static String NODE_LOD = "LOD";
  public final static String NODE_SWITCH = "Switch";

  // Common Nodes --> Common
  public final static String NODE_AUDIOCLIP = "AudioClip";
  public final static String NODE_DIRECTIONALLIGHT = "DirectionalLight";
  public final static String NODE_POINTLIGHT = "PointLight";
  public final static String NODE_SHAPE = "Shape";
  public final static String NODE_SOUND = "Sound";
  public final static String NODE_SPOTLIGHT = "SpotLight";
  public final static String NODE_SCRIPT = "Script";
  public final static String NODE_WORLDINFO = "WorldInfo";

  // Sensors --> Sensor
  public final static String NODE_CYLINDERSENSOR = "CylinderSensor";
  public final static String NODE_PLANESENSOR = "PlaneSensor";
  public final static String NODE_PROXIMITYSENSOR = "ProximitySensor";
  public final static String NODE_SPHERESENSOR = "SphereSensor";
  public final static String NODE_TIMESENSOR = "TimeSensor";
  public final static String NODE_TOUCHSENSOR = "TouchSensor";
  public final static String NODE_VISIBILITYSENSOR = "VisibilitySensor";

  // Geometry --> Geometry
  public final static String NODE_BOX = "Box";
  public final static String NODE_CONE ="Cone";
  public final static String NODE_CYLINDER = "Cylinder";
  public final static String NODE_ELEVATIONGRID = "ElevationGrid";
  public final static String NODE_EXTRUSION = "Extrusion";
  public final static String NODE_INDEXEDFACESET = "IndexedFaceSet";
  public final static String NODE_INDEXEDLINESET = "IndexedLineSet";
  public final static String NODE_POINTSET = "PointSet";
  public final static String NODE_SPHERE = "Sphere";
  public final static String NODE_TEXT = "Text";

  // Geometric Properties --> Node
  public final static String NODE_COLOR = "Color";
  public final static String NODE_COORDINATE = "Coordinate";
  public final static String NODE_NORMAL = "Normal";
  public final static String NODE_TEXTURECOORDINATE = "TextureCoordinate";

  // Appearance --> AppearNode
  public final static String NODE_APPEARANCE = "Appearance";
  public final static String NODE_FONTSTYLE = "FontStyle";
  public final static String NODE_IMAGETEXTURE = "ImageTexture";
  public final static String NODE_MATERIAL = "Material";
  public final static String NODE_MOVIETEXTURE = "MovieTexture";
  public final static String NODE_PIXELTEXTURE = "PixelTexture";
  public final static String NODE_TEXTURETRANSFORM = "TextureTransform";

  // Interpolators --> Interpolator
  public final static String NODE_COLORINTERPOLATOR = "ColorInterpolator";
  public final static String NODE_COORDINATEINTERPOLATOR = "CoordinateInterpolator";
  public final static String NODE_NORMALINTERPOLATOR = "NormalInterpolator";
  public final static String NODE_ORIENTATIONINTERPOLATOR = "OrientationInterpolator";
  public final static String NODE_POSITIONINTERPOLATOR = "PositionInterpolator";
  public final static String NODE_SCALARINTERPOLATOR = "ScalarInterpolator";

  // bindable nodes --> Bindable
  public final static String NODE_BACKGROUND = "Background";
  public final static String NODE_FOG = "Fog";
  public final static String NODE_NAVIGATIONINFO = "NavigationInfo";
  public final static String NODE_VIEWPOINT ="Viewpoint";


  /**
   * create basic node instance from its name
   */

  public final static Node createInstanceFromName (String name)
  {
    // TODO: check context; not all nodes allowed at any place

    // nice Java construct, but explicit lookup trusted more
    // Node node = null;
    // try
    // {
    //   node = (Node) Class.forName (name).newInstance ();
    //   if (! (node instanceof Node))
    //     node = null;
    // }
    // catch (IllegalAccessException e)  { node = null; }
    // catch (ClassNotFoundException e)  { node = null; }
    // catch (InstantiationException e)  { node = null; }

    // Grouping nodes --> GroupNode
    if (name.equals (NODE_GROUP))               return new Group ();
    if (name.equals (NODE_ANCHOR))              return new Anchor ();
    if (name.equals (NODE_BILLBOARD))           return new Billboard ();
    if (name.equals (NODE_COLLISION))           return new Collision ();
    if (name.equals (NODE_TRANSFORM))           return new Transform ();

    // Special Groups --> SpecialGroup
    if (name.equals (NODE_INLINE))              return new Inline ();
    if (name.equals (NODE_LOD))                 return new LOD ();
    if (name.equals (NODE_SWITCH))              return new Switch ();

    // Common Nodes --> Common
    if (name.equals (NODE_AUDIOCLIP))           return new AudioClip ();
    if (name.equals (NODE_DIRECTIONALLIGHT))    return new DirectionalLight ();
    if (name.equals (NODE_POINTLIGHT))          return new PointLight ();
    if (name.equals (NODE_SHAPE))               return new Shape ();
    if (name.equals (NODE_SOUND))               return new Sound ();
    if (name.equals (NODE_SPOTLIGHT))           return new SpotLight ();
    if (name.equals (NODE_SCRIPT))              return new Script ();
    if (name.equals (NODE_WORLDINFO))           return new WorldInfo ();

    // Sensors --> Sensor
    if (name.equals (NODE_CYLINDERSENSOR))      return new CylinderSensor ();
    if (name.equals (NODE_PLANESENSOR))         return new PlaneSensor ();
    if (name.equals (NODE_PROXIMITYSENSOR))     return new ProximitySensor ();
    if (name.equals (NODE_SPHERESENSOR))        return new SphereSensor ();
    if (name.equals (NODE_TIMESENSOR))          return new TimeSensor ();
    if (name.equals (NODE_TOUCHSENSOR))         return new TouchSensor ();
    if (name.equals (NODE_VISIBILITYSENSOR))    return new VisibilitySensor ();

    // Geometry --> Geometry
    if (name.equals (NODE_BOX))                 return new Box ();
    if (name.equals (NODE_CONE))                return new Cone ();
    if (name.equals (NODE_CYLINDER))            return new Cylinder ();
    if (name.equals (NODE_ELEVATIONGRID))       return new ElevationGrid ();
    if (name.equals (NODE_EXTRUSION))           return new Extrusion ();
    if (name.equals (NODE_INDEXEDFACESET))      return new IndexedFaceSet ();
    if (name.equals (NODE_INDEXEDLINESET))      return new IndexedLineSet ();
    if (name.equals (NODE_POINTSET))            return new PointSet ();
    if (name.equals (NODE_SPHERE))              return new Sphere ();
    if (name.equals (NODE_TEXT))                return new Text ();

    // Geometric Properties --> Node
    if (name.equals (NODE_COLOR))               return new Color ();
    if (name.equals (NODE_COORDINATE))          return new Coordinate ();
    if (name.equals (NODE_NORMAL))              return new Normal ();
    if (name.equals (NODE_TEXTURECOORDINATE))   return new TextureCoordinate ();

    // Appearance --> AppearNode
    if (name.equals (NODE_APPEARANCE))          return new Appearance ();
    if (name.equals (NODE_FONTSTYLE))           return new FontStyle ();
    if (name.equals (NODE_MATERIAL))            return new Material ();
    if (name.equals (NODE_IMAGETEXTURE))        return new ImageTexture ();
    if (name.equals (NODE_MOVIETEXTURE))        return new MovieTexture ();
    if (name.equals (NODE_PIXELTEXTURE))        return new PixelTexture ();
    if (name.equals (NODE_TEXTURETRANSFORM))    return new TextureTransform ();

    // Interpolators --> Interpolator
    if (name.equals (NODE_COLORINTERPOLATOR))           return new ColorInterpolator ();
    if (name.equals (NODE_COORDINATEINTERPOLATOR))      return new CoordinateInterpolator ();
    if (name.equals (NODE_NORMALINTERPOLATOR))          return new NormalInterpolator ();
    if (name.equals (NODE_ORIENTATIONINTERPOLATOR))     return new OrientationInterpolator ();
    if (name.equals (NODE_POSITIONINTERPOLATOR))        return new PositionInterpolator ();
    if (name.equals (NODE_SCALARINTERPOLATOR))          return new ScalarInterpolator ();

    // bindable nodes
    if (name.equals (NODE_BACKGROUND))          return new Background ();
    if (name.equals (NODE_FOG))                 return new Fog ();
    if (name.equals (NODE_NAVIGATIONINFO))      return new NavigationInfo ();
    if (name.equals (NODE_VIEWPOINT))           return new Viewpoint ();

    return null;  // no match

  } // createInstanceFromName

} // NodeNames
