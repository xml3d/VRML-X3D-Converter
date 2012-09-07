/*
 * OGLCanvas - OpenGL window context
 *
 * created: mpichler, 19960812
 *
 * changed: mpichler, 19970306
 *
 * $Id: OGLCanvas.java,v 1.13 1997/08/05 10:02:37 mpichler Exp $
 */


package iicm.ge3d;

import java.awt.*;


/**
 * OGLCanvas - window context for GE3D.
 * Copyright (c) 1996,97 IICM. All rights reserved.
 *
 * @author Michael Pichler
 * @version 1.0.3, changed:  6 Mar 97
 */


public class OGLCanvas extends Canvas
{
  private boolean hascontext = false;
  private boolean loadedlib = false;
  protected boolean verbose = false;
  // canvas size (size of Component)
  protected int cwidth = 1, cheight = 1;  // assert: non 0
  // window title
  String title = "<unset>";
  // native window handles
  private int nwindow, ncanvas, noglwindow;
  // native OpenGL context
  private int ncontext;   private long ncontextL;
  private int nxcontext;  private long nxcontextL;
  private int ncolormap;
  // native window buffer (Mesa)
  private int nbuffer;   private long nbufferL;
  private int nxbuffer;  private long nxbufferL;
  private boolean nxactive;
  // mesa backbuffer method
  final static int MESABACKBUF_XIMAGE = 0x1;
  final static int MESABACKBUF_PIXMAP = 0x2;
  final static int MESABACKBUF_AUTO = 0x3;  // (MESABACKBUF_XIMAGE | MESABACKBUF_PIXMAP);
  protected int mesa_backbuf = 0x3;  // MESABACKBUF_AUTO;  // auto: ximage on shading, pixmap for wireframe
  // ensure linkage of proper native code version
  private int version_1_0_8 = 1;

  /**
   * constructor
   * @param t window title (needed to identify window)
   */

  public OGLCanvas (String t)
  {
    title = t;
  }

  /** my preferred size ... */

  public Dimension preferredSize ()
  {
    return new Dimension (640, 480);
  }

  /** ... and minimum size */

  public Dimension minimumSize ()
  {
    return new Dimension (64, 48);
  }

  /**
   * no need to clear background on graphics update
   * (will be done by 3D drawing)
   */

  public void update (Graphics gc)
  {
    paint (gc);
  }

  /**
   * create an OpenGL canvas on first painting.
   * derived class must also call setContext () before issuing OpenGL commands
   *
   * paint of a derived class will typically look like this:
   * (see also SampleCanvas in ge3dsample package)
   *
   * <pre>
   * super.paint (gc);  // create context on first draw
   *
   * if (!hasContext () || !setContext ())  // no context
   *   return;  // should clear background in this case
   *
   * // now ready to draw with OpenGL commands
   * // on first draw you should call GE3D.initGE3D ()
   *
   * swapBuffers ();  // finish drawing
   * </pre>
   */

  public void paint (Graphics goofy)
  {
    // System.out.println ("OGLCanvas.paint");

    if (!hascontext)  // first draw into this Canvas
    {
      if (!loadedlib)
      {
        // Solaris gets confused when loading native library
        // in the static part (during component peer setup);
        // so delay it to the time we actually need it: now.
        // System.out.println ("*** gejc lib needed now");
        System.out.println ("loading libgejc.so ...");
        try {
          System.loadLibrary ("gejc");
          loadedlib = true;
          // System.out.println ("*** gejc lib loaded");
        }
        catch (UnsatisfiedLinkError e)
        {
          System.out.println ("OGLCanvas. error: unable to load 'gejc' shared library.");
          System.out.println ("You'll get no display output. Please check your LD_LIBRARY_PATH.");
          // System.exit (1);
        }
      }

      if (loadedlib)
        createContext (mesa_backbuf);

      hascontext = true;  // try to load native lib only once
    }

  } // paint


  /**
   * react on mouse enter/exit. (e.g. colormap installation) in native code.
   * if derived class overrides this, remember calling super.mouseEnter/Exit.
   */

  public boolean mouseEnter (Event e, int x, int y)
  {
    // System.out.println ("mouse enter");
    if (loadedlib)
      mouseInside (1);
    return false;
  }

  /**
   * @see #mouseEnter
   */

  public boolean mouseExit (Event e, int x, int y)
  {
    // System.out.println ("mouse exit");
    if (loadedlib)
      mouseInside (0);
    return false;
  }


  // native methods

  // creation of the OpenGL context, called on first draw
  private native void createContext (int flags);

  // called on mouse enter/exit
  private native void mouseInside (int flag);


  /**
   * check whether native methods are callable
   */

  public boolean hasContext ()
  {
    return loadedlib;  // && hascontext
  }


  /**
   * activate the context before issuing OpenGL commands in paint
   * @param shading flag - should be set to true unless wireframe drawings
   * @return flag, whether context could be established
   */

  protected native boolean setContext (boolean shading);

  // loading shared libraries
  // static
  // {
    // gejc must either refer to OpenGL or Mesa implementation
    // loadLibrary ("gejc"); done on first draw; see above
  // }

  /**
   * when finished with drawing, swap buffers or flush drawings
   * (for double buffering resp. single buffering)
   */

  protected native void swapBuffers ();


  /*
   * set cursor immediately
  public void setFrameCursor (Frame frame, int type)
  {
    frame.setCursor (type);
    flushDisplay ();
  }
   */
  /*
   * flush display buffer
  public native void flushDisplay ();
   */

} // OGLCanvas
