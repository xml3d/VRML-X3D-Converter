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
 * CType.java - character classification
 *
 * created: mpichler, 19970129
 *
 * changed: mpichler, 19970130
 *
 * $Id: CType.java,v 1.2 1997/05/22 15:37:30 apesen Exp $
 */


package iicm.vrml.pw;


/**
 * CType - &lt;ctype.h&gt; analogons for VRML 2.0
 * methods do not care for EOF (-1)
 * Copyright (c) 1997 IICM
 *
 * @author Michael Pichler
 * @version 0.5, latest change: 29 Jan 97
 */

abstract public class CType
{
  // no need ever to create an instance of this class
  private CType () { }

  private static final byte SPACE = 0x1;
  private static final byte IDFIRSTCHAR = 0x2;
  private static final byte IDRESTCHAR = 0x4;

  private static byte ctype_[];
  static
  {
    ctype_ = new byte[256];
    int c;

    byte idchar = (byte) (IDFIRSTCHAR | IDRESTCHAR);
    for (c = 0x21;  c < 256;  c++)  // chars > ' '
      ctype_[c] = idchar;

    // *not* identifiers
    ctype_[0x22] = ctype_[0x23] = ctype_[0x27] = ctype_[0x2c] = ctype_[0x2e] =
    ctype_[0x5b] = ctype_[0x5c] = ctype_[0x5d] = ctype_[0x7b] = ctype_[0x7d] = 0;
    // '"', '#', '\'', ',', '.', '[', '\\', ']', '{', '}'

    // digits: not allowed as first identifier character
    for (c = '0';  c <= '9';  c++)  // 0x30 .. 0x39
      ctype_[c] = IDRESTCHAR;

    // whitespace
    ctype_['\r'] = ctype_['\n'] = ctype_[' '] = ctype_['\t'] = ctype_[','] = SPACE;
    // 0x0d, 0x0a, 0x20, 0x09, 0x2c
  } // static

  /** whitespace character? */
  public final static boolean isspace (byte c)
  { return (ctype_[c] & SPACE) != 0; }

  /** legal character to start identifier? */
  public final static boolean isIDfirstchar (byte c)
  { return (ctype_[c] & IDFIRSTCHAR) != 0; }

  /** legal character inside identifier? */
  public final static boolean isIDrestchar (byte c)
  { return (ctype_[c] & IDRESTCHAR) != 0; }

} // CType
