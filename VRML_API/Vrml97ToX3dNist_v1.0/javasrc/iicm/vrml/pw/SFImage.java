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
 * SFImage.java
 *
 * created: krosch, 19961019
 *
 * changed: krosch, 19970119
 * changed: mpichler, 19970120
 *
 * $Id: SFImage.java,v 1.6 1997/05/22 15:33:35 apesen Exp $
 */


package iicm.vrml.pw;

import iicm.vrml.pwutils.IntArray;
import java.io.*;
import java.util.Hashtable;


/**
 * SFImage - Field that holds an array of ints (32 bit)
 * Copyright (c) 1996,97 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.1, latest change: 20 Jan 97
 */


public class SFImage extends MFInt32
{
  static final int[] defimage = { 0, 0, 0 };

  SFImage ()
  { super (defimage);
  }

  SFImage (int[] vals)
  { super (vals);
  }

  SFImage (IntArray vals)
  { super (vals);
  }

  public String fieldName ()
  {
    return FieldNames.FIELD_SFImage;
  }

  Field newFieldInstance ()
  {
    return new SFImage (values);
  }

  // meaning of getValueData:
  // width, height, numcomp followed by width*height pixel values;
  // pixels left to right, bottom to top

  // read1Value: see MFInt32

  void readValue (VRMLparser parser) throws IOException
  {
    // no [] around data (SFImage is a "single" field)
    StrTokenizer st = parser.istok;
    ParserOutput pout = parser.pout;

    values.clearData ();  // clear default image
    changed = true;

    read1Value (parser);  // width
    if (readerror)
    { pout.error ("[SFImage] [Error] width (int) expected on parsing SFImage " + Node.atCurrLine (st));
      return;
    }

    read1Value (parser);  // height
    if (readerror)
    { pout.error ("[SFImage] [Error] height (int) expected on parsing SFImage " + Node.atCurrLine (st));
      return;
    }

    read1Value (parser);  // no. of components
    if (readerror)
    { pout.error ("[SFImage] [Error] no. of components (int) expected on parsing SFImage " + Node.atCurrLine (st));
      return;
    } 

    int[] vals = values.getData ();
    // System.out.println ("SFImage. width: " + vals[0] + ", height: " + vals[1]);
    int numpixel = vals[0] * vals[1];  // read (width*height) pixels
    for (int i = 0;  i < numpixel;  i++)
    {
      read1Value (parser);
      if (readerror)
      {
        pout.error ("[SFImage] [Error] could not read pixel no. " + i + " of SFImage (total " + numpixel + ")" + 
          Node.atCurrLine (st));
        return;
      }
    } // for

  } // readValue

  void writeValue (PrintStream os, Hashtable writtenrefs)
  {
    // no [] around data; use hex digits when shorter

    int num = values.getCount ();  // may be incomplete on readerror
    int[] vals = values.getData ();
 // String hexdigits = new String ("0123456789ABCDEF");
    String hexdigits = new String ("0123456789abcdef");

    int pre = (num < 3) ? num : 3;  // preamble

    int i, val;
    for (i = 0;  i < pre;  i++)
      os.print (" " + vals [i]);

    for (i = pre;  i < num;  i++)
    {
      val = vals [i];
      if (val < 256)
        os.print (" " + val);
      else
      {
        // JDK versions prior to 1.0.1 lacked Integer.toHexString
        // os.print (" 0x" + Integer.toHexString (27));
        StringBuffer buf = new StringBuffer (16);
        do
        { buf.insert (0, hexdigits.charAt (val & 15));
          val >>>= 4;
        } while (val != 0);
        os.print (" 0x" + buf.toString ());
      }
    }
  } // writeValue

  void writeX3dValue (PrintStream os, Hashtable writtenrefs)
  {
    writeValue(os, writtenrefs);
  }

} // SFImage
