package com.langexpo.customfunction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatButton;

public class CustomButton extends AppCompatButton {
    public CustomButton(Context context) {
        super(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setPressed(boolean pressed) {
        if (pressed != isPressed()) {
            setGravity(pressed ? Gravity.CENTER_HORIZONTAL|
                    Gravity.BOTTOM : Gravity.CENTER);
        }
        super.setPressed(pressed);
    }
}
