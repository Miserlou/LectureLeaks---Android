package org.ale.lectureleaks;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class BetterScrollView extends ScrollView{
    
    public BetterScrollView(Context context) {
        super(context);
    }
    
    public BetterScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BetterScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public int getSolidColor() {
        return Color.BLACK; 
    }
    
}
