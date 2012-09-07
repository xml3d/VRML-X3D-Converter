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
 * ProtoInstance.java - instance of a PROTO node
 *
 * created: mpichler, 19961001
 * changed: mpichler, 19970108
 *
 * $Id: ProtoInstance.java,v 1.5 1997/05/22 16:11:45 apesen Exp $
 */


package iicm.vrml.pw; 

import java.io.*;
import java.util.*;


/**
 * ProtoInstance - instance of PROTO or EXTERNPROTO node
 * Copyright (c) 1996,97 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.1, changed:  8 Jan 97
 */

public class ProtoInstance extends Node
{
  ProtoNode bap;  // the PROTO node that describes this instance

  // TODO: for traversal of this PROTO instance we need a copy
  // of the children of the PROTO node with the current field values

  public String nodeName ()
  {
    return bap.protoname;
  }

  public void traverse (Traverser t)
  {
    t.tProtoInstance (this);
  }

  ProtoInstance (ProtoNode parent)
  {
    // parent should be non-null
    bap = parent;

    // create a copy of each protofield

    // (subfields = parent.protofields; would overwrite proto fields)
    Hashtable bapfields = parent.protofields;
    Enumeration e = bapfields.keys ();
    while (e.hasMoreElements ())
    {
      String fname = (String) e.nextElement ();
      Field f = (Field) bapfields.get (fname);
      //subfields.put (fname, f.newFieldInstance ());
      // following are modified 4/6/2000 by wang. 
      if (f.getFieldClass() == Field.F_EVENTIN || 
	  f.getFieldClass() == Field.F_EVENTOUT)
	subfields.put (fname, f);
      else {
	subfields.put (fname, f.newFieldInstance ());
      }

      //subfields.put (fname, f);
    }
  }

  public ProtoNode getNode() {
    return bap;
  }

  // the current implementation allows reading the fields
  // and writes them back to the output (writeNode works)

  // for efficient traversal of the nodes,
  // a copy of the children subtree will have to be made,
  // where protofields (protoISfield refers to bap's field)
  // are substituted by the field values of this instance
  
  public void writeX3dNode(VRMLparser parser, PrintStream os, Hashtable writtenrefs, int depth) {

    tab(os, depth);

// os.print("<!-- ... starting writeX3dNode() -->\n");

if (nodeName().startsWith("Geo"))  // Treat GeoSpatial prototypes as native nodes
{    
    if (nodeName().compareTo("GeoInline")==0)  // GeoInline becomes X3D Inline
    {    
    	os.print("<Inline");
    }
    else os.print("<"+nodeName());

    if (objname != null) 
    {
      if (((Node) writtenrefs.get (objname)) != this)  // first time
      {
	os.print(" DEF=\""+objname+"\"");
        //writtenrefs.put (objname, this);
      }
      else  // already written this instance
      {
	os.print(" USE=\""+objname+"\"/>\n");
        return;
      }
    }

    Enumeration e = subfields.keys ();
    while (e.hasMoreElements ())
    {
      String fname = (String) e.nextElement ();
      Field f = (Field) subfields.get (fname);
      
      if ((f instanceof SFNode) || (f instanceof MFNode))
      {
	// skip child node(s) until after attributes written
      }
      else {
	if (f.wasChanged() && ((f.getFieldClass() & Field.F_FIELD) != 0))
	  {
	    os.print(" "+fname+"=");
	    if ((f.fieldName()).equals("MFString")==true) 
	      os.print("\'");
	    else
	      os.print("\"");
	    f.writeX3dValue (os, writtenrefs);  // no IS allowed here (?!)	
	    if ((f.fieldName()).equals("MFString")==true) 
	      os.print("\'");
	    else
	      os.print("\"");
	  }
	}
    }
    os.print(">\n");
    
    // node children next -- might need containerField form ???
    e = subfields.keys ();
    while (e.hasMoreElements ())
    {
      String fname = (String) e.nextElement ();
      Field f = (Field) subfields.get (fname);
      
      if (f instanceof SFNode) {
	if (f.wasChanged() && ((SFNode) f).node !=null) {
//	  os.print("\n");
//	  tab(os, depth+1);
//	  os.print("<fieldValue name=\""+fname+"\">\n");
	  ((SFNode) f).writeX3dValue(os, writtenrefs, depth+2);
//	  tab(os, depth+1);
//	  os.print("</fieldValue>\n");
	}
      }
      else if (f instanceof MFNode) {
	if (f.wasChanged() && ((MFNode) f).nodes.size()>0) {
	  os.print("\n");
	  tab(os, depth+1);
//	  os.print("<fieldValue name=\""+fname+"\">\n");
	  //tab(os, depth+2);
	  ((MFNode) f).writeX3dValue(os, writtenrefs, depth+2);
//	  tab(os, depth+1);
//	  os.print("</fieldValue>\n");
	}
      }
    }
    tab(os, depth);
    if (nodeName().compareTo("GeoInline")==0)  // GeoInline becomes X3D Inline
    {    
    	os.print("</Inline>\n");
    }
    else os.print("</"+nodeName()+">\n");
}
else // use ProtoInstance form instead of native-node form
{
    os.print("<ProtoInstance name=\""+nodeName()+"\"");

    if (objname != null) 
    {
      if (((Node) writtenrefs.get (objname)) != this)  // first time
      {
	os.print(" DEF=\""+objname+"\"");
        //writtenrefs.put (objname, this);
      }
      else  // already written this instance
      {
	os.print(" USE=\""+objname+"\"/>\n");
        return;
      }
    }

    // need to figure out containerField from parent...
    // os.print("containerField=\""+fname+"\"");

    os.print(">\n");
    Enumeration e = subfields.keys ();
    while (e.hasMoreElements ())
    {
      String fname = (String) e.nextElement ();
      Field f = (Field) subfields.get (fname);
      
      if (f instanceof SFNode) {
	if (f.wasChanged() && ((SFNode) f).node !=null) {
	  tab(os, depth+1);
	  os.print("<fieldValue name=\""+fname+"\">\n");
	  ((SFNode) f).writeX3dValue(os, writtenrefs, depth+2);
	  tab(os, depth+1);
	  os.print("</fieldValue>\n");
	}
      }
      else if (f instanceof MFNode) {
	if (f.wasChanged() && ((MFNode) f).nodes.size()>0) {
	  tab(os, depth+1);
	  os.print("<fieldValue name=\""+fname+"\">\n");
	  ((MFNode) f).writeX3dValue(os, writtenrefs, depth+2);
	  tab(os, depth+1);
	  os.print("</fieldValue>\n");
	}
      }
      else {
	if (f.wasChanged() && ((f.getFieldClass() & Field.F_FIELD) != 0))
	  {
	    tab(os, depth+1);
	    os.print("<fieldValue name=\""+fname+"\"");
	    if ((f.fieldName()).equals("MFString")==true) 
	      os.print(" value=\'");
	    else
	      os.print(" value=\"");
	    f.writeX3dValue (os, writtenrefs);  // no IS allowed here (?!)	
	    if ((f.fieldName()).equals("MFString")==true) 
	      os.print("\'");
	    else
	      os.print("\"");	
	    os.print("/>\n");
	  }
	}
    }


    int isI=0;
    Enumeration e1 = subfields.keys ();
    while (e1.hasMoreElements ())
    {
      String fname = (String) e1.nextElement ();
      Field f = (Field) subfields.get (fname);
      if (f.protoIS) isI++;
    }
    if (isI>0) {
	tab(os, depth+1);
	os.print("<IS>\n");
	e1 = subfields.keys ();
	while (e1.hasMoreElements () )
	    {
		String fname = (String) e1.nextElement ();
		Field f = (Field) subfields.get (fname);
		if (f.protoIS) {
		    tab(os, depth+2);
		    os.print("<connect nodeField=\""+fname+"\" protoField=\""+f.protoISfield.protoISname+"\"/>\n");
		}
	    }
	tab(os, depth+1);
	os.print("</IS>\n");
    }
    tab(os, depth);
    os.print("</ProtoInstance>\n");
}
}
  
  public void writeX3dNode(PrintStream os, Hashtable writtenrefs, int depth) {

    tab(os, depth);

// os.print("<!-- ... starting writeX3dNode() -->\n");

if (nodeName().startsWith("Geo"))  // Treat GeoSpatial prototypes as native nodes
{    
    if (nodeName().compareTo("GeoInline")==0)  // GeoInline becomes X3D Inline
    {    
    	os.print("<Inline");
    }
    else os.print("<"+nodeName());

    if (objname != null) 
    {
      if (((Node) writtenrefs.get (objname)) != this)  // first time
      {
	os.print(" DEF=\""+objname+"\"");
        //writtenrefs.put (objname, this);
      }
      else  // already written this instance
      {
	os.print(" USE=\""+objname+"\"/>\n");
        return;
      }
    }

    Enumeration e = subfields.keys ();
    while (e.hasMoreElements ())
    {
      String fname = (String) e.nextElement ();
      Field f = (Field) subfields.get (fname);
      
      if ((f instanceof SFNode) || (f instanceof MFNode))
      {
	// skip child node(s) until after attributes written
      }
      else {
	if (f.wasChanged() && ((f.getFieldClass() & Field.F_FIELD) != 0))
	  {
	    os.print(" "+fname+"=");
	    if ((f.fieldName()).equals("MFString")==true) 
	      os.print("\'");
	    else
	      os.print("\"");
	    f.writeX3dValue (os, writtenrefs);  // no IS allowed here (?!)	
	    if ((f.fieldName()).equals("MFString")==true) 
	      os.print("\'");
	    else
	      os.print("\"");
	  }
	}
    }
    os.print(">\n");
    
    // node children next -- might need containerField form ???
    e = subfields.keys ();
    while (e.hasMoreElements ())
    {
      String fname = (String) e.nextElement ();
      Field f = (Field) subfields.get (fname);
      
      if (f instanceof SFNode) {
	if (f.wasChanged() && ((SFNode) f).node !=null) {
//	  os.print("\n");
//	  tab(os, depth+1);
//	  os.print("<fieldValue name=\""+fname+"\">\n");
	  ((SFNode) f).writeX3dValue(os, writtenrefs, depth+2);
//	  tab(os, depth+1);
//	  os.print("</fieldValue>\n");
	}
      }
      else if (f instanceof MFNode) {
	if (f.wasChanged() && ((MFNode) f).nodes.size()>0) {
	  os.print("\n");
	  tab(os, depth+1);
//	  os.print("<fieldValue name=\""+fname+"\">\n");
	  //tab(os, depth+2);
	  ((MFNode) f).writeX3dValue(os, writtenrefs, depth+2);
//	  tab(os, depth+1);
//	  os.print("</fieldValue>\n");
	}
      }
    }
    tab(os, depth);
    if (nodeName().compareTo("GeoInline")==0)  // GeoInline becomes X3D Inline
    {    
    	os.print("</Inline>\n");
    }
    else os.print("</"+nodeName()+">\n");
}
else // use ProtoInstance form instead of native-node form
{
    os.print("<ProtoInstance name=\""+nodeName()+"\"");

    if (objname != null) 
    {
      if (((Node) writtenrefs.get (objname)) != this)  // first time
      {
	os.print(" DEF=\""+objname+"\"");
        //writtenrefs.put (objname, this);
      }
      else  // already written this instance
      {
	os.print(" USE=\""+objname+"\"/>\n");
        return;
      }
    }

    os.print(">\n");
    Enumeration e = subfields.keys ();
    while (e.hasMoreElements ())
    {
      String fname = (String) e.nextElement ();
      Field f = (Field) subfields.get (fname);
      
      if (f instanceof SFNode) {
	if (f.wasChanged() && ((SFNode) f).node !=null) {
	  tab(os, depth+1);
	  os.print("<fieldValue name=\""+fname+"\">\n");
	  ((SFNode) f).writeX3dValue(os, writtenrefs, depth+2);
	  tab(os, depth+1);
	  os.print("</fieldValue>\n");
	}
      }
      else if (f instanceof MFNode) {
	if (f.wasChanged() && ((MFNode) f).nodes.size()>0) {
	  tab(os, depth+1);
	  os.print("<fieldValue name=\""+fname+"\">\n");
	  ((MFNode) f).writeX3dValue(os, writtenrefs, depth+2);
	  tab(os, depth+1);
	  os.print("</fieldValue>\n");
	}
      }
      else {
	if (f.wasChanged() && ((f.getFieldClass() & Field.F_FIELD) != 0))
	  {
	    tab(os, depth+1);
	    os.print("<fieldValue name=\""+fname+"\"");
	    if ((f.fieldName()).equals("MFString")==true) 
	      os.print(" value=\'");
	    else
	      os.print(" value=\"");
	    f.writeX3dValue (os, writtenrefs);  // no IS allowed here (?!)	
	    if ((f.fieldName()).equals("MFString")==true) 
	      os.print("\'");
	    else
	      os.print("\"");	
	    os.print("/>\n");
	  }
	}
    }

    int isI=0;
    Enumeration e1 = subfields.keys ();
    while (e1.hasMoreElements ())
    {
      String fname = (String) e1.nextElement ();
      Field f = (Field) subfields.get (fname);
      if (f.protoIS) isI++;
    }
    if (isI>0) {
	tab(os,depth+1);
	os.print("<IS>\n");
	e1 = subfields.keys ();
	while (e1.hasMoreElements () )
	    {
		String fname = (String) e1.nextElement ();
		Field f = (Field) subfields.get (fname);
		tab(os, depth+2);
		if (f.protoIS) 
		    os.print("<connect nodeField=\""+fname+"\" protoField=\""+f.protoISfield.protoISname+"\"/>\n");
	    }
	tab(os, depth+1);
	os.print("</IS>\n");
    }
    tab(os, depth);
    os.print("</ProtoInstance>\n");
}
}  
} // ProtoInstance










