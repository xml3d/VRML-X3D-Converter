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
 * ProtoNode.java - PROTO definition
 *
 * created: mpichler, 19960926
 *
 * changed: mpichler, 19970108
 * changed: apesen, 19970411
 *
 * $Id: ProtoNode.java,v 1.6 1997/05/22 16:11:00 apesen Exp $
 */


package iicm.vrml.pw; 

import java.io.*;
import java.util.*;


/**
 * ProtoNode - PROTO or EXTERNPROTO node definition
 * Copyright (c) 1996,97 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.1, changed:  8 Jan 97
 */


public class ProtoNode extends GroupNode
{
  // constants

  String protoname;  // node name
  // inherits children
  boolean readingprotofields;
  public boolean external;  // EXTERNPROTO
  public MFString urls_;  // external only


  /** prototyped fields */
  Hashtable protofields = new Hashtable ();
  // will have extra ones for events

  public String nodeName ()
  {
    return (external ? Node.EXTERNPROTO_KEYWORD : Node.PROTO_KEYWORD);
  }

  public void traverse (Traverser t)
  {
    t.tProtoNode (this);
  }

  ProtoNode (String name, boolean extern)
  {
    // ass.: name non-null
    protoname = name;
    external = extern;

    if (extern)
      urls_ = new MFString ();
  }


  /**
   * read the field declarations (within "[" ... "]")
   */

  void readProtoBody (VRMLparser parser)
  {
    readingprotofields = true;
    readNodeBody (parser, protoname, '[', ']');
    readingprotofields = false;
  }


  /**
   * called both from readProtoBody ([]) and readNodeBody ({})
   */

  void readFields (VRMLparser parser)
  {
    if (readingprotofields) { // read the proto fields (declarations)
      readProtoFields (parser);
    }
    else  // any nodes in PROTO body
    {
      // also on nested protos only the innermost proto (this)
      // can provide fields to use; see Field.readFieldValue
      ProtoNode oldproto = parser.curproto;
      parser.curproto = this;
      readNodes (parser, true);  // until "}"
      parser.curproto = oldproto;
    }
  }

  /**
   * body of EXTERNPROTO: SFString URLs
   */

  void readProtoURLs (VRMLparser parser)
  {
    try
    {
      urls_.readValue (parser);
    }
    catch (IOException e)
    {
      parser.pout.error ("[ProtoNode] [Error] IOException on reading URL of EXTERNPROTO" + Node.atCurrLine (parser.istok));
      return;
    }
  }

  /**
   * read the prototyped fields (inside "[" ... "]")
   */

  void readProtoFields (VRMLparser parser)
  {
    StrTokenizer st = parser.istok;
    ParserOutput pout = parser.pout;

    try
    {
      while (st.skipCommentReturn (true) && !st.eof () && st.nextChar () != ']')
      {
        String fclass = st.readIdentifier ();  // field or event class

        if (fclass == null || !(fclass.equals (Field.STR_FIELD) || fclass.equals (Field.STR_EXPOSEDFIELD)
          || fclass.equals (Field.STR_EVENTIN) || fclass.equals (Field.STR_EVENTOUT)))
        {
          pout.error (nodeName () + " fields must be declared as one of:\n" +
            Field.STR_FIELD + ", " + Field.STR_EXPOSEDFIELD + ", " +
            Field.STR_EVENTIN + ", " + Field.STR_EVENTOUT + ". Got " + fclass + atCurrLine (st));
          return;
        }

        String ftypename = st.readIdentifier ();  // field type name

        if (ftypename == null)
        { pout.error ("[ProtoNode] [Error] field type expected for prototype " + fclass + atCurrLine (st));
          return;
        }

        String fidname = st.readIdentifier ();  // field identifier name

        if (fidname == null)
        { pout.error ("[ProtoNode] [Error] field identifier expected in field prototype " + fclass + " " + ftypename + atCurrLine (st));
          return;
        }

        if (protofields.get (fidname) != null)  // non-unique: error (override)
          pout.error ("[ProtoNode] [Error] proto field " + fidname + " not unique within current " + nodeName () + atCurrLine (st));

        if (pout.debug_)
          pout.debug ("  . reading proto field " + fidname);

        // read default field value
        Field field = FieldNames.createFieldFromName (ftypename);

        if (field != null)
        {
          protofields.put (fidname, field);  // register this field/exposedField/eventIn/eventOut

          //  field.setFieldClass; read default field value where appropriate
          if (fclass.equals (Field.STR_FIELD) || fclass.equals (Field.STR_EXPOSEDFIELD))
          {
            if (fclass.equals (Field.STR_FIELD))
              field.setFieldClass (Field.F_FIELD);
            else
              field.setFieldClass (Field.F_EXPOSEDFIELD);
            if (!external)                   // no default field value for EXTERNPROTO
            {
              field.readValue (parser);      // read its default value; no IS allowed here (?!)
              if (field.readError ())
                pout.error ("[ProtoNode] [Error] PROTO field " + fidname + " had invalid data" + atCurrLine (st));
            }
          }
          else if (fclass.equals (Field.STR_EVENTIN)) 
            field.setFieldClass (Field.F_EVENTIN);
          else if (fclass.equals (Field.STR_EVENTOUT)) 
            field.setFieldClass (Field.F_EVENTOUT);
          else
            pout.error ("[ProtoNode] [Error] Internal error. Field classification " + fclass + " not valid");
        }
        else
          pout.error ("[ProtoNode] [Error] invalid field name " + ftypename + " (no such type)" + atCurrLine (st));

      } // until "]" or EOF
    }
    catch (IOException e)
    {
      pout.error ("[ProtoNode] [Error] IOException on reading proto definitions " + atCurrLine (st));
      return;
    }

    // ']' was not read from stream

  } // readProtoFields


  /**
   * get the proto field that IS associated with a name
   */

  Field getProtoISfield (String name)
  {
    if (name == null)
      return null;
    return (Field) protofields.get (name);
  }


  /**
   * write the prototype definition
   */

  public void writeNode (PrintStream os, Hashtable writtenrefs)
  {
    // TODO: I think PROTOs may be DEF'd (what for?)

    os.print (nodeName () + " " + protoname+ "\n");
    os.print("[\n");
    Enumeration e = protofields.keys ();
    while (e.hasMoreElements ())  // proto fields
    {
      String fname = (String) e.nextElement ();

      Field f = (Field) protofields.get (fname);
      os.print ("\t" + f.getFieldClassName () + " " + f.fieldName () + " " + fname);

      if (!external && ((f.getFieldClass() & Field.F_FIELD) != 0))  // no default value on EXTERNPROTO
      { // and only for fields/exposedFields
        os.print (" ");
        f.writeValue (os, writtenrefs);  // no IS allowed here (?!)
      }
      os.print ("\n");
    }
    os.print ("]\n");
    if (external)
    {
      urls_.writeValue (os, writtenrefs);
      os.print ("\n");
    }
    else
    {
      os.print ("{\n");
      // writeNodes starts a separate name scope (empty writtenrefs) for PROTO children
      writeNodes (os);  // children
      os.print ("}\n");
    }
  }

  public void writeX3dNode (VRMLparser parser,PrintStream os, Hashtable writtenrefs, int depth)
  //  public void writeX3dNode (PrintStream os, Hashtable writtenrefs, int depth)
  {
    if (external && protoname.startsWith("Geo")) {
    	// ignore GeoVrml protos since they are native nodes in X3D GeoSpatial Profile
    	return;
    }

    tab(os, depth); 
    if (external) {
      os.print("<ExternProtoDeclare name=\""+protoname+"\"  url='");
      String[] tmpurls = urls_.getValueCopy();
      int num = urls_.getValueCount();
      for (int i=0; i<num; i++) {
	  int j = tmpurls[i].indexOf(".wrl");
	  String tmp = tmpurls[i].substring(0,j);
	  String tmp1 = tmp+".x3d"+tmpurls[i].substring(j+4);
	  tmpurls[i]=tmp1;
      }
      MFString x3durls = new MFString(tmpurls);
      x3durls.writeX3dValue(os, writtenrefs);
      os.print("'>\n");
    }
    else
      os.print("<ProtoDeclare name=\""+protoname+"\">\n");
  
    Enumeration e = protofields.keys ();
 
    String is_e[] = new String[protofields.size()];
    int isI=0;
    if (e!=null) {
	if (!external) {
	    tab(os, depth+1);
	    os.print("<ProtoInterface>\n");
	}
	while (e.hasMoreElements ())  // proto fields
	    {
		String fname = (String) e.nextElement ();
		Field f = (Field) protofields.get (fname);
		
		tab(os, depth+2); 
		os.print("<field ");
      
		os.print(" name=\""+fname+"\" type=\""+f.fieldNameToX3d(f.fieldName())+"\"");
		if ((f.getFieldClass() & Field.F_FIELD) != 0) {
		    if (!external) {
			if (f instanceof SFNode) {
			    os.print(" accessType=\""+f.getFieldClassName()+"\">\n");
			    ((SFNode) f).writeX3dValue(os, writtenrefs, depth+2);
			    tab(os, depth+2);
			    os.print("</field>\n");
			}
			else if (f instanceof MFNode) {
			    os.print(" accessType=\""+f.getFieldClassName()+"\">\n");
			    ((MFNode) f).writeX3dValue(os, writtenrefs, depth+2);
			    tab(os, depth+2);
			    os.print("</field>\n");
			}
			else {
			    if ((f.fieldName()).equals("MFString")==true) 
				os.print(" value=\'");
			    else
				os.print(" value=\"");
			    f.writeX3dValue (os, writtenrefs);  // no IS allowed here (?!)
			    if ((f.fieldName()).equals("MFString")==true) 
				os.print("\'");
			    else
				os.print("\"");	    
			    os.print(" accessType=\""+f.getFieldClassName()+"\"/>\n");
			}
		    }
		    else
			os.print(" accessType=\""+f.getFieldClassName()+"\"/>\n"); 
		}
		else
		    os.print(" accessType=\""+f.getFieldClassName()+"\"/>\n"); 
	    }

    e = protofields.keys ();
    if (e.hasMoreElements ()) // field declarations present
    {
	if (!external) {
	    tab(os, depth+1); 
	    os.print("</ProtoInterface>\n");
	}
    }
  
    if (!external) 
    {
      tab(os, depth+1); 
      os.print("<ProtoBody>\n");
      writeX3dNodes (parser, os, depth+4);  // children
      tab(os, depth+1); 
      os.print("</ProtoBody>\n");
    }

    tab(os, depth);
    if (external) 
      os.print("</ExternProtoDeclare>\n");
    else    
      os.print("</ProtoDeclare>\n");
    }
  }
} // ProtoNode




