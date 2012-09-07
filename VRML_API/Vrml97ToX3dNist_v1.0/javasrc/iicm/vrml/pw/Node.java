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
 * Node.java
 * base class for graph node
 *
 * created: mpichler, 19960724
 *
 * changed: krosch, 19960831
 * changed: apesen, 19970506
 * changed: mpichler, 19970917
 * changed: kwagen, 19970917
 *
 * $Id: Node.java,v 1.13 1997/09/24 12:16:51 mpichler Exp $
 */


package iicm.vrml.pw;

import java.io.*;
import java.util.*;


/**
 * Node - Node base class
 * Copyright (c) 1996,97 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.5, latest change: 31 Jan 97
 */


abstract public class Node extends Object
{
  // constants

  public final static String DEF_KEYWORD = "DEF";
  public final static String USE_KEYWORD = "USE";
  public final static String PROTO_KEYWORD = "PROTO";
  public final static String EXTERNPROTO_KEYWORD = "EXTERNPROTO";
  public final static String ROUTE_KEYWORD = "ROUTE";
  public final static String SCRIPT_KEYWORD = "Script";

  /** this allows for storage of user data */
  public Object userdata;

  public static String fieldIScontent; //for x3d IS="node.attri"
  // node names see NodeNames interface
  public static String newISname="_IS_";
  public static int newISnum=0;

  // variables

  /** fields of the node */
  public Hashtable subfields = new Hashtable ();

  /** instance name for DEF/USE */
  public String objname = null;

  // methods

  /**
   * the node's name
   */

  abstract public String nodeName ();


  /**
   * must call appropriate method of Traverser for node traversal
   */

  abstract public void traverse (Traverser t);


  /**
   * traverse a node, if it is non-null
   */

  public static void traverseNode (Traverser t, Node node)
  {
    if (node != null)
      node.traverse (t);
  }


  /**
   * helper for error messages: " at line NN" (current line no.)
   */

  static String atCurrLine (StrTokenizer st)
  {
    // now also header read via StrTokenizer
    if (st == null)
      return "";
    return (" at line " + st.lineno ());
  }


//   // add a (plain) field. deprecated. field class should be stated.
//   void addField (String name, Field field)
//   {
//     subfields.put (name, field);
//   }


  /**
   * add an (exposed) field or event
   */

  void addField (String name, Field field, int fclass)
  {
    field.setFieldClass (fclass);
    subfields.put (name, field);
  }


  /**
   * get an event field of appropriate field class (Field.F_EVENTIN/OUT)
   */

  public Field getEvent (String name, int fclass)
  {
    // a) try exact match
    Field field = (Field) subfields.get (name);
    if (field != null && (field.getFieldClass () & fclass) != 0)
    {
      // System.out.println("found exact: " + name );
      return field;
    }

    // b) complete route name if necessary (set_/_changed optional in ROUTE statement)
    if ((fclass & Field.F_EVENTIN) != 0)  // try set_name
    {
      field = (Field) subfields.get ("set_" + name);
      // System.out.println("try EVENTIN : set_" + name);
    }
    else if ((fclass & Field.F_EVENTOUT) != 0)  // try name_changed
    {
      field = (Field) subfields.get (name + "_changed");
      // System.out.println("try EVENTOUT: " + name + "_changed");
    }
    if (field != null && (field.getFieldClass () & fclass) != 0)
      return field;

    // c) complete field name if necessary (ExposedFields have implicit set_/_changed event names)
    if ((fclass & Field.F_EVENTIN) != 0 && name.startsWith ("set_"))
    {
      field = (Field) subfields.get (name.substring (4));
      // System.out.println("try EXPOSED: " + name.substring (4));
    }
    else if ((fclass & Field.F_EVENTOUT) != 0 && name.endsWith ("_changed"))
    {
      field = (Field) subfields.get (name.substring (0, name.length () - 8));
      // System.out.println("try EXPOSED: " + name.substring (0, name.length () - 8) );
    }
    if (field != null && (field.getFieldClass () & fclass) != 0)
      return field;

    return null;  // no appropriate found
  } // getEvent

  /**
   * delete route
   * @return true on success, otherwise false
   */

  public static boolean deleteRoute (Node fromNode, String fromEvent, Node toNode, String toEvent)
  {
    Field eventOut = fromNode.getEvent (fromEvent, Field.F_EVENTOUT);
    Field eventIn = toNode.getEvent (toEvent, Field.F_EVENTIN);

    if (fromNode == null || toNode == null)
      return false;

    if (eventOut == null || eventIn == null)
      return false;
    
    if (eventOut.removeReceiver (eventIn))
      return true;
    else
      return false;
  } // deleteRoute

  /**
   * add route
   * @return RouteNode when mkroute flag is set, otherwise any non-null node on success
   */

  public static Node addRoute (
    String fromNodeName, Node fromNode, String fromEvent,
    String toNodeName, Node toNode, String toEvent,
    ParserOutput pout, StrTokenizer st,  // st for debugging only (may be null)
    boolean mkroute)
  {
    if (fromNode == null || toNode == null)
    {
      if (mkroute)
        pout.error ("[Node] [Error] ROUTE: nodenames (" + fromNodeName + ", " + toNodeName +
          ") were not resolveable" + atCurrLine (st));
      return null;
    }

    Field eventOut = fromNode.getEvent (fromEvent, Field.F_EVENTOUT);
    Field eventIn = toNode.getEvent (toEvent, Field.F_EVENTIN);

    if (eventOut == null || eventIn == null)
    {
      if (eventOut == null && mkroute)           // eventOut not found 
        pout.error ("[Node] [Error] ROUTE: Field (" + fromEvent + ") is not an eventOut" + atCurrLine (st));
      if (eventIn == null && mkroute)            // eventIn not found
        pout.error ("[Node] [Error] ROUTE: Field (" + toEvent + ") is not an eventIn" + atCurrLine (st));
      return null;
    }

    if (eventOut.fieldName () != eventIn.fieldName ())  // check if same type (e.g. SFFloat)
    {
      if (mkroute)
        pout.error ("[Node] [Error] ROUTE: Fields (" + fromEvent + ", " + toEvent +     ") do not have same type (" + eventOut.fieldName () + ", " + eventIn.fieldName () + ")" +
          atCurrLine (st));
      return null;
    }

    if (mkroute && pout.debug_)
      pout.debug ("ROUTE " + fromNodeName + "." + fromEvent +
        " TO " + toNodeName + "." + toEvent);

    // establish ROUTE
    if (eventOut.routeExists (eventIn))
    {
      if (mkroute)
        pout.warning ("ROUTE " + fromNodeName + "." + fromEvent +
          " TO " + toNodeName + "." + toEvent + " already exists (ignored).");
    }
    else
      eventOut.addReceiver (eventIn);

    if (mkroute)
      return new RouteNode (fromNode, toNode, fromEvent, toEvent);

    return fromNode;  // is non-null on success
  } // addRoute

  /**
   * read a node instance
   */

  public static Node readNode (VRMLparser parser)
  {
    StrTokenizer st = parser.istok;
    ParserOutput pout = parser.pout;

    // when exceptions are not handled here they will propagate to root level
    // and destory the whole scene graph
    String name;
    try
    {
      name = st.readIdentifier ();

      if (name == null)
      { pout.error ("[Node] [Error] node name expected" + atCurrLine (st));
        st.readChar ();  // prevent infinite loop
        return null;
      }
    }
    catch (IOException e)
    { pout.error ("[Node] [Error] IOException during parsing" + atCurrLine (st));
      return null;
    }

    // now we know the node name and unless it is one of the
    // DEF/USE/PROTO/ROUTE keywords we can call readNodeBody

    if (name.equals (DEF_KEYWORD))  // DEF
    {
      String refname;
      
      try
      {
        refname = st.readIdentifier ();
        name = st.readIdentifier ();  // the real node name

        if (refname == null || name == null)
        { pout.error ("[Node] [Error] reference name and node name expected after DEF" + atCurrLine (st));
          st.readChar ();
          return null;
        }
	if (refname.length()>=4 && (refname.substring(4)).equals("_IS_")==true)
	  { pout.error ("[Node] [Error] reference name starts with _IS_, it may duplicate with other names");
	  }
      }
      catch (IOException e)
      { pout.error ("[Node] [Error] IOException during parsing" + atCurrLine (st));
        return null;
      }

      Node node = createInstanceFromName (parser, name);
      if (node == null)
        return null;

      node.objname = refname;
      node.readNodeBody (parser, name);

      if (pout.debug_)
        pout.debug ("* node " + name + " is assigned reference name " + refname);

      //node.objname = refname;
      parser.addReference (refname, node);

      return node;
    } // DEF
    else if (name.equals (USE_KEYWORD))  // USE
    {
      String refname;

      try
      {
        refname = st.readIdentifier ();
        if (refname == null)
        { pout.error ("[Node] [Error] reference name expected after USE" + atCurrLine (st));
          st.readChar ();
          return null;
        }
      }
      catch (IOException e)
      { pout.error ("[Node] [Error] IOException during parsing" + atCurrLine (st));
        return null;
      }

      Node node = parser.getReference (refname);

      if (node == null)
        pout.error ("[Node] [Error] unresolveable reference USE " + refname + atCurrLine (st));
      else if (pout.debug_)
        pout.debug ("* node reference USE " + refname + " found");

      return node;
    } // USE
    else if (name.equals (PROTO_KEYWORD) || name.equals (EXTERNPROTO_KEYWORD))  // [EXTERN]PROTO
    {
      String whichproto = name;
      boolean external = name.equals (EXTERNPROTO_KEYWORD);
      String protoname;

      try
      {
        protoname = st.readIdentifier ();

        if (protoname == null)
        { pout.error ("[Node] [Error] identifier expected after " + whichproto + " keyword" + atCurrLine (st));
          return null;
        }
      }
      catch (IOException e)
      { pout.error ("[Node] [Error] IOException during parsing" + atCurrLine (st));
        return null;
      }

//    System.out.println ("... reading " + whichproto + " " + protoname);

      if (parser.getProtoNode (protoname) != null)
      {
        pout.error ("[Node] [Error] PROTO/EXTERNPROTO named " + protoname + " already existed.");
        parser.addProtoNode (protoname, null);  // proto must not refer to its own name
      }

//    System.out.println ("... now ProtoNode node = new ProtoNode (protoname, external);");

      ProtoNode node = new ProtoNode (protoname, external);

      if (pout.debug_)
        pout.debug ("* reading " + whichproto + " " + protoname
        + " (you\'ll get " + (external ? "1" : "2") + " more debug message" + (external ? "" : "s") + ")");

//    System.out.println ("... now node.readProtoBody (parser);");

      node.readProtoBody (parser);  // "[" to "]"

      if (external)  // URLs (no curly brackets)
        node.readProtoURLs (parser);
      else
        node.readNodeBody (parser, protoname);  // "{" to "}"

//    System.out.println ("... now parser.addProtoNode");

      parser.addProtoNode (protoname, node);  // register prototype

//    System.out.println ("... completed reading " + whichproto + " " + protoname);

      return node;
    } // [EXTERN]PROTO
    else if (name.equals (ROUTE_KEYWORD))  // ROUTE
    {
      String fromNodeName = null, toNodeName = null;
      String fromEvent = null, toEvent = null;
      // ROUTEs may occur where nodes or fields are (TODO)

      // syntax: ROUTE node.out TO node.in
      try
      {
        fromNodeName = st.readIdentifier ();  // from node
        if (fromNodeName != null && st.nextChar () == '.')
        { st.readChar ();  // '.'
          fromEvent = st.readIdentifier ();   // from event
        }
        if (fromEvent != null)
          toNodeName = st.readIdentifier ();  // TO
        if (toNodeName != null && toNodeName.equals ("TO"))
          toNodeName = st.readIdentifier ();  // to node
        else
          toNodeName = null;
        if (toNodeName != null && st.nextChar () == '.')
        { st.readChar ();  // '.'
          toEvent = st.readIdentifier ();     // to event
        }
        if (toEvent == null)
        { pout.error ("[Node] [Error] error in ROUTE syntax (ROUTE node.out TO node.in)" + atCurrLine (st));
          return null;
        }
        // here everyting non-null
      }
      catch (IOException e)
      { pout.error ("[Node] [Error] IOException during parsing" + atCurrLine (st));
        return null;
      }

      // find node references
      Node fromNode = parser.getReference (fromNodeName);
      Node toNode = parser.getReference (toNodeName);

      return addRoute (fromNodeName, fromNode, fromEvent, toNodeName, toNode, toEvent, pout, st, true);

    } // ROUTE

    Node node = createInstanceFromName (parser, name);

    // TODO: may wish to skip matching "{" ... "}" pair on invalid node names
    if (node != null) {
      node.readNodeBody (parser, name);
    }

    return node;

  } // readNode


  /**
   * create node instance from its name
   */

  static Node createInstanceFromName (VRMLparser parser, String name)
  {
    Node node = NodeNames.createInstanceFromName (name);  // try basic nodes
    if (node != null)
      return node;

    ProtoNode pnode = parser.getProtoNode (name);  // try prototyped nodes
    if (pnode != null)
    {
      if (parser.pout.debug_)
        parser.pout.debug ("* create instance of PROTO " + name);
      return new ProtoInstance (pnode);
    }

    // might search dynamically for appropriate nodes

    parser.pout.error ("[Node] [Error] class '" + name + "' not found");
    return null;
  } // createInstanceFromName


  /**
   * read node body (from "{" to "}") of node name
   */

  void readNodeBody (VRMLparser parser, String name)
  {
    readNodeBody (parser, name, '{', '}');
  }

  /**
   * check for openbrace, readFields, read closebrace
   */

  void readNodeBody (VRMLparser parser, String name, char openbrace, char closebrace)
  {
    StrTokenizer st = parser.istok;
    ParserOutput pout = parser.pout;

    if (pout.debug_)
      pout.debug ("* reading node " + name);

    try
    {
      char t;
      t = (char) st.readChar();
      if ( t != openbrace) 
	//if ((t = (char) st.readChar ()) != openbrace)
      { pout.error ("[Node] [Error] openbrace" + " expected" + atCurrLine (st));
        return;
      }

    }
    catch (IOException e)
    { pout.error ("[Node] [Error] IOException during parsing" + atCurrLine (st));
      return;
    }

    // createInstanceFromName was already called (this is the instance)

    readFields (parser);  // read fields

    // read final "}" of node
    // TODO: check matching parenthesis (scan over subgroups) on parse error
    // (strings checked by StrTokenizer)
    try
    {
      int c = st.readChar ();
      char t = (char) c;

      while (c != closebrace)
      {
        // System.out.println ("after readFields. got character: " + (char) c + " (" + c + ")");
        if (c == -1)
        { pout.error ("[Node] [Error] premature EOF on parsing node " + name + atCurrLine (st));
          break;
        }
        c = st.readChar ();
      }
    }
    catch (IOException e)
    {
      pout.error ("[Node] [Error] IOException during parsing" + atCurrLine (st));
      // this node should be complete anyway
    }

  } // readNodeBody


  /**
   * read fields of this node instance (node body)
   */

  void readFields (VRMLparser parser)
  {
    // "fields" (field declaration) in VRML 1.0 only (not supported)
    StrTokenizer st = parser.istok;
    ParserOutput pout = parser.pout;
    try
    {
      st.skipComment ();
    }
    catch (IOException e)
    { pout.error ("[Node] [Error] IOException on reading fields " + atCurrLine (st));
      return;
    }

    while (!st.eof () && st.nextChar () != '}')  // ass.: comment was skipped before
    {
      String fname;
      try
      {
        fname = st.readIdentifier ();
        if (fname == null)
        { pout.error ("[Node] [Error] field name expected" + atCurrLine (st) +
            "\nignoring remaining fields of node " + nodeName ());
          return;
        }
      }
      catch (IOException e)
      { pout.error ("[Node] [Error] IOException on reading fields " + atCurrLine (st));
        return;
      }

      // VRML 2.0: also child nodes are only fields
      if (pout.debug_)
        pout.debug ("  - reading field " + fname);

      // TODO: ROUTEs and [EXTERN]PROTOs are allowed anywhere fields are

      Field field = null;
      if (fname != null)
        field = (Field) subfields.get (fname);
      if (field == null)
      { // only fields (ordinary or exposed) read from input; changed for envent IS declarations
        pout.error ("[Node] [Error] invalid field name " + fname + atCurrLine (st) +
          "\nignoring remaining fields of node " + nodeName ());
        return;
      }
      try
      {
        // only fields (ordinary or exposed) read from input
	// fieldIScontent = objname+"."+fname;

	if ((field.getFieldClass () & Field.F_FIELD) != 0) {
 	  if (!field.readISdeclaration(parser)) {
// 	  if (!field.readISdeclaration (parser,fieldIScontent)) {
 	    field.readFieldValue (parser);
 	    if (field.readError ())
 	      pout.error ("[Node] [Error] field " + fname + " had invalid data" + atCurrLine (st));
 	  }
 	  else //{ IS is true
 	    field.protoIS = true; 
	  /*if (objname == null) {
	      objname = newISname+newISnum;
	      newISnum++;
	      }
 	    fieldIScontent = objname+"."+fname;
	    field.setIScontent(fieldIScontent);
	    }*/
	    
	}
      
	
	//else if (!field.readISdeclaration (parser))
	
	/*  
        if ((field.getFieldClass () & Field.F_FIELD) != 0)
          field.readFieldValue (parser);  // may be inside prototype here
        else if (!field.readISdeclaration (parser))  // events are allowed to occur with IS clauses
	 {
	    pout.error ("[Node] [Error] IS expected for event " + fname + " (only field values read from input) " + atCurrLine (st));
	    return; 
	  }

        // TODO: report true line number of error (now end of field)
        if (field.readError ())
	  pout.error ("[Node] [Error] field " + fname + " had invalid data" + atCurrLine (st));
	  */
        st.skipComment ();
      }
      catch (IOException e)
      { pout.error ("[Node] [Error] IOException on or after reading field " + fname + atCurrLine (st));
        return;
      }
    } // until "}" or EOF

    // '}' was not read from stream

  } // readFields


  /**
   * write node to output stream
   */

  public void writeNode (PrintStream os, Hashtable writtenrefs)
  {

    if (objname != null)
    {
      if (((Node) writtenrefs.get (objname)) != this)  // first time
      {
        os.print (DEF_KEYWORD + " " + objname + " ");
        writtenrefs.put (objname, this);
      }
      else  // already written this instance
      {
        os.print (USE_KEYWORD + " " + objname+ "\n");
        return;
      }
    }
    os.print (nodeName () + " {\n");
    writeSubfields (os, writtenrefs);
    os.print ("}\n");

  } // writeNode


  /**
   * write subfields (inside {}) to outputstream
   */

  public void writeSubfields (PrintStream os, Hashtable writtenrefs)
  {
    // this works for all Nodes;
    // specific Nodes (longish ones, esp. geometry nodes) may prefer to
    // output their fields in the "canonical" order

    for (Enumeration e = subfields.keys ();  e.hasMoreElements ();  )
    {
      String fname = (String) e.nextElement ();
      writeSubfield (fname, (Field) subfields.get (fname), os, writtenrefs);
    }

  } // writeSubfields


  /**
   * write one subfield
   */

  public void writeSubfield (String fname, Field field, PrintStream os, Hashtable writtenrefs)
  {
    // only fields (ordinary or exposed) written to output
    if (field.wasChanged () && ((field.getFieldClass () & Field.F_FIELD) != 0))
    {
      os.print ("\t" + fname + " ");
      field.writeFieldValue (os, writtenrefs);  // may be inside prototype here
      os.print ("\n");
    }
  } // writeSubfield

  public void writeX3dNode (VRMLparser parser, PrintStream os, Hashtable writtenrefs, int depth, int proto) {
    if (proto == 1) {
      if (nodeName().equals(PROTO_KEYWORD) || nodeName().equals(EXTERNPROTO_KEYWORD)) {
      ((ProtoNode) this).writeX3dNode(parser, os, writtenrefs, depth);
      return;
      }
      else return;
    }
    else {
      if (nodeName().equals(PROTO_KEYWORD) || nodeName().equals(EXTERNPROTO_KEYWORD)) return;
      else
	writeX3dNode(parser,os,writtenrefs,depth);
    }
    
  }

  /**
   * write X3d node to output stream
   */
  public void writeX3dNode (VRMLparser parser, PrintStream os, Hashtable writtenrefs, int depth)
  {
    if (nodeName().equals(ROUTE_KEYWORD)==true) {
      ((RouteNode) this).writeX3dNode(os, writtenrefs);
      return;
    }

    if (nodeName().equals(PROTO_KEYWORD) || nodeName().equals(EXTERNPROTO_KEYWORD)) {
      ((ProtoNode) this).writeX3dNode(parser, os, writtenrefs, depth);
      return;
    }

    if (nodeName().equals(SCRIPT_KEYWORD)==true) {
      ((Script) this).writeX3dNode(os, writtenrefs, depth); //, objname);
      return;
    }

    ProtoNode pnode = parser.getProtoNode(nodeName());
    if (pnode != null) {    
      ((ProtoInstance) this).writeX3dNode(parser, os, writtenrefs, depth);
      return;
    }

    tab(os, depth); 
    os.print("<"+nodeName()+" ");
    
    if (objname != null)
    {
      if (((Node) writtenrefs.get (objname)) != this)  // first time
      {
	os.print(" DEF=\""+objname+"\" ");
        writtenrefs.put (objname, this);
      }
      else  // already written this instance
      {
	os.print(" USE=\""+objname+"\"/>\n");
        return;
      }
    }

    writeX3dSubfields (parser, os, writtenrefs, depth+1);
  } // writeNode

  /**
   * write X3d node to output stream
   */
  public void writeX3dNode (PrintStream os, Hashtable writtenrefs, int depth)
  {
    if (nodeName().equals(ROUTE_KEYWORD)==true) {
      ((RouteNode) this).writeX3dNode(os, writtenrefs);
      return;
    }
    if (nodeName().equals(PROTO_KEYWORD)==true) {
      ((ProtoNode) this).writeX3dNode(os, writtenrefs, depth);
      return;
    }

     tab(os, depth);

    Node node = NodeNames.createInstanceFromName(nodeName());
    if (node == null) { // for ProtoInstance
	((ProtoInstance) this).writeX3dNode(os, writtenrefs, depth);
	return;
	//os.print("<ProtoInstance name=\""+nodeName()+"\" ");
    }
    else
     os.print("<"+nodeName()+" ");

    if (objname != null)
    {
      if (((Node) writtenrefs.get (objname)) != this)  // first time
      {
	os.print(" DEF=\""+objname+"\" ");
	if (nodeName().equals(SCRIPT_KEYWORD)) 
	   os.print(">\n");

        writtenrefs.put (objname, this);
      }
      else  // already written this instance
      {
        //os.print (USE_KEYWORD + " " + objname+"\n");
	os.print(" USE=\""+objname+"\"/>\n");
        return;
      }
    }
      
    writeX3dSubfields (os, writtenrefs, depth+1);

  } // writeNode

  /**
   * write X3d subfields (inside {}) to outputstream
   */

  public void writeX3dSubfields (PrintStream os, Hashtable writtenrefs,int depth)
  {
    // this works for all Nodes;
    // specific Nodes (longish ones, esp. geometry nodes) may prefer to
    // output their fields in the "canonical" order

    Vector saveNodes = new Vector();
    Vector saveNames = new Vector();

    for (Enumeration e = subfields.keys ();  e.hasMoreElements ();  )
    {
      String fname = (String) e.nextElement ();
      Field f = (Field) subfields.get(fname);
      
      if ((f instanceof SFNode) || (f instanceof MFNode)) {
	saveNodes.addElement(f);
	saveNames.addElement(fname);
      }
      else {
	//os.print("\n");
	//tab(os, depth);
	writeX3dSubfield(fname, (Field) subfields.get (fname), os,writtenrefs);
      }
    }

    if (saveNodes.size() == 0) {
	os.print("/>\n");
        return;
    }
    else {
      os.print(">\n");
      Enumeration ee = saveNodes.elements();
      Enumeration nn = saveNames.elements();

      while (ee.hasMoreElements() && !(nodeName().equals("Inline"))) {
	Field f = (Field) ee.nextElement();
	String fn = (String) nn.nextElement();

	if (f instanceof SFNode) {
	  if (((SFNode) f).getNode() != null) {
	    (((SFNode) f).getNode()).writeX3dNode(os, writtenrefs, depth+1);
	  }
	}
	else { 
	    Vector bn = ((MFNode) f).getNodes();
	    if (bn.size() > 0 ) {
		Enumeration bb = bn.elements();
		while (bb.hasMoreElements()) {
		    ((Node) bb.nextElement()).writeX3dNode(os, writtenrefs, depth+1);
	    }
	}
	}
      }
      tab(os, depth-1);
      os.print("</"+nodeName()+">\n");
      return;
      }
	  
  } // writeX3dSubfields

  /**
   * write X3d subfields (inside {}) to outputstream
   */

  public void writeX3dSubfields (VRMLparser parser, PrintStream os, Hashtable writtenrefs,int depth)
  {
    // this works for all Nodes;
    // specific Nodes (longish ones, esp. geometry nodes) may prefer to
    // output their fields in the "canonical" order

    Vector saveNodes = new Vector();
    Vector saveNames = new Vector();
    int isI=0;
    for (Enumeration e1 = subfields.keys(); e1.hasMoreElements (); )
    {
	String fname = (String) e1.nextElement ();
	Field f = (Field) subfields.get(fname);
	if (f.protoIS) isI++;
    }

    for (Enumeration e = subfields.keys ();  e.hasMoreElements ();  )
    {
      String fname = (String) e.nextElement ();
      Field f = (Field) subfields.get(fname);
      
      if ((f instanceof SFNode) || (f instanceof MFNode)) {
	saveNodes.addElement(f);
	saveNames.addElement(fname);
      }
      else 
	writeX3dSubfield(fname, (Field) subfields.get (fname), os,writtenrefs);
      
    }

    if (saveNodes.size() == 0 && isI==0) {
      os.print("/>\n");
      return;
    }
    else {
      os.print(">\n");
      if (isI>0)
	  writeX3dISfields (parser, os, writtenrefs, depth+1);
      Enumeration ee = saveNodes.elements();
      Enumeration nn = saveNames.elements();

      while (ee.hasMoreElements() && !(nodeName().equals("Inline"))) {
	Field f = (Field) ee.nextElement();
	String fn = (String) nn.nextElement();

	if (f instanceof SFNode) {
	  if (((SFNode) f).getNode() != null) {
	    (((SFNode) f).getNode()).writeX3dNode(parser, os, writtenrefs, depth+1);
	  }
	}
	else { 
	    Vector bn = ((MFNode) f).getNodes();
	    if (bn.size() > 0 ) {
	      Enumeration bb = bn.elements();
	      while (bb.hasMoreElements()) {
		((Node) bb.nextElement()).writeX3dNode(parser, os, writtenrefs, depth+1);
	      }
	    }
	}
      }
      tab(os, depth-1);
      os.print("</"+nodeName()+">\n");
      return;
      }
	  
  } // writeX3dSubfields

  public void writeX3dISfields (VRMLparser parser, PrintStream os, Hashtable writtenrefs,int depth)
  {
    int IS_num = 0;
    for (Enumeration e = subfields.keys ();  e.hasMoreElements ();  )
    {
      String fname = (String) e.nextElement ();
      Field f = (Field) subfields.get(fname);
      if (f.protoIS==true) IS_num++;
    }

    if (IS_num>0) {
	tab(os, depth);
	os.print("<IS>\n");
	for (Enumeration e = subfields.keys ();  e.hasMoreElements ();  )
	    {
		String fname = (String) e.nextElement ();
		Field f = (Field) subfields.get(fname);
		if (f.protoIS==true) {
		    tab(os, depth+1);
		    os.print("<connect nodeField=\""+fname+"\" protoField=\""+f.protoISfield.protoISname+"\"/>\n");
		}
	    }
	tab(os, depth);
	os.print("</IS>\n");
    }
  } // writeX3dISfields

  void tab(PrintStream os, int depth) {
    String t = "";
    for (int d=0; d<depth; d++) 
      t+="  ";
    os.print(t);
  }

  /**
   * write one X3d subfield
   */

  public void writeX3dSubfield (String fname, Field field, PrintStream os, Hashtable writtenrefs)
  {
    // only fields (ordinary or exposed) written to output
    if (field.protoIS==true) return;
    if (field.wasChanged () && ((field.getFieldClass () & Field.F_FIELD) != 0))
      {
      if (field.protoIS) return;
      os.print(" "+fname);
      if ((field.fieldName()).equals("MFString")==true) 
	os.print("=\'");
	else
	  os.print("=\"");

      field.writeX3dFieldValue (os, writtenrefs);  // may be inside prototype here
      if ((field.fieldName()).equals("MFString")==true) 
	os.print("\'");
	else
	  os.print("\""); 
      }
  } // writeX3dSubfield
} // class Node

