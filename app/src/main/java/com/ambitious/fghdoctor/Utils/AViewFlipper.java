package com.ambitious.fghdoctor.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

import com.ambitious.fghdoctor.R;

public class AViewFlipper extends ViewFlipper {

    Paint paint = new Paint();

    public AViewFlipper(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void dispatchDraw(Canvas canvas)
    {
        super.dispatchDraw(canvas);
        int width = getWidth();

        float margin = 4;
        float radius = 10;
        float cx = width / 2-((radius + margin) * 2 * getChildCount() / 2);
        float cy = getHeight()-10;

        canvas.save();

        for (int i = 0; i < getChildCount(); i++)
        {
            if (i == getDisplayedChild())
            {
                paint.setColor(getResources().getColor(R.color.colorBase));
                canvas.drawCircle(cx, cy, radius, paint);
            } else
            {
//                paint.setColor(getResources().getColor(R.color.colorBlack));
                paint.setColor(Color.parseColor("#DAE8F3"));
                canvas.drawCircle(cx, cy, radius, paint);
            }
            cx += 2 * (radius + margin);
        }
        canvas.restore();
    }

}


