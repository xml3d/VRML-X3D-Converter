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
 * Material.java
 * Copyright (c) 1997 IICM
 *
 * created: mpichler, 19961001
 *
 * changed: mpichler, 19961001
 * changed: apesen, 19970410
 *
 * $Id: Material.java,v 1.3 1997/05/22 10:22:33 apesen Exp $
 */


package iicm.vrml.pw; 


// Material

public class Material extends AppearNode
{
  public SFFloat ambientIntensity, shininess, transparency;
  public SFColor diffuseColor, emissiveColor, specularColor;

  public String nodeName ()
  {
    return NodeNames.NODE_MATERIAL;
  }

  public void traverse (Traverser t)
  {
    t.tMaterial (this);
  }

  Material ()
  {
    addField ("ambientIntensity", ambientIntensity = new SFFloat (0.2f), Field.F_EXPOSEDFIELD);
    addField ("diffuseColor", diffuseColor = new SFColor (0.8f, 0.8f, 0.8f), Field.F_EXPOSEDFIELD);
    addField ("emissiveColor", emissiveColor = new SFColor (0.0f, 0.0f, 0.0f), Field.F_EXPOSEDFIELD);
    addField ("shininess", shininess = new SFFloat (0.2f), Field.F_EXPOSEDFIELD);
    addField ("specularColor", specularColor = new SFColor (0.0f, 0.0f, 0.0f), Field.F_EXPOSEDFIELD);
    addField ("transparency", transparency = new SFFloat (0.0f), Field.F_EXPOSEDFIELD);
  }
} // Material
