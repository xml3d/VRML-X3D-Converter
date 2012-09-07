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
 * ParserOutput.java
 *
 * created: mpichler, 19960805
 * changed: mpichler, 19970108
 *
 * $Id: ParserOutput.java,v 1.5 1997/05/22 15:36:42 apesen Exp $
 */


package iicm.vrml.pw;


/**
 * ParserOutput - callbacks for parser messages
 * Copyright (c) 1996,97 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.1, changed:  8 Jan 97
 */


interface ParserOutput
{
  // when this flag is false, no debug code will be generated
  static public boolean debug_ = false;

  void error (String err);      // error messages

  void warning (String warn);   // warning messages

  void verbose (String msg);    // verbose information

  /**
   * debug message. should be surrounded by if (debug_)
   */

  void debug (String msg);      // debug information

} // ParserOutput
