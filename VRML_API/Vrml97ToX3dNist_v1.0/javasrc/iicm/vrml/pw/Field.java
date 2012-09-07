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
 * Field.java
 * Field interface
 *
 * created: mpichler, 19960806
 *
 * changed: krosch, 19960909
 * changed: apesen, 19970605
 * changed: mpichler, 19970917
 * changed: kwagen, 19970917
 *
 * $Id: Field.java,v 1.13 1997/09/17 12:50:15 mpichler Exp $
 */


package iicm.vrml.pw;

import java.io.*;
import java.util.Hashtable;
import java.util.Vector;


/**
 * Field - Field base class
 * Copyright (c) 1996,97 IICM
 *
 * @author Michael Pichler, Karin Roschker, Andreas Pesendorfer
 * @version 0.5, latest change: 31 Jan 97
 */


abstract public class Field
{
  // constants
  static final String STR_TRUE = "TRUE";    // boolean TRUE field
  static final String STR_FALSE = "FALSE";  // boolean FALSE field
  static final String STR_NULL = "NULL";    // NULL Node reference
  static final String IS_KEYWORD = "IS";    // IS (use proto field)

  // field classes
  public final static int F_FIELD = 0x1;         // ordinary Field
  public final static int F_EVENTIN = 0x2;       // EventIn
  public final static int F_EVENTOUT = 0x4;      // EventOut
  public final static int F_EXPOSEDFIELD = 0x7;  // ExposedField

  // field class names
  public final static String STR_FIELD = "field";
  public final static String STR_EXPOSEDFIELD = "exposedField";
  public final static String STR_EVENTIN = "eventIn";
  public final static String STR_EVENTOUT = "eventOut";

  public final static String STR_X3d_FIELD = "initializeOnly";
  public final static String STR_X3d_EXPOSEDFIELD = "inputOutput";
  public final static String STR_X3d_EVENTIN = "inputOnly";
  public final static String STR_X3d_EVENTOUT = "outputOnly";

  final static String[] fieldClassName =
  { null, STR_X3d_FIELD, STR_X3d_EVENTIN, null, STR_X3d_EVENTOUT,
    null, null, STR_X3d_EXPOSEDFIELD
  };

  // protected members

  // field class (F_*)
  protected int class_ = F_FIELD;

  // flag whether value was changed since construction
  /*private*/ protected boolean changed;  // = false;

  // flag whether an error occured on reading value
  /*private*/ protected boolean readerror;  // = false

  // the "master" of a proto field (IS ...)
  public Field protoISfield = null;

  // the name of the proto field (IS ...)
  String protoISname;

  // the content of IS : DEFNode.field
  String protoIScontent = null;
  boolean protoIS;

  // receivers (routes from EventOut to EventIn)
  Vector receiver_ = null;  // created on demand. see addReceiver
  private boolean enableRoutes_ = true;  // allowes sending of eventOuts

  // callbacks for receiveEvent
  private Vector goteventcb_;
  public void setEventCallback (GotEventCallback cb)
  {
    if (goteventcb_ == null)
      goteventcb_ = new Vector ();
    goteventcb_.addElement (cb);
  }

  // timestamp of last value change
  double timeOfChange_ = 0.0;

  /**
   * set field class
   */

  void setFieldClass (int fclass)
  {
    class_ = fclass;
  }

  /**
   * get field class
   */

  public int getFieldClass ()
  {
    return class_;
  }

  /**
   * get field class name
   */

  public String getFieldClassName ()
  {
    return fieldClassName [class_];
  }

  /**
   * the field's type name
   */

  abstract public String fieldName ();


  /**
   * create a new instance of the prototyped field
   * changed flags etc. will be virgin (not taken from proto field)
   */

  abstract Field newFieldInstance ();


  /**
   * read field's value(s), which may be a prototyped field (IS).
   * @see #readISdeclaration, #readValue
   */

  void readFieldValue (VRMLparser parser) throws IOException
  {
    readISdeclaration (parser);

    // TODO: shall this really be called when an IS clause was found??? ###
    readValue (parser);  // ordinary field

  } // readFieldValue


  /**
   * check for occurance of an IS clause (inside PROTO).
   * on nested protos only the innermost may provide fields to use
   * @return flag wheter an IS clause was actually found and read
   */

  boolean readISdeclaration (VRMLparser parser) throws IOException
  {

    if (parser.curproto == null)
      return false;

    StrTokenizer st = parser.istok;
    ParserOutput pout = parser.pout;

    String token = st.readIdentifier ();

    if (token != null && token.equals (IS_KEYWORD))
    {
      if (pout.debug_)
        pout.debug ("  . IS field encountered");
      String pname = st.readIdentifier ();

      protoISfield = parser.curproto.getProtoISfield (pname);

      if (pname == null)
      { pout.error ("[Field] [Error] field name expected after IS keyword" + Node.atCurrLine (st));
        readerror = true;
      }
      else if (protoISfield == null)
      { pout.error ("[Field] [Error] proto field name (IS " + pname + ") not found in current PROTO" + Node.atCurrLine (st));
        readerror = true;
      }
      else if (!fieldName ().equals (protoISfield.fieldName ()))
      { pout.error ("[Field] [Error] type mismatch on proto field (" +
        fieldName () + " vs. " + protoISfield.fieldName () + ")" + Node.atCurrLine (st));
        readerror = true;
      }
      else
      {
        protoISfield.protoISname = pname;
        changed = true;
      }
      return true;
    } // IS

    // this is one of the 2 places where the parser must be able to put back a string

    st.putbackString (token);
    return false;

  } // readISdeclaration

  boolean readISdeclaration (VRMLparser parser, String content) 
       throws IOException
  {

    if (readISdeclaration(parser)) {
      if (protoISfield.protoIScontent!=null) 
	protoISfield.protoIScontent.concat(" "+content);
      else
	protoISfield.protoIScontent=content;

      return true;
    }
    return false;

  }

  void setIScontent(String content) {
    if (protoISfield.protoIScontent != null) {
      String tmp = protoISfield.protoIScontent.concat(" "+content);
      protoISfield.protoIScontent = tmp;
    }
    else 
      protoISfield.protoIScontent = content;
  }

  String getIScontent() {
    if (protoISfield == null) return null;
    else 
      if (protoISfield.protoIScontent != null) return protoISfield.protoIScontent;
      else return null;
  }
  /**
   * check whether a route exists to a reciever
   */

  boolean routeExists (Field target)
  {
    if (receiver_ == null)
      return false;
    return receiver_.contains (target);
  }


  /**
   * add eventIn to receiver-list.
   * caller should check whether routeExists first, to avoid identical routes
   */

  void addReceiver (Field eventIn)
  {
    if (receiver_ == null)
      receiver_ = new Vector ();
    receiver_.addElement (eventIn);
  } // addReceiver


  /**
   * remove eventIn from receiver-liat.
   */

  boolean removeReceiver (Field eventIn)
  {
    if (receiver_ == null)
      return false;
    return (receiver_.removeElement (eventIn));
  }


  /**
   * allow sending eventOuts.
   */

  void enableRoutes ()
  {
    enableRoutes_ = true;
  } // enableRoutes


  /**
   * avoid sending eventOuts.
   */

  void disableRoutes ()
  {
    enableRoutes_ = false;
  } // disableRoutes


  /**
   * send an event. actual value to be set before with setValue
   */

  public void sendEvent (double timestamp)
  {
    receiveEvent (timestamp, this);
    // sets timestamp and distributes event to receivers
  }


  /**
   * receive an event and send it to all receiver of the sender
   */

  private void receiveEvent (double timestamp, Field sender)
  {
    if (timestamp <= timeOfChange_ || !enableRoutes_)  // prevent loops
      return;
    timeOfChange_ = timestamp;
    if (sender != this)
      copyValue (sender);  // sender non-null
    if (goteventcb_ != null)
    {
      for (int i = 0; i < goteventcb_.size (); i++)
        ((GotEventCallback) goteventcb_.elementAt (i)).gotEventCB (this, timestamp);
    }
    if (receiver_ == null)
      return;
    for (int i = 0;  i < receiver_.size (); i++)
    {
      ((Field) (receiver_.elementAt (i))).receiveEvent (timestamp, this); 
    }
  }


  /**
   * copy field's value(s) to this field (of same type, non-null)
   */

  abstract void copyValue (Field source);


  /**
   * read field's value(s);
   * sets changed and/or readerror flags accordingly
   */

  abstract void readValue (VRMLparser parser) throws IOException;


  /**
   * write field's value(s), which may be a prototyped field (IS)
   */

  void writeFieldValue (PrintStream os, Hashtable writtenrefs)
  {
    if (protoISfield != null) 
      os.print (IS_KEYWORD + " " + protoISfield.protoISname);
    else
      writeValue (os, writtenrefs);
  }

  void writeX3dFieldValue (PrintStream os, Hashtable writtenrefs)
  {
    if (protoISfield == null) writeX3dValue(os, writtenrefs);
    /*    if (protoISfield != null) {} 
      //os.print (IS_KEYWORD + " " + protoISfield.protoISname);
    else
      writeX3dValue (os, writtenrefs);*/
  }
  /**
   * write field's value(s) to an output stream
   */

  abstract void writeValue (PrintStream os, Hashtable writtenrefs);


  /**
   * write field's value(s) to an x3d output stream
   */

  abstract void writeX3dValue (PrintStream os, Hashtable writtenrefs);

  /**
   * @return flag whether field value was changed after construction
   */

  public final boolean wasChanged ()
  {
    return changed;
  }


  /**
   * @return flag whether an error occured on reading value
   */

  public final boolean readError ()
  {
    return readerror;
  }


  // read routines for primitive data types
  // (used both for single and multi valued fields)

  /**
   * read a boolean field
   */

  final boolean readBoolValue (StrTokenizer st) throws IOException
  {
    boolean value;

//     if (st.nextToken() == StrTokenizer.TT_NUMBER)
//     { // VRML 2.0 booleans only valid as TRUE or FALSE
//       // for backward compatibility only
//       value = (st.nval != 0);
//       changed = true;    // accept, but
//       readerror = true;  // flag as error
//       return value;
//     }

    String token = st.readIdentifier ();

    if (token == null)
    { readerror = true;
      return false;
    }
    if (token.equals (STR_FALSE))
      value = false;
    else if (token.equals (STR_TRUE))
      value = true;
    else
    { readerror = true;
      return false;
    }

    changed = true;
    return value;
  } // readBoolValue


  /**
   * read a String field, must be enclosed in quotes ("").
   * quotes themselves are not part of the string
   */

  final String readStringValue (StrTokenizer st) throws IOException
  {
    st.skipComment ();
    if (st.nextChar () != '"')
    { readerror = true;
      return "";
    }

    changed = true;
    return st.readQuotedString ();
  } // readStringValue


  /**
   * read an integer field (32 bit)
   */

  final int readIntValue (StrTokenizer st) throws IOException
  {
    // should have a way to detect invalid tokens here...
    if (st.eof ())
    { readerror = true;
      return 0;
    }

    changed = true;
    return st.readIntValue ();
  } // readIntValue


  /**
   * read a float/double field
   */

  final double readFloatValue (StrTokenizer st) throws IOException
  {
    // should have a way to detect invalid tokens here...
    if (st.eof ())
    { readerror = true;
      return 0.0;
    }

    changed = true;
    // System.out.println ("float value: " + st.nval);
    return st.readFloatValue ();
  } // readFloatValue
    /*
  public static String fieldNameToX3d(String name) {
    if (name.equals ("SFBool"))      return new String("Boolean");
    if (name.equals ("SFColor"))     return new String("Color");
    if (name.equals ("SFFloat"))     return new String("Float");
    if (name.equals ("SFImage"))     return new String("Image");
    if (name.equals ("SFInt32"))     return new String("Integer");
    if (name.equals ("SFNode"))      return new String("Node");
    if (name.equals ("SFRotation"))  return new String("Rotation");
    if (name.equals ("SFString"))    return new String("String");
    if (name.equals ("SFTime"))      return new String("Time");
    if (name.equals ("SFVec2f"))     return new String("Vector2Float");
    if (name.equals ("SFVec3f"))     return new String("Vector3Float");

    // multi valued fields (MF*)
    if (name.equals ("MFColor"))     return new String("Colors");
    if (name.equals ("MFFloat"))     return new String("Floats");
    if (name.equals ("MFInt32"))     return new String("Integers");
    if (name.equals ("MFNode"))      return new String("Nodes");
    if (name.equals ("MFRotation"))  return new String("Rotations");
    if (name.equals ("MFString"))    return new String("Strings");
    if (name.equals ("MFVec2f"))     return new String("Vector2FloatArray");
    if (name.equals ("MFVec3f"))     return new String("Vector3FloatArray");
    
    return null;
    }*/
  public static String fieldNameToX3d(String name) {
    if (name.equals ("SFBool"))      return new String("SFBool");
    if (name.equals ("SFColor"))     return new String("SFColor");
    if (name.equals ("SFFloat"))     return new String("SFFloat");
    if (name.equals ("SFImage"))     return new String("SFImage");
    if (name.equals ("SFInt32"))     return new String("SFInt32");
    if (name.equals ("SFNode"))      return new String("SFNode");
    if (name.equals ("SFRotation"))  return new String("SFRotation");
    if (name.equals ("SFString"))    return new String("SFString");
    if (name.equals ("SFTime"))      return new String("SFTime");
    if (name.equals ("SFVec2f"))     return new String("SFVec2f");
    if (name.equals ("SFVec3f"))     return new String("SFVec3f");

    // multi valued fields (MF*)
    if (name.equals ("MFColor"))     return new String("MFColor");
    if (name.equals ("MFFloat"))     return new String("MFFloat");
    if (name.equals ("MFInt32"))     return new String("MFInt32");
    if (name.equals ("MFNode"))      return new String("MFNode");
    if (name.equals ("MFRotation"))  return new String("MFRotation");
    if (name.equals ("MFString"))    return new String("MFString");
    if (name.equals ("MFVec2f"))     return new String("MFVec2f");
    if (name.equals ("MFVec3f"))     return new String("MFVec3f");
    
    return null;
  }
} // class Field

