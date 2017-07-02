package me.zhaoliufeng.mylab.NumTextView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhaoliufeng.customviews.NumTexView.NumTextView;
import me.zhaoliufeng.mylab.R;
import me.zhaoliufeng.toolslib.RandomNumberFactory;

/**
 * 自定义控件 数字增长文本框
 */
public class NumTextViewActivity extends Activity {

    @BindView(R.id.numtv_int)
    NumTextView numtvInt;
    @BindView(R.id.edt_int_max)
    EditText edtIntMax;
    @BindView(R.id.edt_int_min)
    EditText edtIntMin;
    @BindView(R.id.edt_decimal_min)
    EditText edtDecimalMin;
    @BindView(R.id.edt_decimal_max)
    EditText edtDecimalMax;
    @BindView(R.id.numtv_decimal)
    NumTextView numtvDecimal;
    private String TAG = "NumTextViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_text_view);
        ButterKnife.bind(this);
    }

    public void integerOnClick(View view) {
        if (edtIntMin.getText().toString().equals("")
                || edtIntMax.getText().toString().equals(""))
            return;
        int minVal = Integer.valueOf(edtIntMin.getText().toString());
        int maxVal = Integer.valueOf(edtIntMax.getText().toString());
        numtvInt.setGrowNum(RandomNumberFactory.getRandomNumber(minVal, maxVal).toString());
    }

    public void decimalOnClick(View view) {
        if (edtDecimalMin.getText().toString().equals("")
                || edtDecimalMax.getText().toString().equals(""))
            return;
        float minVal = Float.valueOf(edtDecimalMin.getText().toString());
        float maxVal = Float.valueOf(edtDecimalMax.getText().toString());
        numtvDecimal.setGrowNum(RandomNumberFactory.getRandomNumber(minVal, maxVal).toString());
    }
}
