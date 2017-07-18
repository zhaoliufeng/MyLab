package me.zhaoliufeng.mylab;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import me.zhaoliufeng.customviews.ColorPicker;

public class ColorPickerActivity extends AppCompatActivity {

    ColorPicker colorPicker;
    View view;
    EditText edtR, edtG, edtB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
        colorPicker = (ColorPicker) findViewById(R.id.colorPicker);
        edtR = (EditText) findViewById(R.id.edtR);
        edtB = (EditText) findViewById(R.id.edtB);
        edtG = (EditText) findViewById(R.id.edtG);

        view = findViewById(R.id.view);

        colorPicker.setColorChangeListener(new ColorPicker.ColorChangeListener() {
            @Override
            public void colorChange(int colorVal, boolean warmMode, boolean isUp) {
                if (!warmMode){
                    Log.e("RGB", Integer.toHexString(colorVal) + "");
                    view.setBackgroundColor(colorVal);
                    edtR.setText(String.valueOf(Color.red(colorVal)));
                    edtG.setText(String.valueOf(Color.green(colorVal)));
                    edtB.setText(String.valueOf(Color.blue(colorVal)));
                }else {
                    Log.e("RGB", "warm val " + colorVal + " cold val " + (255 - colorVal));
                }

            }
        });

    }

    public void onClick(View view) {
        colorPicker.setColor(255, Integer.valueOf(edtR.getText().toString()),
                Integer.valueOf(edtG.getText().toString()),
                Integer.valueOf(edtB.getText().toString()));
    }

    public void changeOnClick(View view) {
        colorPicker.changeColorMode();
    }
}
