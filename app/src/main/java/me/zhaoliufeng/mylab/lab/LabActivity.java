package me.zhaoliufeng.mylab.lab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import me.zhaoliufeng.mylab.CtmTimePicker.CtmTimePickerActivity;
import me.zhaoliufeng.mylab.MusicPlayer.MusicPlayActivity;
import me.zhaoliufeng.mylab.R;

public class LabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab);
    }

    public void viewLocationOnClick(View view) {
        startActivity(new Intent(this, ViewLocationActivity.class));
    }

    public void viewApiOnClick(View view) {
        startActivity(new Intent(this, ViewDrawApiActivity.class));
    }

    public void shaderOnClick(View view) {
        startActivity(new Intent(this, ShaderActivity.class));
    }

    public void pickerOnClick(View view) {
        startActivity(new Intent(this, CtmTimePickerActivity.class));
    }

    public void musicOnClick(View view) {
        startActivity(new Intent(this, MusicPlayActivity.class));
    }

    public void bleOnClick(View view) {
        startActivity(new Intent(this, BleActivity.class));
    }
}
