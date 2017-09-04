package com.scriptgo.www.admingraviflex.compound;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scriptgo.www.admingraviflex.R;

/**
 * Created by BALAREZO on 03/09/2017.
 */

public class ProgressCircularText extends LinearLayout {

    LinearLayout lnl_progresscircular = null;
    String txt_load = null;
    TextView text_load = null;

    Context ctx = null;

    public ProgressCircularText(Context context) {
        super(context);
        ctx = context;
        initializeViews(context);
    }

    public ProgressCircularText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ctx = context;
        initattr(context, attrs);
        initializeViews(context);
    }

    public ProgressCircularText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ctx = context;
        initattr(context, attrs);
        initializeViews(context);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        lnl_progresscircular = (LinearLayout) findViewById(R.id.lnl_progresscircular);
        text_load = (TextView) findViewById(R.id.text_load);

        text_load.setText(txt_load);

    }

    void initattr(Context context, AttributeSet attrs) {
        TypedArray typedArray;
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.AttrProgressCircularText);
        txt_load = typedArray.getString(R.styleable.AttrProgressCircularText_text_load);
        typedArray.recycle();
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.compound_progresscirculartext, this);

    }


}
