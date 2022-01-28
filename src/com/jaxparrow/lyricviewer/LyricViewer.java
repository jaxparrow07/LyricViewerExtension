package com.jaxparrow.lyricviewer;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;
import com.google.appinventor.components.runtime.util.*;

import android.content.Context; 
import android.widget.FrameLayout;

import com.lauzy.freedom.library.LrcView;
import com.lauzy.freedom.library.Lrc;
import com.lauzy.freedom.library.LrcHelper;

import java.io.IOException;
import java.util.List;

public class LyricViewer extends AndroidNonvisibleComponent {

  private Context mContext;
  private LrcView mLrcView;
  public LyricAttrib mLyricAttrib;

  public String mNoLrcText = "No Data";

  public LyricViewer(ComponentContainer container) {
    super(container.$form());
    this.mContext = container.$context();

    try {

      mLrcView = new LrcView(mContext, MediaUtil.getBitmapDrawable(container.$form()));
      mLrcView.setOnPlayIndicatorLineListener(new LrcView.OnPlayIndicatorLineListener() {
                 @Override
                 public void onPlay(long time, String content) {
                     PlayIndicatorClicked((int) time, content);
                 }
      });
    
    } catch(IOException ioException) {
        return;
    }


  }

  // Events
  @SimpleEvent(description = "When the lyric position is changed and the play button is clicked on the lyric view. It will return the duration/position ( milliseconds ).")
  public void PlayIndicatorClicked(int position, String line) {
      EventDispatcher.dispatchEvent(this, "PlayIndicatorClicked", position, line);
  }

  // Core Methods
  @SimpleFunction(description = "Adds Lyric view to arrangement")
  public void AddToView(AndroidViewComponent view) {
    FrameLayout fm = (FrameLayout) view.getView();
    fm.addView(mLrcView);
  }

  @SimpleFunction(description = "Sets the lyric file (lrc) from app assets")
  public void SetLyricFromAssets(String fileName) {
      List<Lrc> lrcs = LrcHelper.parseLrcFromAssets(mContext, fileName);
      mLrcView.setLrcData(lrcs);
  }

  @SimpleFunction(description = "Sets the lyric file (lrc) from filepath")
  public void SetLyricFromFile(String fileName) {
      List<Lrc> lrcs = LrcHelper.parseLrcFromAssets(mContext, fileName);
      mLrcView.setLrcData(lrcs);
  }

  @SimpleFunction(description = "Updates the lyric position to matching player's current position ( time )")
  public void UpdateTime(long time) {
      mLrcView.updateTime(time);
  }

  @SimpleFunction(description = "Sets 'No Data' text")
  public void SetEmpty() {
      mLrcView.setEmptyContent(mNoLrcText);
  }

  @SimpleFunction(description = "Animates and scrolls to the position by the line position.")
  public void ScrollToLine(int linePosition) {
      mLrcView.scrollToPosition(linePosition)
  }


  // Getter Methods #1
  @SimpleFunction(description = "Returns the line count of current lyrics")
  public int GetLinesCount() {
      return mLrcView.getLrcCount();
  }

  @SimpleFunction(description = "Returns the indicator position")
  public int GetIndicatorLinePosition() {
      return mLrcView.getIndicatePosition();
  }

  @SimpleFunction(description = "Returns true if lyric is empty")
  public boolean isLyricEmpty() {
      return mLrcView.isLrcEmpty();
  }

  // Getter Methods #2
  @SimpleFunction(description = "Returns line position by give time")
  public int GetLinePositionByTime(long time) {
      return mLrcView.getUpdateTimeLinePosition(time);
  }

  @SimpleFunction(description = "Returns lyric line content by line position")
  public String GetContentByLinePosition(int linePosition) {
      return mLrcView.GetContentByLinePosition(linePosition);
  }

  @SimpleFunction(description = "Returns lyric line content by line position")
  public String GetTimeByLinePosition(int linePosition) {
      return mLrcView.GetTimeByLinePosition(linePosition);
  }

  @SimpleFunction(description = "Returns text height of content by linePosition")
  public float GetTextHeightbyLinePosition(int linePosition) {
      mLrcView.getTextHeight(linePosition)
  }





  // Extra Methods
  @SimpleFunction(description = "Parses milliseconds and returns formatted time ( Useful in some cases )")
  public String ParseTime(int time) {

    /*

    Snippet from Stackoverflow by Kishan Soni to Parse milliseconds for Music player.
    https://stackoverflow.com/a/31422675/13639399

    */

    String finalTimerString = "";
    String secondsString = "";

    int hours = (int) (time / (1000 * 60 * 60));
    int minutes = (int) (time % (1000 * 60 * 60)) / (1000 * 60);
    int seconds = (int) ((time % (1000 * 60 * 60)) % (1000 * 60) / 1000);

    if (hours > 0) {
        finalTimerString = hours + ":";
    }

    if (seconds < 10) {
        secondsString = "0" + seconds;
    } else {
        secondsString = "" + seconds;
    }

    finalTimerString = finalTimerString + minutes + ":" + secondsString;

    return finalTimerString;

  }
 




}


