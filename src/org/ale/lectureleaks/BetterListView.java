package org.ale.lectureleaks;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ListView;

public class BetterListView extends ListView{
    
    public BetterListView(Context context) {
        super(context);
    }
    
    public BetterListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BetterListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public int getSolidColor() {
        return Color.BLACK; 
    }
    
}
