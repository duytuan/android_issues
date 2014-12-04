package jp.shopch.shopchannel.widget;

import android.content.Context;
import android.text.*;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * author: Tozawa
 */
public class WrapTextView extends TextView {

    private CharSequence mOrgText = "";
    private BufferType mOrgBufferType = BufferType.NORMAL;

    public WrapTextView(Context context) {
        super(context);
        setFilters(new InputFilter[]{new WrapTextViewFilter(this)});
    }

    public WrapTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFilters(new InputFilter[]{new WrapTextViewFilter(this)});
    }

    public WrapTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFilters(new InputFilter[]{new WrapTextViewFilter(this)});
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        setText(mOrgText, mOrgBufferType);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        mOrgText = text;
        mOrgBufferType = type;
        super.setText(text, type);
    }

    @Override
    protected void onAttachedToWindow() {
        setText(mOrgText, mOrgBufferType);
        super.onAttachedToWindow();
    }

    @Override
    public CharSequence getText() {
        return mOrgText;
    }

    @Override
    public int length() {
        return mOrgText.length();
    }

    private class WrapTextViewFilter implements InputFilter {
        private final TextView view;

        public WrapTextViewFilter(TextView view) {
            this.view = view;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            int w = view.getWidth();
            if (w <= 0) {
                return source;

            } else {
                TextPaint paint = view.getPaint();
                int wpl = view.getCompoundPaddingLeft();
                int wpr = view.getCompoundPaddingRight();
                int width = w - wpl - wpr;

                SpannableStringBuilder result = new SpannableStringBuilder(source);
                int num_appends = 0;
                for (int index = start; index < end; index++) {
                    if (Layout.getDesiredWidth(source, start, index + 1, paint) > width) {
                        result.insert(index + num_appends, "\n");
                        num_appends++;
                        start = index;

                    } else if (source.charAt(index) == '\n') {
                        start = index;
                    }
                }

                return result;
            }
        }
    }
}
