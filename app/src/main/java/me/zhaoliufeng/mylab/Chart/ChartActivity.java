package me.zhaoliufeng.mylab.Chart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhaoliufeng.customviews.Chart.Histogram;
import me.zhaoliufeng.customviews.Chart.LineChart;
import me.zhaoliufeng.mylab.R;
import me.zhaoliufeng.toolslib.NumberFindUtils;

public class ChartActivity extends AppCompatActivity {

    @BindView(R.id.lineChart)
    LineChart lineChart;

    String[] yTitle;

    final String[] xTitle = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28"};

    float[] data = {3, 6, 25, 20, 10, 20, 16, 13, 5, 10, 6, 7, 5, 12, 11, 25, 13, 5, 8, 17, 14, 14, 20, 16, 19, 6, 1, 20};
    @BindView(R.id.histogram)
    Histogram histogram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        ButterKnife.bind(this);
        yTitle = new String[6];
        getYTitle();
        lineChart.setData(data, xTitle, yTitle);

        int[] data = {6, 13, 5, 5, 10};
        String[] titles = {"数据一", "数据二", "数据三", "数据四", "数据五"};
        histogram.setHistogramData(data, titles);
    }

    //计算纵坐标标题的最大值 已5为单位 计算得出纵坐标标题
    private void getYTitle() {
        int yTitleMax = (((int) NumberFindUtils.getMaxNumber(data) / 5) + 1) * 5;
        for (int i = yTitleMax, j = 0; i >= 0; i -= yTitleMax / 5, j++) {
            yTitle[j] = String.valueOf(i);
        }

        for (int i = 0; i < data.length; i++) {
            data[i] = data[i] * ((float) 100.0 / yTitleMax);
        }
    }
}
