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
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;

import java.io.IOException;
import java.util.List;

import com.lauzy.freedom.library.LrcView;
import com.lauzy.freedom.library.Lrc;
import com.lauzy.freedom.library.LrcHelper;



public class LyricViewer extends AndroidNonvisibleComponent {

  private Context mContext;
  private LrcView mLrcView;
  private ComponentContainer mContainer;

  public String mNoLrcText = "No Data";
  public String mCustomTypeFaceFile = "";
  public String mPlayIcon = "default";

  public LyricViewer(ComponentContainer container) {

    super(container.$form());
    this.mContext = container.$context();
    this.mContainer = container;

    try {

      mLrcView = new LrcView(mContext, (BitmapDrawable) Drawable.createFromStream(mContainer.$form().openAssetForExtension(this, "def_lyric_play_button.png"), null));
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
      this.mCustomTypeFaceFile = str;   
      mLrcView.setTypeFace(Typeface.createFromAsset(mContext.getAssets(), mCustomTypeFaceFile));
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
  public int TextColor() {
      return (int) mLrcView.getVarFromMap("mNormalColor");
  }
  @SimpleProperty(description = "Normal text color")
  public void TextColor(int color) {
      mLrcView.setNormalColor(color);
  }

  @SimpleProperty(description = "Current playing line color")
  public int CurrentLyricLineColor() {
      return (int) mLrcView.getVarFromMap("mCurrentPlayLineColor");
  }
  @SimpleProperty(description = "Current playing line color")
  public void CurrentLyricLineColor(int color) {
      mLrcView.setCurrentPlayLineColor(color);
  }

  @SimpleProperty(description = "Text Alignment")
  public Object TextAlignment() {
      return mLrcView.getVarFromMap("mTextAlign");
  }
  @SimpleProperty(description = "Text Alignment")
  public void TextAlignment(Object align) {
      mLrcView.setTextAlignment(align);
  }

  @SimpleProperty(description = "No Lyric Text Size")
  public float NoLyricTextSize() {
      return (float) mLrcView.getVarFromMap("mNoLrcTextSize");
  }
  @SimpleProperty(description = "No Lyric Text Size")
  public void NoLyricTextSize(float size) {
      mLrcView.setNoLrcTextSize(size);
  }

  @SimpleProperty(description = "No Lyric Text Color")
  public int NoLyricTextColor() {
      return (int) mLrcView.getVarFromMap("mNoLrcTextColor");
  }
  @SimpleProperty(description = "No Lyric Text Color")
  public void NoLyricTextColor(int color) {
      mLrcView.setNoLrcTextColor(color);
  }

  @SimpleProperty(description = "Line Width of Indicator")
  public float IndicatorLineWidth() {
      return (float) mLrcView.getVarFromMap("mIndicatorLineWidth");
  }
  @SimpleProperty(description = "Line Width of Indicator")
  public void IndicatorLineWidth(float size) {
      mLrcView.setIndicatorLineWidth(size);
  }

  @SimpleProperty(description = "Text Size of Indicator")
  public float IndicatorTextSize() {
      return (float) mLrcView.getVarFromMap("mIndicatorTextSize");
  }
  @SimpleProperty(description = "Text Size of Indicator")
  public void IndicatorTextSize(float size) {
      mLrcView.setIndicatorTextSize(size);
  }

  @SimpleProperty(description = "Text Color of Indicator")
  public int IndicatorTextColor() {
      return (int) mLrcView.getVarFromMap("mIndicatorTextColor");
  }
  @SimpleProperty(description = "Text Color of Indicator")
  public void IndicatorTextColor(int color) {
      mLrcView.setIndicatorTextColor(color);
  }

  @SimpleProperty(description = "Text Color of the lyric line where the Indicator is in.")
  public int CurrentIndicatingLineColor() {
      return (int) mLrcView.getVarFromMap("mCurrentIndicateLineTextColor");
  }
  @SimpleProperty(description = "Text Color of the lyric line where the Indicator is in.")
  public void CurrentIndicatingLineColor(int color) {
      mLrcView.setCurrentIndicateLineTextColor(color);
  }

  @SimpleProperty(description = "Line Color of Indicator.")
  public int IndicatorLineColor() {
      return (int) mLrcView.getVarFromMap("mIndicatorLineColor");
  }
  @SimpleProperty(description = "Line Color of Indicator.")
  public void IndicatorLineColor(int color) {
      mLrcView.setIndicatorLineColor(color);
  } 

  @SimpleProperty(description = "Margin of Indicator.")
  public float IndicatorMargin() {
      return (float) mLrcView.getVarFromMap("mIndicatorMargin");
  }
  @SimpleProperty(description = "Margin of Indicator.")
  public void IndicatorMargin(float margin) {
      mLrcView.setIndicatorMargin(margin);
  }

  @SimpleProperty(description = "Gap between Icon and Indicator.")
  public float IconLineGap() {
      return (float) mLrcView.getVarFromMap("mIconLineGap");
  }
  @SimpleProperty(description = "Gap between Icon and Indicator.")
  public void IconLineGap(float gap) {
      mLrcView.setIconLineGap(gap);
  }

  @SimpleProperty(description = "Width of Indicator Icon.")
  public float IndicatorPlayIconWidth() {
      return (float) mLrcView.getVarFromMap("mIconWidth");
  }
  @SimpleProperty(description = "Width of Indicator Icon.")
  public void IndicatorPlayIconWidth(float width) {
      mLrcView.setIconWidth(width);
  }

  @SimpleProperty(description = "Height of Indicator Icon.")
  public float IndicatorPlayIconHeight() {
      return (float) mLrcView.getVarFromMap("mIconHeight");
  }
  @SimpleProperty(description = "Height of Indicator Icon.")
  public void IndicatorPlayIconHeight(float height) {
      mLrcView.setIconHeight(height);
  }

  @SimpleProperty(description = "Play icon of Indicator.")
  public String IndicatorPlayIcon() {
      return mPlayIcon;
  }
  @SimpleProperty(description = "Play icon of Indicator.")
  public void IndicatorPlayIcon(String str) {

      try {

        this.mPlayIcon = str;
        mLrcView.setPlayDrawable((BitmapDrawable) Drawable.createFromStream(mContainer.$form().openAsset(mPlayIcon), null)); 
        
      } catch(IOException ioException) {

        throw new YailRuntimeError(ioException.getMessage(), "LyricViewer");

      }

  }

  @SimpleProperty(description = "Setting this to true will make the current playing line Bold.")
  public boolean CurrentLyricLineBold() {
      return (boolean) mLrcView.getVarFromMap("isCurrentTextBold");
  }
  @SimpleProperty(description = "Setting this to true will make the current playing line Bold.")
  public void CurrentLyricLineBold(boolean isBold) {
      mLrcView.setLrcCurrentTextBold(isBold);
  }

  @SimpleProperty(description = "Setting this to true will make line where the Indicator is in Bold.")
  public boolean CurrentIndicatingLineBold() {
      return (boolean) mLrcView.getVarFromMap("isLrcIndicatorTextBold");
  }
  @SimpleProperty(description = "Setting this to true will make line where the Indicator is in Bold.")
  public void CurrentIndicatingLineBold(boolean isBold) {
      mLrcView.setLrcIndicatorTextBold(isBold);
  }

  // Events
  @SimpleEvent(description = "When the lyric position is changed and the play button is clicked on the lyric view. It will return the duration/position ( milliseconds ).")
  public void PlayIndicatorClicked(int position, String content) {
      EventDispatcher.dispatchEvent(this, "PlayIndicatorClicked", position, content);
  }

  // Core Methods
  @SimpleFunction(description = "Adds Lyric view to arrangement")
  public void AddToView(AndroidViewComponent view) {
    FrameLayout fm = (FrameLayout) view.getView();
    fm.addView(mLrcView);
  }

  @SimpleFunction(description = "Sets the lyric file (lrc) from app assets")
  public void SetLyricFromAssets(String fileName) {
      try {
        
        List<Lrc> lrcs = LrcHelper.parseLrcFromAssets(mContainer.$form().openAsset(fileName));
        mLrcView.setLrcData(lrcs);

      } catch(IOException ioException) {

        throw new YailRuntimeError(ioException.getMessage(), "LyricViewer");

      }
  }

  @SimpleFunction(description = "Sets the lyric file (lrc) from filepath")
  public void SetLyricFromFile(String filePath) {
      List<Lrc> lrcs = LrcHelper.parseLrcFromFile(filePath);
      mLrcView.setLrcData(lrcs);
  }

  @SimpleFunction(description = "Updates the lyric position to matching player's current position ( time )")
  public void UpdateTime(long position) {
      mLrcView.updateTime(position);
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





  // Extra Methods
  @SimpleFunction(description = "Parses milliseconds and returns formatted time ( Useful in some cases )")
  public String ParseTime(int position) {

    /*

    Snippet from Stackoverflow by Kishan Soni to Parse milliseconds for Music player.
    https://stackoverflow.com/a/31422675/13639399

    */

    String finalTimerString = "";
    String secondsString = "";

    int hours = (int) (position / (1000 * 60 * 60));
    int minutes = (int) (position % (1000 * 60 * 60)) / (1000 * 60);
    int seconds = (int) ((position % (1000 * 60 * 60)) % (1000 * 60) / 1000);

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


