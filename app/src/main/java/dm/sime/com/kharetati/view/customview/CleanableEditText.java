package dm.sime.com.kharetati.view.customview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.utility.Global;


public class CleanableEditText extends AppCompatEditText implements View.OnTouchListener, View.OnFocusChangeListener, TextWatcherAdapter.TextWatcherListener {

    @Override
    public void onTextChanged(EditText view, String text) {
        if (isFocused()) {

            setClearIconVisible(isNotEmpty(text));

        }
    }

    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static enum Location {
        LEFT(0), RIGHT(2);
        final int idx;

        private Location(int idx) {
            this.idx = idx;
        }
    }

    public interface Listener {
        void didClearText();
    }

    public CleanableEditText(Context context) {
        super(context);
        init();
    }

    public CleanableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CleanableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setIconLocation(Location loc) {
        this.loc = loc;
        initIcon();
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setRegXPattern(String regXPattern) {
        this.regXPattern = regXPattern;
    }

    public String getRegXPattern() {
        return this.regXPattern;
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        this.l = l;
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener f) {
        this.f = f;
    }

    private Location loc =Global.CURRENT_LOCALE.equals("en")? Location.RIGHT:Location.LEFT;
    private String type;
    private String regXPattern;

    private Drawable xD;

    private Listener listener;

    private OnTouchListener l;

    private OnFocusChangeListener f;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getDisplayedDrawable() != null) {

            int x = (int) event.getX();

            int y = (int) event.getY();

            int left = (loc == Location.LEFT) ? 0 : getWidth() - getPaddingRight() - xD.getIntrinsicWidth();

            int right = (loc == Location.LEFT) ? getPaddingLeft() + xD.getIntrinsicWidth() : getWidth();

            boolean tappedX = x >= left && x <= right && y >= 0 && y <= (getBottom() - getTop());

            if (tappedX) {

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    setText("");

                    if (listener != null) {

                        listener.didClearText();

                    }

                }
                return true;
            }
        }

        if (l != null) {
            return l.onTouch(v, event);
        }

        return false;

    }



    @Override

    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(isNotEmpty(getText()));
        } else {

            setClearIconVisible(false);

        }

        if (f != null) {

            f.onFocusChange(v, hasFocus);

        }

    }


    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);
        initIcon();
    }



    public void init() {
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(new TextWatcherAdapter(this, this));
        initIcon();

        setClearIconVisible(false);

    }



    private void initIcon() {

        xD = null;

        if (loc != null) {

            xD = getCompoundDrawables()[loc.idx];

        }

        if (xD == null) {

            xD = getResources().getDrawable(R.drawable.ic_cancel);

        }

        xD.setBounds(0, 0, xD.getIntrinsicWidth(), xD.getIntrinsicHeight());

        int min = getPaddingTop() + xD.getIntrinsicHeight() + getPaddingBottom();

        if (getSuggestedMinimumHeight() < min) {

            setMinimumHeight(min);

        }

    }



    private Drawable getDisplayedDrawable() {

        return (loc != null) ? getCompoundDrawables()[loc.idx] : null;

    }



    protected void setClearIconVisible(boolean visible) {

        Drawable[] cd = getCompoundDrawables();

        Drawable displayed = getDisplayedDrawable();

        boolean wasVisible = (displayed != null);

        if (visible != wasVisible) {

            Drawable x = visible ? xD : null;
            if(Global.CURRENT_LOCALE.equals("en"))
                super.setCompoundDrawables( cd[0], cd[1], (loc == Location.RIGHT) ? x : cd[2], cd[3]);
            else
                super.setCompoundDrawables((loc == Location.LEFT) ? x : cd[0],cd[1], cd[2],cd[3]);

        }

    }

}