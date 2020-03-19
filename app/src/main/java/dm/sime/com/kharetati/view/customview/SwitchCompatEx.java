package dm.sime.com.kharetati.view.customview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;


public class SwitchCompatEx extends SwitchCompat {

    private Paint thumbLabelPaint;
    private Paint trackLabelPaint;
    public int TRACK_COLOR = 0xFFFFFFFF;
    public int TRACK_STROKE_WIDTH = dp2Px(2f);
    public int TRACK_STROKE_COLOR = 0xFFb71e3e;
    public int TRACK_LABEL_COLOR = 0xFFb71e3e;
    public int TRACK_LABEL_SIZE = sp2Px(12f);

    public int THUMB_COLOR = 0xFFb71e3e;
    public int THUMB_LABEL_COLOR = 0xFFFFFFFF;
    public int THUMB_LABEL_SIZE = sp2Px(12f);


    public ThumbDrawable thumbDrawable;
    public TrackDrawable trackDrawable;
    public TextView thumbLabel;
    public TextView trackLabel;


    public SwitchCompatEx(Context context) {
        super(context);

    }

    public SwitchCompatEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        //this.setBackground(null);
        thumbDrawable = new ThumbDrawable();
        trackDrawable = new TrackDrawable();
        thumbLabel = new TextView(context);
        trackLabel = new TextView(context);

        //trackLabel =this.getTextOn();
        thumbLabelPaint = new Paint();
        trackLabelPaint = new Paint();

        thumbLabelPaint.setColor(THUMB_LABEL_COLOR);
        thumbLabelPaint.setAntiAlias(true);
        thumbLabelPaint.setTextSize(THUMB_LABEL_SIZE);
        trackLabelPaint.setColor(TRACK_LABEL_COLOR);
        trackLabelPaint.setAntiAlias(true);
        trackLabelPaint.setTextSize(TRACK_LABEL_SIZE);
        Typeface typeface =  Typeface.createFromAsset(context.getAssets(),"Dubai-Regular.ttf");
        this.setSwitchTypeface(typeface);
        thumbLabelPaint.setTypeface(typeface);
        trackLabelPaint.setTypeface(typeface);
        setTextOff(Global.CURRENT_LOCALE.equals("en")? "English":"العربية");
        setTextOn(Global.CURRENT_LOCALE.equals("ar")? "English":"العربية");
        thumbLabel.setText(this.isChecked()?this.getTextOn():this.getTextOff());
        trackLabel.setText(this.isChecked()?this.getTextOff():this.getTextOn());
        thumbLabel.setTypeface(typeface);
        trackLabel.setTypeface(typeface);

        this.setBackground(null);
        this.setTrackDrawable(trackDrawable);
        this.setThumbDrawable(thumbDrawable);

    }

    public SwitchCompatEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ((GradientDrawable)trackDrawable).setSize(w,h);
        ((GradientDrawable)thumbDrawable).setSize(w/2,h);
    }

    public void  drawLabel( Canvas canvas, Rect bounds, Paint paint , CharSequence text) {

        if(text== null) return;

        RectF tb = new RectF();
        tb.right = paint.measureText(text, 0, text.length());
        tb.bottom = paint.descent() - paint.ascent();
        tb.left += bounds.centerX() - tb.centerX();
        tb.top += bounds.centerY() - tb.centerY() - paint.ascent();

        canvas.drawText(text.toString(), tb.left, tb.top, paint);
    }

    private int sp2Px(Float sp){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, Resources.getSystem().getDisplayMetrics());
    }
    private int dp2Px(Float dp){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public class TrackDrawable extends GradientDrawable{

        private Rect textOffBounds = new Rect();
        private Rect textOnBounds = new Rect();

        public TrackDrawable() {

            setColor(TRACK_COLOR);
            this.setStroke(TRACK_STROKE_WIDTH, TRACK_STROKE_COLOR);

        }

        @Override
        protected void onBoundsChange(Rect r) {
            super.onBoundsChange(r);

            this.setCornerRadius(r.height() / 2f);

            textOffBounds.set(r);
            textOffBounds.right /= 2;

            textOnBounds.set(textOffBounds);
            textOnBounds.offset(textOffBounds.right, 0);
        }

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            invalidate();
            requestLayout();



            //setTextOn(Global.CURRENT_LOCALE.equals("en")? "العربية":"English");

            /*if(Global.CURRENT_LOCALE.equals("en")){
                setTextOn("Arabic");
                //setText("English");
            }
            else{
                setTextOn("English");
                //setText("Arabic");

            }*/
            drawLabel(canvas, textOffBounds, thumbLabelPaint, getTextOff());
            drawLabel(canvas, textOnBounds, trackLabelPaint, getTextOn());
        }
    }
    public class ThumbDrawable extends GradientDrawable{

        private Rect thumbLabelBounds = new Rect();

        public ThumbDrawable() {
            setColor(THUMB_COLOR);
        }

        @Override
        protected void onBoundsChange(Rect r) {
            super.onBoundsChange(r);

            this.setCornerRadius(r.height() / 2f);
            thumbLabelBounds.set(r);
        }

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            invalidate();
            requestLayout();
            //setTextOff(Global.CURRENT_LOCALE.equals("en")? "English":"العربية");

            drawLabel(canvas, thumbLabelBounds, thumbLabelPaint, thumbLabel.getText());
        }
    }
}
