//
// !!! THIS FILE WAS AUTOMATICALLY GENERATED !!!
// !!! DO NOT EDIT !!!
//
// -*- java -*-
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
 * StringArray.java (out of Gen_Array.j)
 * dynamic array of primitive data types
 * (like Vector for arbitrary Objects)
 *
 * Gen_Array.j serves as template for
 * IntArray, FloatArray, DoubleArray
 *
 * created: mpichler, 19960808
 *
 * changed: mpichler, 19970605
 *
 * $Id: Gen_Array.j,v 1.7 1997/06/23 10:23:35 apesen Exp $
 */


package iicm.vrml.pwutils;


/**
 * StringArray - dynamic array of String type values
 * better time and memory efficiency than Vector,
 * String[] accessible in native code
 * Copyright (c) 1996 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @author (based on Field macro by Gerald Pani)
 * @version 0.1, latest change:  1 Okt 96
 */


public class StringArray
{
  // private members

  private String[] data_;

  private int count_;  // no. of used values


  // methods

  /**
   * create with default length
   */

  public StringArray ()
  {
    this (4);
  }

  /**
   * create with initial buffer length
   */

  public StringArray (int len)
  {
    if (len < 1)  // start with at least one element
      len = 1;
    // assert: len > 0
    data_ = new String [len];
    count_ = 0;
  }

  /**
   * copy initial data from another String[]. To say it again:
   * values in String[] are *copied* and not just referenced by this class
   */

  public StringArray (String[] dat)
  {
    if (dat == null || dat.length < 1)
    {
      data_ = new String [1];
      count_ = 0;
    }
    else
    {
      count_ = dat.length;
      data_ = new String [count_];
      System.arraycopy (dat, 0, data_, 0, count_);
    }
  }

  /**
   * copy initial data from another StringArray (non-null).
   * do not use the above constructor in this case, as it would use all values,
   * not just the used length (count); again: values are *copied*
   */

  public StringArray (StringArray dat)
  {
    count_ = dat.count_;
    data_ = new String [count_];
    System.arraycopy (dat.data_, 0, data_, 0, count_);
  }

  /**
   * copy data from another StringArray
   */

  public void setData (StringArray dat)
  {
    setData (dat.data_, dat.count_);
  }

  /**
   * copy data from a String[]
   */

  public void setData (String[] dat)
  {
    setData (dat, dat.length);
  }

  /**
   * copy first n elements of a String[]
   */

  public synchronized void setData (String[] dat, int n)
  {
    if (n > data_.length)
      data_ = new String [n];  // n > data_.lenth >= 0

    count_ = n;
    System.arraycopy (dat, 0, data_, 0, n);
  }

  /**
   * get count of used array members.
   * Note: may be smaller than data_.length
   */

  public final synchronized int getCount ()
  {
    return count_;
  }

  /**
   * get data array. Only elements 0 to (getCount () - 1) were set by user.
   * Subsequent calls (after enlargement) may return a different array object
   */

  public final synchronized String[] getData ()
  {
    return data_;
  }

  /**
   * append one String element
   */

  public synchronized void append (String d)
  {
    int size = data_.length;
    if (count_ == size)
      enlarge (2 * size);
    data_ [count_++] = d;
  }

  /**
   * set number of used array elements (ensures there is enough storage)
   */

  public synchronized void setSize (int n)
  {
    // this will perform badly if called several times for small increments
    if (n > data_.length)
      enlarge (n);
    count_ = n;
  }

  /**
   * clear the array
   * @return true if the array was previously non-empty
   */

  public synchronized boolean clearData ()
  {
    if (count_ == 0)  // empty
      return false;

    count_ = 0;
    if (data_.length > 4)  // save some memory
      data_ = new String [4];

    return true;
  }


  // private methods

  /**
   * enlarge the data array to its new size; does not change count_
   */

  private synchronized void enlarge (int newsize)
  {
    int oldsize = data_.length;
    if (newsize < 4)  // might be 0
      newsize = 4;

    String[] olddata = data_;
    data_ = new String [newsize];
    System.arraycopy (olddata, 0, data_, 0, count_);
  }

} // class StringArray
