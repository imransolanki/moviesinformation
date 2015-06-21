package com.themoviedb.presentation.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.themoviedb.R;

/**
 * Custom class for creating Custom {@link TextView}. This is useful to set
 * custom styles to {@link TextView}
 * 
 * @author Imran
 * 
 */
public class FontTextView extends TextView {
	private static Typeface tf = null;

	public FontTextView(Context context) {
		super(context);
	}

	public FontTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCustomFont(context, attrs);
	}

	public FontTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setCustomFont(context, attrs);
	}

	private void setCustomFont(Context ctx, AttributeSet attrs) {
		TypedArray a = ctx.obtainStyledAttributes(attrs,
				R.styleable.FontTextView);
		String customFont = a.getString(R.styleable.FontTextView_customFont1);
		setCustomFont(ctx, customFont);
		a.recycle();
	}

	public boolean setCustomFont(Context ctx, String asset) {

		FontManager font = FontManager.getInstance(ctx);
		tf = font.getTypeface(asset);

		if (tf != null) {

			setTypeface(tf);
			return true;
		} else {
			return false;
		}
	}
}