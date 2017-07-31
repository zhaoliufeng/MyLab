package me.zhaoliufeng.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.lang.reflect.Field;

/**
 * Created by We-Smart on 2017/7/27.
 */

public class CustomNumberPicker extends NumberPicker {
    public CustomNumberPicker(Context context) {
        super(context);
        init();
    }

    public CustomNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setDividerColor(this);
        set_numberpicker_text_colour(this);
    }

    public void setDividerColor(NumberPicker picker)
    {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields)
        {
            Log.v("setDividerColor", "pf:" + pf.getName() + " type :" + pf.getGenericType());
            if (pf.getName().equals("mSelectionDivider"))//能找到这个域 （分割线视图)
            {
                Log.v("setDividerColor", "find......mSelectionDivider");
                pf.setAccessible(true);
                try
                {
                    ColorDrawable colorDrawable = new ColorDrawable();
                    colorDrawable.setColor(Color.parseColor("#FFCF2F"));
                    pf.set(picker, colorDrawable);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            if (pf.getName().equals("mSelectionDividerHeight"))//找不到这个私有域，（分割线的厚度）
            {
                Log.v("PowerSet", "find......mSelectionDividerHeight.");
                pf.setAccessible(true);
                try {
                    int result = 1;
                    pf.set(picker, result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private void set_numberpicker_text_colour(NumberPicker number_picker){
        final int count = number_picker.getChildCount();
        final int color = Color.parseColor("#FEC22B");

        for(int i = 0; i < count; i++){
            View child = number_picker.getChildAt(i);

            try{
                Field wheelpaint_field = number_picker.getClass().getDeclaredField("mSelectorWheelPaint");
                wheelpaint_field.setAccessible(true);

                ((Paint)wheelpaint_field.get(number_picker)).setColor(color);
                ((EditText)child).setTextColor(color);
                number_picker.invalidate();
            }
            catch(NoSuchFieldException e){
                Log.w("NoSuchField", e);
            }
            catch(IllegalAccessException e){
                Log.w("IllegalAccess", e);
            }
            catch(IllegalArgumentException e){
                Log.w("IllegalArgument", e);
            }
        }
    }
}
