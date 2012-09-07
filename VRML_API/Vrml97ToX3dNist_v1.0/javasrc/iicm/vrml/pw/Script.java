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
 * Script.java
 * Copyright (c) 1996,97 IICM
 *
 * created: krosch, 19960824
 *
 * changed: krosch, 19970115
 * changed: apesen, 19970410
 * changed: mpichler, 19970604
 *
 * $Id: Script.java,v 1.7 1997/06/04 17:07:50 mpichler Exp $
 */


package iicm.vrml.pw; 

import java.io.*;
import java.util.*;


// Script

public class Script extends Common
{
  public MFString url;
  public SFBool directOutput, mustEvaluate;

  public final static String STR_URL = "url";
  public final static String STR_DIRECTOUTPUT = "directOutput";
  public final static String STR_MUSTEVALUATE = "mustEvaluate";


  public String nodeName ()
  {
    return NodeNames.NODE_SCRIPT;
  }

  // also Script fields stored in subfields
  // Hashtable scriptfields = new Hashtable ();

  public void traverse (Traverser t)
  {
    t.tScript (this);
  }

  Script ()
  {
    // ordinary fields
    addField (STR_URL, url = new MFString (), Field.F_EXPOSEDFIELD);
    addField (STR_DIRECTOUTPUT, directOutput = new SFBool (false), Field.F_FIELD);
    addField (STR_MUSTEVALUATE, mustEvaluate = new SFBool (false), Field.F_FIELD);
  }

  /**
   * check for base field name (ordinary fields)
   */

  boolean isOrdinaryField (String fname)
  {
    return (fname.equals (STR_URL) || fname.equals (STR_DIRECTOUTPUT) || fname.equals (STR_MUSTEVALUATE));
  }

  /**
   * read the fields (inside "{" ... "}"): static and field declarations
   */

  void readFields (VRMLparser parser)
  {
    StrTokenizer st = parser.istok;
    ParserOutput pout = parser.pout;

    try
    {
      while (st.skipCommentReturn (true) && !st.eof () && st.nextChar () != '}')
      {
        String fclass = st.readIdentifier ();  // field or event class or static field

        // ordinary fields
        Field field = null;
        if (fclass != null && isOrdinaryField (fclass))
          field = (Field) subfields.get (fclass);
        if (field != null)
        {
          String fname = fclass;

          if (pout.debug_)
            pout.debug ("  - reading field " + fname);

          field.readFieldValue (parser);  // may be inside prototype here
          if (field.readError ())
            pout.error ("field " + fname + " had invalid data" + atCurrLine (st));
          continue;
        }
        // else: field/event declaration

        if (fclass == null || !(fclass.equals (Field.STR_FIELD) || fclass.equals (Field.STR_EVENTIN)
          || fclass.equals (Field.STR_EVENTOUT)))
        {
          pout.error ("Script fields must be declared as one of:\n" +
            Field.STR_FIELD + ", "  + Field.STR_EVENTIN + ", " + Field.STR_EVENTOUT +
            ". Got " + fclass + atCurrLine (st));
          return;  // ignore remaining fields
        }

        st.skipComment ();
        if (st.eof () || st.nextChar () == '}')
        { // '}' not read
          pout.error ("premature end of Script field " + fclass + atCurrLine (st));
          return;
        }
        String ftypename = st.readIdentifier ();  // field type name

        st.skipComment ();
        if (ftypename == null || st.eof () || st.nextChar () == '}')
        { // '}' not read
          pout.error ("premature end of field Script field " + fclass + " " + ftypename + atCurrLine (st));
          return;
        }

        String fidname = st.readIdentifier ();  // field identifier name
        if (fidname == null)
        { pout.error ("field identifier expected for Script field " + fclass + " " + ftypename + atCurrLine (st));
          return;
        }

        if (subfields.get (fidname) != null)  // non-unique: error (override)
          pout.error ("Script field " + fidname + " not unique within current SCRIPT" + atCurrLine (st));

        if (pout.debug_)
          pout.debug ("  . reading Script field " + fidname);

        field = FieldNames.createFieldFromName (ftypename);
        if (field != null)
        {
          // scriptfields.put (fidname, field);  // register this field/eventIn/eventOut
          // added to subfields to allow uniform event ROUTEs

          // not only fields, also events may have an IS clause inside PROTO nodes
          //boolean if_is = field.readISdeclaration (parser);

	  //fieldIScontent=ref+"."+fidname;
	  //fieldIScontent = objname+"."+fidname;
	  boolean if_is = field.readISdeclaration (parser);
	  if (if_is) {
	    field.protoIS = true;
	    fieldIScontent = objname+"."+fidname;
	    field.setIScontent(fieldIScontent);
	  }

          if (fclass.equals (Field.STR_FIELD))
          {
            addField (fidname, field, Field.F_FIELD);
            // field.setFieldClass (Field.F_FIELD);
            // read default field value
	    if (if_is != true) {
	      field.readValue (parser);
	      if (field.readError ())
		pout.error ("Script field " + fidname + " had invalid data" + atCurrLine (st));
	    }
          }
	  else if (fclass.equals (Field.STR_EVENTIN)) 
            addField (fidname, field, Field.F_EVENTIN);  // field.setFieldClass (Field.F_EVENTIN);
	  
          else if (fclass.equals (Field.STR_EVENTOUT)) 
            addField (fidname, field, Field.F_EVENTOUT);  // field.setFieldClass (Field.F_EVENTOUT);
          else
            pout.error ("Internal error. Field classification " + fclass + " not valid");
	}
        else
          pout.error ("invalid field type name " + ftypename + " (no such type)" + atCurrLine (st));
      } // until "}" or EOF
    }
    catch (IOException e)
    {
      pout.error ("IOException on reading Script definitions " + atCurrLine (st));
      return;
    }

    // '}' was not read from stream

  } // readFields

  /**
   * writeSubfield: output value of fields
   */

  public void writeSubfield (String fname, Field f, PrintStream os, Hashtable writtenrefs)
  {
    // ordinary field
    if (isOrdinaryField (fname))
      super.writeSubfield (fname, f, os, writtenrefs);
    else  // declared script field/event
    {
      os.print ("\t" + f.getFieldClassName () + " " + f.fieldName () + " " + fname + " ");

      if (f.protoISfield != null) f.writeFieldValue(os, writtenrefs); //added by Wang on 04/06/2000
      else 
      if ((f.getFieldClass() & Field.F_FIELD) != 0)  // output value for fields only
        f.writeFieldValue (os, writtenrefs);
      os.print ("\n");
    }
  }

  public void writeX3dNode (PrintStream os, Hashtable writtenrefs, int depth)
  {
 //os.print("<!-- Script parser -->\n");

    tab(os, depth);
    os.print("<Script ");
    if (objname != null) {
      if (((Node) writtenrefs.get(objname)) != this) {
	os.print("DEF=\""+objname+"\"");
	//writtenrefs.put(objname, this);
      }
      else {
	os.print(" USE=\""+objname+"\"\n");
	return;
      }
    }

    Field f1 = (Field) subfields.get("directOutput");
    if (f1.wasChanged()) {
      os.print(" directOutput=\"");
      ((SFBool) f1).writeX3dValue(os,writtenrefs);
      os.print("\"");
    }

    f1 = (Field) subfields.get("mustEvaluate");
    if (f1.wasChanged()) {
      os.print(" mustEvaluate=\"");
      ((SFBool) f1).writeX3dValue(os,writtenrefs);
      os.print("\"");
    }
    
    f1 = (Field) subfields.get("url");
    int num = (((MFString) f1).values).getCount(); 
    String[] vals = (((MFString) f1).values).getData();
	  
    if (num==1 && (vals[0].startsWith("javascript:") || 
		   vals[0].startsWith("vrmlscript:"))) 
      {
	os.print(">\n");
	// print CDATA after field definitions, code block below
      }
    else  {
      os.print(" url=\"");
      for (int i=0; i<num; i++) {
	os.print("&quot;");
	for (int j=0; j<vals[i].length(); j++) {
	  if (vals[i].charAt(j) == ('\n'))
	    os.print("&#10;");
	  else
	    os.print(vals[i].charAt(j));
	}
	os.print("&quot;");
      }
      os.print("\">\n");
    }

    int isI=0;
    for ( Enumeration e = subfields.keys(); e.hasMoreElements ();)
      {
	String fname = (String) e.nextElement();
	Field f = (Field) subfields.get(fname);    
	
	if (f.protoIS) isI++;
	if (fname.equals("url") || fname.equals("directOutput") ||
	    fname.equals("mustEvaluate")) 
	  continue;
	else {
	  tab(os,depth+1);
	  os.print("<field name=\""+fname+"\""+" type=\""+
	     f.fieldNameToX3d(f.fieldName())+"\"");
	  if ((f.getFieldClass() & Field.F_FIELD) != 0) {
	    if (f instanceof SFNode) {
	      // assume no DEF in fields of Script nodes	
	      if ((((SFNode) f).node).objname != null) {		
		  os.print(" accessType=\""+f.getFieldClassName()+"\">\n");
		  Node node=NodeNames.createInstanceFromName((((SFNode) f).node).nodeName());
		  if (node != null) {
		      tab(os, depth+2);
		      os.print("<"+(((SFNode) f).node).nodeName()+" USE=\""+(((SFNode) f).node).objname+"\"/>\n");
		  }
		  else {
		      tab(os, depth+1);
		      os.print("<ProtoInstance name=\""+(((SFNode) f).node).nodeName()+"\" USE=\""+(((SFNode) f).node).objname+"\"/>\n");
		  }
		  tab(os, depth+1);
		  os.print("</field>\n");
	      }
	      else {
		os.print(" accessType=\""+f.getFieldClassName()+"\">\n");
		((SFNode) f).writeX3dValue(os, writtenrefs, depth+2);
		tab(os, depth+1);
		os.print("</field>\n");
	      }
	    }
	    else if (f instanceof MFNode) {
	      // TODO USE 	
	      os.print(" accessType=\""+f.getFieldClassName()+"\">\n");
	      ((MFNode) f).writeX3dValue(os, writtenrefs, depth+2);
	      tab(os, depth+1);
	      os.print("</field>\n");
	    }
	    else {
	      if ((f.fieldName()).equals("MFString")==true) 
		os.print(" value=\'");
	      else
		os.print(" value=\"");
	      f.writeX3dValue (os, writtenrefs);  	       	
	      
	      if ((f.fieldName()).equals("MFString")==true) 
		os.print("\'");
	      else
		os.print("\"");	    
	      os.print(" accessType=\""+f.getFieldClassName()+"\"/>\n");
	    }
	  }
	  else {
	    os.print(" accessType=\""+f.getFieldClassName()+"\"/>\n");
	  }
	}
      }
    if (isI>0) {
	tab(os, depth+1);
	os.print("<IS>\n");
	for ( Enumeration e = subfields.keys(); e.hasMoreElements ();)
	    {
		String fname = (String) e.nextElement();
		Field f = (Field) subfields.get(fname); 
		if (f.protoIS) {
		    tab(os, depth+1);
		    os.print("<connect nodeField=\""+fname+"\" protoField=\""+f.protoISfield.protoISname+"\"/>\n");
		}
	    }
	tab(os, depth+1);
	os.print("</IS>\n");
    }

    if (num==1 && (vals[0].startsWith("javascript:") ||
		   vals[0].startsWith("vrmlscript:")))
      {
	// print CDATA after field definitions
	int index = vals[0].indexOf("vrmlscript:");
	if (index != -1) {
	  String s = vals[0].substring(index+11);
	  vals[0]="javascript:"+s;
	}
	tab(os,depth+1);
	os.print("<![CDATA[");
	os.print(vals[0]+"\n");
	tab(os,depth+1);
	os.print("]]>\n");
      }
			
    tab(os, depth);
    os.print("</Script>\n");
  }
} // Script  







