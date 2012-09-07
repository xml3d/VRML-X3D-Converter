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
 * GotEventCallback.java
 * callback interface for Field.receiveEvent
 * Copyright (c) 1997 IICM
 *
 * created: mpichler, 19970605
 * changed: apesen, 19970605
 *
 * $Id: GotEventCallback.java,v 1.1 1997/08/04 10:50:05 apesen Exp $
 */


package iicm.vrml.pw;


public interface GotEventCallback
{
  void gotEventCB (Field field, double timestamp);
}
