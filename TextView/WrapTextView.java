package android.issues.textview;
/*
	This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
import android.content.Context;
import android.text.*;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Wrapping text in TextView
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
