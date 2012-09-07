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
 * StrTokenizer.java - read VRML stream tokens
 * Copyright (c) 1997 IICM
 *
 * created: mpichler, 19970115
 *
 * changed: mpichler, 19970415
 *
 * $Id: StrTokenizer.java,v 1.10 1997/06/04 12:12:00 mpichler Exp $
 */


package iicm.vrml.pw;

import iicm.vrml.pwutils.CharArray;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;


/**
 * StrTokenizer - read VRML stream tokens
 * Copyright (c) 1997 IICM
 *
 * @author Michael Pichler
 * @version 1.0.2, latest change: 18 Feb 97
 */

public class StrTokenizer
{
  private InputStream in_;
  private final int optsize = 2048;  // no. of bytes read at once from input stream
  private byte[] backbuf_ = new byte[optsize];  // may grow a bit later for pushing back strings
  private int buffered_ = 0;  // no. of total bytes in backbuf
  private int backoffs_ = 0;  // index of next
  private int c_;  // next character or -1 on eof
  private int lineno_ = 1;  // line number
  private CharArray commentBuffer = new CharArray (1024);

  /**
   * construct a StrTokenizer for an InputStream.
   * StrTokenizer does buffering on the input stream.
   * current implementation blocks to read first character from stream
   */
  public StrTokenizer (InputStream is)
  {
    in_ = is;
    try
    { getChar ();  // or c_ = is.read ();
    }
    catch (IOException e)  // treat like EOF
    { c_ = -1;
    }
  }

  /**
   * return flag whether end of file was reached
   */
  public boolean eof ()
  {
    return c_ < 0;
  }

  /**
   * current line number
   */
  public int lineno ()
  {
    return lineno_;
  }

  /**
   * look at next character without reading a new one.
   * returns -1 on eof
   * @see skipComment
   */
  public int nextChar ()
  {
    return c_;
  }

  /**
   * get next character from stream or backbuffer (internal)
   */
  private final int getChar () throws IOException
  {
    // c_ is always the next char to work with;
    // for convenience, it is also returned by this function
    // c_ can also hold eof (-1), backbuf_ has only chars

    // line no. increased after '\n' has been processed, i.e. here
    if (c_ == '\n')  // may call a progress callback here
      lineno_++;

    if (backoffs_ >= buffered_)  // backbuf got empty - refill
    {
      backoffs_ = 0;

      // Process.InputStream seems not block properly in read call without this
      // but with this, gunzip cannot be called a second time
      // buffered_ = in_.available ();

      buffered_ = in_.read (backbuf_, 0, optsize);

      if (buffered_ < 1)  // no more data
      {
        in_.close ();
        return (c_ = -1);
      }
    }

    // ass.: backoffs_ < buffered_
    // take next char from buffer
    c_ = backbuf_[backoffs_++] & 0xff;  // or 255 would be -1
    return c_;

  } // getChar

// skip whitespace - called by all read operations
// as comments are syntactically equivalent to ws,
// there is no need for this function
//   private void skipWS () throws IOException
//   {
//     while (c_ >= 0 && CType.isspace ((byte) c_))
//       getChar ();
//   }

  /**
   * skip any amount of whitspace - handle any comment.
   * called by all read operations
   */
  public void skipComment () throws IOException
  {
    int c = c_;
    boolean commentFound = false;
    commentBuffer = new CharArray(0); // reset

    try {
    while (true)
    {
      while (c >= 0 && CType.isspace ((byte) c))
        c = getChar ();

      while  (c == '#')
      {
      	// comment until end of line
        // while (c >= 0 && c != '\r' && c != '\n')
        //   c = getChar ();
      	
      	// added 13 July 2002, Don Brutzman
      	// handle comments until end of line
      	commentFound = true;
      	commentBuffer.append ((char) '<');
      	commentBuffer.append ((char) '!');
      	commentBuffer.append ((char) '-');
      	commentBuffer.append ((char) '-');
	c = getChar (); // skip #
        while (c >= 0 && c != '\r' && c != '\n')
        {
    		commentBuffer.append ((char) c);
		c = getChar ();
        }
        commentBuffer.append ((char) ' ');

        // line no. increased after '\n' has been processed, i.e. here
        if (c_ == '\n')  // may call a progress callback here
      		lineno_++;
        
        //// possible option: combine adjacent comments
        //// clear following space and set up for another token
       	while (c >= 0 && CType.isspace ((byte) c))
        	c = getChar ();
        commentBuffer.append ((char) '-');
        commentBuffer.append ((char) '-');
        commentBuffer.append ((char) '>');
        commentBuffer.append ((char) '\n');
      }

      if  (commentFound)
      {
//      String commentString = new String(commentBuffer.getData(), 0, commentBuffer.getCount ());
//      System.out.print   (commentString);
        System.out.print   (OutputComment ());
        if (!eof()) System.out.println ("[next .wrl line " + (lineno_ - 1) + "]\n");
        	
        // what really needs to happen:  treat commentString similarly to a Node or Field
        // and insert them in file when traversing scene graph for output
      }
      return;
    }
    }
    catch (IOException ioe)
    {
    	// ignore eof
    	// System.out.println (ioe);
    }
    catch (Exception e)
    {
    	System.out.println (e);
    }
  } // skipComment

  /**
   * OutputComment outputs the commen.  Invoke between nodes and not inside field attributes.
   */
  String OutputComment () throws IOException
  {
    String commentString = new String(commentBuffer.getData(), 0, commentBuffer.getCount ());
    return commentString;
  }


  /**
   * call skipComment and return the argument.
   * java has no comma operator for loop conditions
   */
  boolean skipCommentReturn (boolean arg) throws IOException
  {
    // this enables loops like "while (skipcomment, nextchar != 'x')"
    // which can be written like "while (skipCommentReturn (true) && nextchar != 'x')
    // (even for loops do not allow a comma in the conditon part)
    skipComment ();
    return arg;
  }

  /**
   * read a whole line (until \r or \n) into a char[] buffer.
   * if the line is longer than the buffer, the rest is skipped.
   * the newline character is not appended to the buffer
   * @return the no. of chars written into buf
   */
  public int readLine (char[] buf) throws IOException
  {
    int len = buf.length;
    int i = 0;

    while (c_ >= 0 && c_ != '\r' && c_ != '\n')
    {
      if (i < len)
        buf[i++] = (char) c_;
      getChar ();
    }

    return i;

  } // readLine

  /**
   * read a character (skips whitespace and comments)
   * @return character or -1 on EOF
   */
  public int readChar () throws IOException
  {
    skipComment ();
    int c = c_;
    getChar ();
    return c;
  } // readChar

  /**
   * read an identifier String (skips whitespace and comments)
   * @return null if no identifier could be read or non-zero length identifier string
   */
  public String readIdentifier () throws IOException
  {
    skipComment ();
    int c = c_;
    if (c < 0 || !CType.isIDfirstchar ((byte) c))  // invalid identifier
      return null;

    CharArray buf = new CharArray (32);
    buf.append ((char) c);
    c = getChar ();
    while (c >= 0 && CType.isIDrestchar ((byte) c))
    {
      buf.append ((char) c);
      c = getChar ();
    }

    return new String (buf.getData (), 0, buf.getCount ());
  } // readIdentifier

  /**
   * read a quoted String ("...").
   * the quotes themselves are not part of the String returned.
   * returns empty string if next char is not '"'
   */
  public String readQuotedString () throws IOException
  {
    skipComment ();
    if (c_ != '"')
      return "";

    CharArray buf = new CharArray (32);
    boolean escaped = false;

    // VRML spec: "any characters (including newlines and '#') may appear within the quotes"

    while (true)
    {
      getChar ();
      if (c_ < 0)  // EOF
        break;
      if (escaped)  // escaped character
      { buf.append ((char) c_);
        escaped = false;
      }
      else if (c_ == '\\')  // escape next char
        escaped = true;
      else if (c_ == '"')  // unescaped '"': end of string
      { getChar ();  // eat '"'
        break;
      }
      else  // ordinary char
        buf.append ((char) c_);
    }

    return new String (buf.getData (), 0, buf.getCount ());
  } // readQuotedString

  /**
   * read an integer. format: [+-][0-9]* or [+-]0x[0-9A-Fa-f]*
   */
  public int readIntValue () throws IOException
  {
    int val = 0;  // may use long instead
    boolean hex = false;
    boolean negative = false;

    skipComment ();  // leading ws/comment
    int c = c_;

    if (c == '+' || c == '-')  // sign
    { negative = (c == '-');
      c = getChar ();
    }

    if (c == '0')
    { c = getChar ();
      if (c == 'x')  // hexadecimal
      { c = getChar ();
        hex = true;
      } // leading 0 may be otherwise ignored
    }

    if (hex)  // hexadecimal
    {
      while (true)
      {
        if ('0' <= c && c <= '9')
          val = 16 * val + (c - '0');
        else if ('a' <= c && c <= 'f')
          val = 16 * val + (c - 'a' + 10);
        else if ('A' <= c && c <= 'F')
          val = 16 * val + (c - 'A' + 10);
        else
          break;
        c = getChar ();
      }
    }
    else  // decimal
    {
      while ('0' <= c && c <= '9')  // base value
      { val = 10 * val + (c - '0');
        c = getChar ();
      }
    }

    return (negative ? -val : val);

  } // readIntValue

  /**
   * read a floating point number. format: [+-][0-9]*{[.][0-9]*}{[Ee][+-][0-9]*}
   */
  public double readFloatValue () throws IOException
  {
    double val = 0.0;
    boolean decpoint = false;
    boolean negative = false;

    skipComment ();  // leading ws/comment
    int c = c_;

    if (c == '+' || c == '-')  // sign
    { negative = (c == '-');
      c = getChar ();
    }

    while ('0' <= c && c <= '9')  // base value
    { val = 10 * val + (c - '0');
      c = getChar ();
    }

    if (c == '.')  // after decimal point
    {
      c = getChar ();  // eat '.'
      double posval = 0.1;  // digit value
      while ('0' <= c && c <= '9')
      { val += (c - '0') * posval;
        posval *= 0.1;
        c = getChar ();
      }
    }

    if (c == 'E' || c == 'e')  // exponential part
    {
      c = getChar ();  // eat 'e'
      double exp = 0.0;
      boolean negexp = false;

      if (c == '+' || c == '-')  // sign
      { negexp = (c == '-');
        c = getChar ();
      }
      while ('0' <= c && c <= '9')  // exponent value
      { exp = 10 * exp + (c - '0');
        c = getChar ();
      }

      val *= Math.pow (10, negexp ? -exp : exp);
    } // exponent

    return (negative ? -val : val);

  } // readFloatValue

  /**
   * put back a String.
   * In order to not further slow down reading of numbers, their
   * string representation is not remembered by this class
   */
  public void putbackString (String s)
  {
    int slen;
    if (s == null || (slen = s.length ()) == 0)  // (!s || !*s ;-)
      return;

    // all '\n' in s have been counted as processed - undo this.
    // note that the buffered char c_ does not contribute to lineno_.
    // as only identifiers are put back, this loop is dead code anyway
    int pos = -1;
    while ((pos = s.indexOf ('\n', pos + 1)) >= 0)
      lineno_--;

    int c = s.charAt (0);  // the very next character

    if (backoffs_ >= slen)  // string fits into old backbuffer
    {
      int start = backoffs_ - slen;
      s.getBytes (1, slen, backbuf_, start);  // without first char
      backbuf_ [backoffs_ - 1] = (byte) c_;
      backoffs_ = start;
      // buffered_ (behind last char) remains the same
    }
    else  // create new backbuf
    {
      int oldlen = buffered_ - backoffs_;
      int newlen = slen + oldlen;
      // (would suffice to shuffle data around in backbuf_ when newlen <= backbuf_.size)
      byte[] newbuf = new byte[(newlen > optsize) ? newlen : optsize];
      s.getBytes (1, slen, newbuf, 0);  // without first char
      newbuf [slen - 1] = (byte) c_;
      System.arraycopy (backbuf_, backoffs_, newbuf, slen, oldlen);

      backbuf_ = newbuf;
      backoffs_ = 0;
      buffered_ = slen + oldlen;
    }

    c_ = c;

  } // putbackString

} // StrTokenizer
