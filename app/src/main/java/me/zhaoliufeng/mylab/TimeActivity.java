package me.zhaoliufeng.mylab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import java.sql.Time;

public class TimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        TimePicker timePicker = (TimePicker) findViewById(R.id.tpk);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Log.i("TIME", getAlarmShow(hourOfDay, minute) + "");
            }
        });
    }

    public static String getAlarmShow(int hours, int minutes) {
        String data = "";
        if (hours >= 0 && hours < 12) {
            data = alarmShow(hours == 0 ? 12 : hours) + ":" + alarmShow(minutes) + " AM";
        } else {
            data = alarmShow(hours - 12) + ":" + alarmShow(minutes) + "PM";
        }
        return data;
    }

    public static String alarmShow(int time) {
        if (time < 10) {
            return "0" + time;
        } else {
            return Integer.toString(time);
        }
    }
}
