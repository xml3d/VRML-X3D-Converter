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
 * DefParserOutput.java
 *
 * created: mpichler, 19960805
 * changed: mpichler, 19960805
 *
 * $Id: DefParserOutput.java,v 1.3 1997/05/22 15:38:02 apesen Exp $
 */


package iicm.vrml.pw;


/**
 * DefParserOutput - default: write parser messages to System.out
 * Copyright (c) 1996 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.1, changed:  5 Aug 96
 */


public class DefParserOutput implements ParserOutput
{
  public void error (String err)
  {
    System.out.println ("[DefParserOutput] [Error] " + err);
  }

  public void warning (String warn)
  {
    System.out.println ("[DefParserOutput] [Warning] " + warn);
  }

  public void verbose (String msg)
  {
    System.out.println ("[DefParserOutput] " + msg);
  }

  public void debug (String msg)
  {
    System.out.println ("pw: " + msg);
  }

} // DefParserOutput
