package com.jaxparrow.lyricviewer;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;
import com.google.appinventor.components.runtime.util.*;

import android.content.Context; 
import android.widget.FrameLayout;
import android.graphics.Typeface;
import android.graphics.Paint;

import java.io.IOException;
import java.util.List;

import com.lauzy.freedom.library.LrcView;
import com.lauzy.freedom.library.Lrc;
import com.lauzy.freedom.library.LrcHelper;



public class LyricViewer extends AndroidNonvisibleComponent {

  private Context mContext;
  private LrcView mLrcView;

  public String mNoLrcText = "No Data";
  public String mCustomTypeFaceFile = "";

  public LyricViewer(ComponentContainer container) {
    super(container.$form());
    this.mContext = container.$context();

    mLrcView = new LrcView(mContext);
    mLrcView.setOnPlayIndicatorLineListener(new LrcView.OnPlayIndicatorLineListener() {
               @Override
               public void onPlay(long time, String content) {
                   PlayIndicatorClicked((int) time, content);
               }
    });

  }

  // Getter Blocks # Simple
  @SimpleProperty(description = "")
  public int Width() {
      return mLrcView.getLrcWidth();
  }
  @SimpleProperty(description = "")
  public int Height() {
      return mLrcView.getLrcHeight();
  }

  @SimpleProperty(description = "")
  public Object TextAlignLeft() {
      return Paint.Align.LEFT;
  }
  @SimpleProperty(description = "")
  public Object TextAlignCenter() {
      return Paint.Align.CENTER;
  }
  @SimpleProperty(description = "")
  public Object TextAlignRight() {
      return Paint.Align.RIGHT;
  }

  // Getter Blocks # Properties
  @SimpleProperty(description = "Custom font from assets")
  public String CustomTypeFace() {
      return this.mCustomTypeFaceFile;
  }
  @SimpleProperty(description = "Custom font from assets")
  public void CustomTypeFace(String str) {
      mLrcView.setTypeFace(Typeface.createFromAsset(mContext.getAssets(), str));
      this.mCustomTypeFaceFile = str;
  }


  @SimpleProperty(description = "No lyrics text")
  public String EmptyContent() {
      return this.mNoLrcText;
  }
  @SimpleProperty(description = "No lyrics text")
  public void EmptyContent(String str) {
      mLrcView.setEmptyContent(str);
      this.mNoLrcText = str;
  }

  @SimpleProperty(description = "Font size of lyrics")
  public float TextSize() {
      return (float) mLrcView.getVarFromMap("mLrcTextSize");
  }
  @SimpleProperty(description = "Font size of lyrics")
  public void TextSize(float size) {
      mLrcView.setLrcTextSize(size);
  }

  @SimpleProperty(description = "Spacing between lines")
  public float LineSpacing() {
      return (float) mLrcView.getVarFromMap("mLrcLineSpaceHeight");
  }
  @SimpleProperty(description = "Spacing between lines")
  public void LineSpacing(float size) {
      mLrcView.setLrcLineSpaceHeight(size);
  }

  @SimpleProperty(description = "Touch Delay of lyrics")
  public int TouchDelay() {
      return (int) mLrcView.getVarFromMap("mTouchDelay");
  }
  @SimpleProperty(description = "Touch Delay of lyrics")
  public void TouchDelay(int delay) {
      mLrcView.setTouchDelay(delay);
  }

  @SimpleProperty(description = "Touch Delay of indicator")
  public int IndicatorTouchDelay() {
      return (int) mLrcView.getVarFromMap("mIndicatorTouchDelay");
  }
  @SimpleProperty(description = "Touch Delay of indicator")
  public void IndicatorTouchDelay(int delay) {
      mLrcView.setIndicatorTouchDelay(delay);
  }

  @SimpleProperty(description = "Normal text color")
  public int NormalColor() {
      return (int) mLrcView.getVarFromMap("mNormalColor");
  }
  @SimpleProperty(description = "Normal text color")
  public void NormalColor(int color) {
      mLrcView.setNormalColor(color);
  }

  @SimpleProperty(description = "Current play line color")
  public int CurrentLyricLineColor() {
      return (int) mLrcView.getVarFromMap("mCurrentPlayLineColor");
  }
  @SimpleProperty(description = "Current play line color")
  public void CurrentLyricLineColor(int color) {
      mLrcView.setCurrentPlayLineColor(color);
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
  public void SetLyricFromFile(String filePath) {
      List<Lrc> lrcs = LrcHelper.parseLrcFromFile(filePath);
      mLrcView.setLrcData(lrcs);
  }

  @SimpleFunction(description = "Updates the lyric position to matching player's current position ( time )")
  public void UpdateTime(long time) {
      mLrcView.updateTime(time);
  }

  @SimpleFunction(description = "Pauses the autoscrolling")
  public void Pause() {
      mLrcView.pause();
  }

  @SimpleFunction(description = "Resumes the autoscrolling if paused")
  public void Resume() {
      mLrcView.resume();
  }

  @SimpleFunction(description = "Shows the indicator")
  public void ShowIndicator() {
      mLrcView.setEnableShowIndicator(true);
  }

  @SimpleFunction(description = "Hides the indicator")
  public void HideIndicator() {
      mLrcView.setEnableShowIndicator(false);
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
  public long GetTimeByLinePosition(int linePosition) {
      return mLrcView.GetTimeByLinePosition(linePosition);
  }

  @SimpleFunction(description = "Returns text height of content by linePosition")
  public float GetTextHeightbyLinePosition(int linePosition) {
      return mLrcView.getTextHeight(linePosition);
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


