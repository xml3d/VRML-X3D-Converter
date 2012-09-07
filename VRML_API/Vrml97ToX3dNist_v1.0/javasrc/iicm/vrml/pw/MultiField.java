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
 * MultiField.java
 * MultiField interface
 *
 * created: mpichler, 19960808
 * changed: mpichler, 19970131
 * changed: kwagen, 19970917
 *
 * $Id: MultiField.java,v 1.6 1997/09/17 10:15:06 kwagen Exp $
 */


package iicm.vrml.pw;

import java.io.*;
import java.util.Hashtable;


/**
 * MultiField - multi valued Field base class
 * Copyright (c) 1996,97 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.5, latest change: 31 Jan 97
 */



abstract public class MultiField extends Field
{
  /**
   * @return number of field values
   */

  abstract public int getValueCount ();

  /**
   * read a single field value
   * @return true if value could be successfully read
   */

  abstract void read1Value (VRMLparser parser) throws IOException;

  /**
   * clear current values (before reading new ones)
   * @return true if field was previosly non-empty
   */

  abstract boolean clearValues ();

  /**
   * read field's values
   * @return true if all values could be successfully read
   */

  void readValue (VRMLparser parser) throws IOException
  {
    StrTokenizer st = parser.istok;

    // clear any default values (or proto defaults)
    if (clearValues ())
      changed = true;

    st.skipComment ();
    if (st.nextChar () != '[')  // single value
    {
      read1Value (parser);
      return;
    }

    char c = (char) st.readChar ();  // '['

    // multiple values
    while (st.skipCommentReturn (true) && !st.eof () && st.nextChar () != ']')
    {
      // stop field parsing after an error occured (catch up ']')
      if (readerror)
        st.readChar ();
      else {
        read1Value (parser);
      }
      // ',' is whitespace in VRML 2.0
    }

    char t = (char) st.readChar ();  // eat ']'
  } // readValue

} // class MultiField
