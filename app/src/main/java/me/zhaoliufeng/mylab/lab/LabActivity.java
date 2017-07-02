package me.zhaoliufeng.mylab.lab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
}
