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
 * TimeSensor.java
 * Copyright (c) 1997 IICM
 *
 * created: krosch, 19960824
 *
 * changed: krosch, 19960829
 * changed: apesen, 19970410
 *
 * $Id: TimeSensor.java,v 1.5 1997/08/20 12:05:30 apesen Exp $
 */


// TimeSensor
 
package iicm.vrml.pw; 

import java.util.Vector;

public class TimeSensor extends Sensor
{
  public SFTime cycleInterval, startTime, stopTime;
  public SFBool enabled, loop;

  public SFTime cycleTime, time;
  public SFBool isActive;
  public SFFloat fraction_changed;
  
  private boolean newCycle_ = true;

  private double cycleInterval_, startTime_, stopTime_;
  private double cycleStart_ = -1.0;  // exact cycle starttime /|/|/|/|/

  private Vector saveStartTRec_ = null;  // save startTime-receiver-list 
  private Vector saveStopTRec_ = null;  // save stopTime-receiver-list 
  private Vector saveCycleIRec_ = null;  // save cycleInterval-receiver-list 

  public String nodeName ()
  {
    return NodeNames.NODE_TIMESENSOR;
  }
  
  public void traverse (Traverser t)
  {
    t.tTimeSensor (this);
  }

  public static String double2string (double val)
  {
    // Java unable to directly show accurate double values
    double fract = val - (long) val;
    if (fract == 0)
      return (long) val + ".0";
    String fractstr = "" + fract;
    if (fractstr.startsWith ("0."))
      return (long) val + fractstr.substring (1);
    return (long) val + "+" + fract;
  }

  public void evaluate (double timestamp)
  {
    // System.out.println("TimeSensor: " + enabled.getValue ());
    if (enabled.getValue ())
    {
      if (!isActive.getValue ())
      {
        // start a new cycle when loop==true && stopTime <= startTime (after loading)
        // or when timestamp >= startTime and not yet active

        if ((stopTime.getValue () > startTime.getValue () || !loop.getValue ()) && timestamp < startTime.getValue())
          return;
        if (startTime_ == startTime.getValue () && !loop.getValue ())  // cycle for this start time already done
          return;
        // System.out.println(objname +" : Send isActive (true)");
        // System.out.println(objname +" : timestamp : " + double2string (timestamp));
        // System.out.println(objname +" : startTime : " + double2string (startTime.getValue ()));
        // System.out.println(objname +" : stopTime  : " + double2string (stopTime.getValue ()));
        isActive.setValue (true);
        isActive.sendEvent (timestamp);  // send isActive (=true) when begin running
        newCycle_ = true;
        stopTime_ = stopTime.getValue ();
        startTime_ = startTime.getValue ();
        cycleInterval_ = cycleInterval.getValue ();
        cycleStart_ = timestamp;
        startTime.disableRoutes ();      // send no starttime event
        stopTime.disableRoutes ();       // send no stoptime event
        cycleInterval.disableRoutes ();  // send no cycleInterval event
      }
      if (newCycle_)  // begin a new cycle
      {
        // System.out.println(objname +" : begin new cycle");
        cycleTime.setValue (timestamp);
        cycleTime.sendEvent (timestamp);  // send cycleTime-Event at begin of each cycle
        // System.out.println(objname +" : Send cycleTime (timestamp)");
        newCycle_ = false;
      }

      if (timestamp - cycleStart_ > cycleInterval_)
      {
        newCycle_ = true;  // next evaluation starts a new cycle
        cycleStart_ = ((double) ((int) ((timestamp - startTime_) / cycleInterval_))) * cycleInterval_;
      }
      double temp = (timestamp - startTime_) / cycleInterval_;  // calc new tick 
      double f = temp - ((double) ((long) temp));
      // System.out.println(objname +" : Rest:" + f);
      if ((f == 0.0 && timestamp > startTime_) || (newCycle_ && !loop.getValue ()))
        f = 1.0;

      fraction_changed.setValue((float) f);    // new cycle-tick
      fraction_changed.sendEvent (timestamp);  // sent fraction_changed-Event after each change
      time.sendEvent (timestamp);  // sent time-Event at each tick

      if (newCycle_)
        if ((timestamp >= stopTime_) && (stopTime_ > startTime_) || (!loop.getValue ()))  
        {
          // System.out.println(objname +" : Send isActiv (false);  (timeout or loop == false)");
          isActive.setValue(false);
          isActive.sendEvent (timestamp);
          startTime.enableRoutes ();      // enable sending of starttime event
          stopTime.enableRoutes ();       // enable sending of stoptime event
          cycleInterval.enableRoutes ();  // enable sending of cycleInterval event
        }
    }
    else if (isActive.getValue ())
    {
      // System.out.println(objname +" : Send isActiv (false);  (enabeld == false)");
      isActive.setValue(false);
      isActive.sendEvent (timestamp);
      startTime.enableRoutes ();      // enable sending of starttime event
      stopTime.enableRoutes ();       // enable sending of stoptime event
      cycleInterval.enableRoutes ();  // enable sending of cycleInterval event
    }
  } // evaluate

  TimeSensor ()
  {
    addField ("cycleInterval", cycleInterval = new SFTime (1.0), Field.F_EXPOSEDFIELD);
    addField ("enabled", enabled = new SFBool (true), Field.F_EXPOSEDFIELD);
    addField ("loop", loop = new SFBool (false), Field.F_EXPOSEDFIELD);
    addField ("startTime", startTime = new SFTime (0.0), Field.F_EXPOSEDFIELD);
    addField ("stopTime", stopTime = new SFTime (0.0), Field.F_EXPOSEDFIELD);
    addField ("cycleTime", cycleTime = new SFTime (0.0), Field.F_EVENTOUT);  // eventOut SFTime cycleTime
    addField ("fraction_changed", fraction_changed = new SFFloat (0.0f), Field.F_EVENTOUT);  // eventOut
    addField ("isActive", isActive = new SFBool (false), Field.F_EVENTOUT);  // eventOut SFBool isActive
    addField ("time", time = new SFTime (0.0), Field.F_EVENTOUT);  // eventOut SFTime time
  }
} // TimeSensor
