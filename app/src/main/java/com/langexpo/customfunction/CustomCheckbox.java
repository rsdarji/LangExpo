package com.langexpo.customfunction;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatCheckBox;

public class CustomCheckbox extends AppCompatCheckBox {
    public CustomCheckbox(Context context) {
        super(context);
    }

    public CustomCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCheckbox(Context context, AttributeSet attrs, int defStyleAttr) {
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
