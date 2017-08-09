package me.zhaoliufeng.customviews.NumberPicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.lang.reflect.Field;

/**
 * Created by We-Smart on 2017/7/27.
 */

public class CustomTimePicker extends TimePicker {


    public CustomTimePicker(Context context) {
        super(context);
        init();
    }

    public CustomTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init()
    {
        getNumberPicker(this);
    }


    public void getNumberPicker(TimePicker timePicker)
    {
        try
        {
            Class<?> clazz = Class.forName("com.android.internal.R$id");
            Field fieldHour = clazz.getField("hour");
            fieldHour.setAccessible(true);
            int hourId = fieldHour.getInt(null);
            NumberPicker hourNumberPicker = (NumberPicker) timePicker.findViewById(hourId);
            setDividerColor(hourNumberPicker);
            set_numberpicker_text_colour(hourNumberPicker);

            Field fieldminute = clazz.getField("minute");
            fieldminute.setAccessible(true);
            int minuteId = fieldminute.getInt(null);
            NumberPicker minuteNumberPicker = (NumberPicker) timePicker.findViewById(minuteId);
            setDividerColor(minuteNumberPicker);

            Field fieldampm = clazz.getField("amPm");
            fieldminute.setAccessible(true);
            int ampmId = fieldampm.getInt(null);
            NumberPicker ampmNumberPicker = (NumberPicker) timePicker.findViewById(ampmId);
            setDividerColor(ampmNumberPicker);

            //更改冒号颜色
            Field fieldDivider=clazz.getField("divider");
            fieldDivider.setAccessible(true);
            int dividerId=fieldDivider.getInt(null);
            TextView textView=(TextView)timePicker.findViewById(dividerId);
            textView.setTextColor(Color.parseColor("#FFCF2F"));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
